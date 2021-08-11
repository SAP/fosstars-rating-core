package com.sap.oss.phosphor.fosstars;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Visitor;
import java.util.ArrayList;
import java.util.List;

public class ScoreCollector implements Visitor {

  private final List<Score> scores = new ArrayList<>();

  @Override
  public void visit(Score score) {
    scores.add(score);
  }

  @Override
  public void visit(Rating rating) {
    // do nothing
  }

  @Override
  public void visit(Feature<?> feature) {
    // do nothing
  }

  @Override
  public void visit(Parameter parameter) {
    // do nothing
  }

  public List<Score> scores() {
    return new ArrayList<>(scores);
  }
}
