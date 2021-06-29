package com.sap.oss.phosphor.fosstars.tool;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A config for reporting.
 */
public class ReportConfig {

  /**
   * Types of reports.
   */
  public enum ReportType {
    MARKDOWN, JSON, ISSUES
  }

  /**
   * A type of a report.
   */
  final ReportType type;

  /**
   * Where a report should be stored.
   */
  final String where;

  /**
   * A source of data.
   */
  final String source;

  /**
   * Creates a new config.
   *
   * @param type A type of a report.
   * @param where Where a report should be stored.
   * @param source A source of data
   */
  ReportConfig(
      @JsonProperty("type") ReportType type,
      @JsonProperty("where") String where,
      @JsonProperty("source") String source) {

    this.type = type;
    this.where = where;
    this.source = source;
  }
}
