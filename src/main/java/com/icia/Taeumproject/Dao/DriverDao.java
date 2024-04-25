package com.icia.Taeumproject.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.icia.Taeumproject.Dto.ApplyDto;
import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.DriverFileDto;

@Mapper
public interface DriverDao {

	int getTodayTraffic(int m_id);

	int getMonthTraffic(int m_id);

	int getTotalTraffic(int m_id);

	List<ApplyDto> getPassengerList(int m_id);

	List<ApplyDto> getRouteList(int m_id);

//	String getDriverImage(String email);

	DriverDto getDriverInfo(int m_id);

	DriverDto getInfo(int m_id);

	void insertFile(DriverFileDto dfd);

	void insertDriver(DriverDto driver);









}
