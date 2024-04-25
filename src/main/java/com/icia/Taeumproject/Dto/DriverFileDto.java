package com.icia.Taeumproject.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverFileDto {
	private int df_id;
	private int DR_M_ID;//외래키 드라이버 ID
	private String df_oriname;
	private String df_sysname;
}