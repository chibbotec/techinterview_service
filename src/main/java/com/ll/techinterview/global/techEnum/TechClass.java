package com.ll.techinterview.global.techEnum;

public enum TechClass {
  JAVASCRIPT("JavaScript"),
  TYPESCRIPT("TypeScript"),
  REACT("React"),
  VUE("Vue"),
  ANGULAR("Angular"),
  NODE_JS("Node.js"),
  JAVA("Java"),
  SPRING("Spring"),
  PYTHON("Python"),
  DJANGO("Django"),
  DATABASE("Database"),
  DEVOPS("DevOps"),
  MOBILE("Mobile"),
  ALGORITHM("Algorithm"),
  COMPUTER_SCIENCE("Computer Science"),
  OS("OS"),
  NETWORK("Network"),
  SECURITY("Security"),
  CLOUD("Cloud"),
  ETC("ETC");

  private final String displayName;

  TechClass(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}