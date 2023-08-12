package com.ksoot.problem.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pet {

  private Long id;

  private String name;

  private String category;

  private String[] tags;

  private Status status;

  public static enum Status {
    AVAILABLE, PENDING, SOLD;
  }
}
