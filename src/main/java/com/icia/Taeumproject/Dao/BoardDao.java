package com.icia.Taeumproject.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.icia.Taeumproject.Dto.BoardDto;
import com.icia.Taeumproject.Dto.CommentDto;

@Mapper
public interface BoardDao {
	
	void insertBoard(BoardDto board);
	
	List<BoardDto> getBoardList();

	BoardDto getBoardById(int id);

	// 댓글 가져오기
	List<CommentDto> getComments(int id);
	// 댓글 작성하기
	void insertComment(CommentDto comment);
	
}
