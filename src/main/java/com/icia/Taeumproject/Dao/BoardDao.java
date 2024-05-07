package com.icia.Taeumproject.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.icia.Taeumproject.Dto.BoardDto;

@Mapper
public interface BoardDao {
	
	void insertBoard(BoardDto board);
	
	List<BoardDto> getBoardList();
	
}
