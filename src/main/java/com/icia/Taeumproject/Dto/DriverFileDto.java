package com.icia.Taeumproject.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverFileDto {
	private int dp_id;
	private int M_ID;//외래키 드라이버 ID
	private String dp_oriname;
	private String dp_sysname;
}