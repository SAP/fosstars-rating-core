package com.sap.sgs.phosphor.fosstars.model.advice.oss;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.advice.AdviceContentStorage;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * The class stores advices
 * for {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures}.
 */
public class OssAdviceContentStorage extends AdviceContentStorage {

  /**
   * A path to a resource that contains the default advices.
   */
  private static final String RESOURCE_PATH
      = "com/sap/sgs/phosphor/fosstars/model/advice/oss/OssAdvices.yml";

  /**
   * A default instance of the storage that contains advices loaded from {@link #RESOURCE_PATH}.
   */
  public static final OssAdviceContentStorage DEFAULT;

  static {
    try {
      OssSecurityRating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
      DEFAULT = new OssAdviceContentStorage(RESOURCE_PATH, rating);
    } catch (IOException e) {
      throw new UncheckedIOException("Could not load advices", e);
    }
  }

  /**
   * Create a new instance and load advices from a resource for a {@link OssSecurityRating}.
   *
   * @param path A path to the resource.
   * @param rating The rating.
   * @throws IOException If the advices couldn't be loaded.
   */
  public OssAdviceContentStorage(String path, OssSecurityRating rating) throws IOException {
    super(path, rating);
  }

}
