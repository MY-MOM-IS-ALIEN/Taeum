package com.icia.Taeumproject.Dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
	private String Name;
	private String Address;
	private int m_id;
	private int ride;
	private int kind;
	private int cycle;
	private LocalDateTime A_DATE;
	private Timestamp A_LOCALDATE;
}
