package com.ll.techinterview.global.enums;

public enum InterviewType {
  DEFAULT("Default"),
  PRIVATE("Pirvate"),
  PUBLIC("Public");

  private final String displayName;

  InterviewType(String displayName){this.displayName = displayName;}

  public String getDisplayName(){return displayName;}
}
