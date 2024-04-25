package com.icia.Taeumproject.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.icia.Taeumproject.Dto.ApplyDto;
import com.icia.Taeumproject.Dto.SearchDto;
@Mapper
public interface ApplyDao {

  List<ApplyDto> selectApplyList(SearchDto sdto);

  int selectApplyCnt(SearchDto sDto);

  void insertApply(ApplyDto apply);

  ApplyDto selectApplyCnt(int a_ID);

}
