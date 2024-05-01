package com.icia.Taeumproject.Dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Node {

  private Integer node_id;
  private Double x;
  private Double y;
  private String Address;
  private int M_ID;
  private int DR_ID;
  private int kind;
  private int cycle;
  private String status;
  private String A_DATE;
  private long D_ID;
}




