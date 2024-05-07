package com.icia.Taeumproject.Dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.icia.Taeumproject.Dto.ApplyDto;
import com.icia.Taeumproject.Dto.Node;
import com.icia.Taeumproject.Dto.SearchDto;

@Mapper
public interface ApplyDao {

	// 사용자 신청
	void insertApply(ApplyDto apply);

	ApplyDto selectApplyCnt(int a_ID);

	// 중복 신청 체크
	boolean sDuplicateApply(int m_ID, String formattedDate);
	
	// 사용자 신청 내역 가져오기
	List<ApplyDto> getApplyList(int m_id);

	// 사용자 신청 삭제
	void cancelApply(int applyId);

  List<ApplyDto> selectAllMember();
	

}