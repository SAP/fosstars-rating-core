package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This is an implementation of {@link ValueSet} based on a hash map.
 */
@JsonSerialize(using = ValueHashSet.Serializer.class)
@JsonDeserialize(using = ValueHashSet.Deserializer.class)
@JsonTypeName("ValueHashSet")
public class ValueHashSet implements ValueSet {

  /**
   * A mapping from a feature to its value.
   */
  private final Map<Feature<?>, Value<?>> featureToValue = new HashMap<>();

  /**
   * Initializes a new {@link ValueHashSet} with a number of values.
   *
   * @param values The values.
   * @return The new {@link ValueHashSet}.
   */
  public static ValueHashSet from(Value<?>... values) {
    Objects.requireNonNull(values, "Values can't be null!");
    return new ValueHashSet(values);
  }

  /**
   * Initializes an empty {@link ValueHashSet}.
   */
  public ValueHashSet() {

  }

  /**
   * Initializes a {@link ValueHashSet} with a number of values.
   *
   * @param values The values.
   */
  public ValueHashSet(Set<Value<?>> values) {
    this(Objects.requireNonNull(values, "Values can't be null!").toArray(new Value[0]));
  }

  /**
   * Initializes a {@link ValueHashSet} with a number of values.
   *
   * @param values The values.
   */
  public ValueHashSet(Value<?>... values) {
    Objects.requireNonNull(values, "Values can't be null!");
    for (Value<?> value : values) {
      update(value);
    }
  }

  /**
   * Create an empty value set.
   *
   * @return An empty {@link ValueHashSet}.
   */
  public static ValueHashSet empty() {
    return new ValueHashSet();
  }

  /**
   * Fills out a {@link ValueHashSet} with unknown values for the specified features.
   *
   * @param features A collection of features.
   * @return An instance of {@link ValueHashSet}.
   */
  public static ValueHashSet unknown(Collection<Feature<?>> features) {
    ValueHashSet values = empty();
    for (Feature<?> feature : features) {
      if (values.has(feature)) {
        throw new IllegalArgumentException(String.format(
            "Hey! You just gave me a duplicate feature: %s", feature));
      }
      values.update(UnknownValue.of(feature));
    }
    return values;
  }

  /**
   * Fills out a {@link ValueHashSet} with unknown values for the specified features.
   *
   * @param features A number of features.
   * @return An instance of {@link ValueHashSet}.
   */
  public static ValueHashSet unknown(Feature<?>... features) {
    return unknown(Arrays.asList(features));
  }

  @Override
  public <T> boolean has(Feature<T> feature) {
    Objects.requireNonNull(feature, "Oh no! Feature is null");
    return featureToValue.containsKey(feature);
  }

  @Override
  public ValueSet update(Value<?>... values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");
    for (Value<?> value : values) {
      featureToValue.put(value.feature(), value);
    }
    return this;
  }

  @Override
  public ValueSet update(ValueSet values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");
    for (Value<?> value : values) {
      update(value);
    }
    return this;
  }

  @Override
  public ValueSet update(Set<Value<?>> values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");
    return update(values.toArray(new Value[0]));
  }

  @Override
  public Set<Value<?>> toSet() {
    return new HashSet<>(featureToValue.values());
  }

  @Override
  public int size() {
    return featureToValue.size();
  }

  @Override
  public <T> Optional<Value<T>> of(Feature<T> feature) {
    Value<T> value = (Value<T>) featureToValue.get(feature);
    if (value == null) {
      return Optional.empty();
    }
    return Optional.of(value);
  }

  @Override
  public boolean isEmpty() {
    return featureToValue.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ValueHashSet == false) {
      return false;
    }
    ValueHashSet that = (ValueHashSet) o;
    return Objects.equals(featureToValue, that.featureToValue);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(featureToValue);
  }

  @Override
  public boolean containsAll(Set<Feature<?>> features) {
    Objects.requireNonNull(features, "Oh no! Features is null");
    return featureToValue.keySet().containsAll(features);
  }

  @Override
  public Iterator<Value<?>> iterator() {
    return toSet().iterator();
  }

  /**
   * The default serialized for {@link ValueHashSet}.
   */
  static class Serializer extends StdSerializer<ValueHashSet> {

    /**
     * This default constructor is to make Jackson happy.
     */
    Serializer() {
      this(null);
    }

    Serializer(Class<ValueHashSet> clazz) {
      super(clazz);
    }

    @Override
    public void serialize(ValueHashSet valueHashSet, JsonGenerator gen,
        SerializerProvider serializerProvider) {

      throw new UnsupportedOperationException("This should not be called!");
    }

    @Override
    public void serializeWithType(ValueHashSet valueHashSet, JsonGenerator gen,
        SerializerProvider serializers, TypeSerializer typeSer) throws IOException {

      WritableTypeId typeId = typeSer.typeId(valueHashSet,JsonToken.START_ARRAY);
      typeSer.writeTypePrefix(gen, typeId);

      for (Map.Entry<Feature<?>, Value<?>> entry : valueHashSet.featureToValue.entrySet()) {
        Feature<?> feature = entry.getKey();
        Value<?> value = entry.getValue();
        if (!feature.equals(value.feature())) {
          throw new IllegalArgumentException("Feature doesn't match!");
        }
        gen.writeObject(value);
      }

      typeId.wrapperWritten = !gen.canWriteTypeId();
      typeSer.writeTypeSuffix(gen, typeId);
    }
  }

  /**
   * The default deserialized for {@link ValueHashSet}.
   */
  static class Deserializer extends StdDeserializer<ValueHashSet> {

    /**
     * This default constructor is to make Jackson happy.
     */
    Deserializer() {
      this(null);
    }

    protected Deserializer(Class<?> clazz) {
      super(clazz);
    }

    @Override
    public ValueHashSet deserialize(JsonParser jsonParser,
        DeserializationContext deserializationContext) throws IOException {

      ValueHashSet valueHashSet = new ValueHashSet();

      Value<?>[] values = jsonParser.getCodec().readValue(jsonParser, Value[].class);
      for (Value<?> value : values) {
        valueHashSet.update(value);
      }

      return valueHashSet;
    }
  }
}
