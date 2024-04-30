package com.icia.Taeumproject.Dao;

import org.apache.ibatis.annotations.Mapper;

import com.icia.Taeumproject.Dto.MemberDto;

@Mapper
public interface MemberDao {

  // 회원가입 메서드
  void insertMember(MemberDto member);

  // 로그인 비밀번호를 가져오는 메서드
  String selectPassword(String username);

  // 로그인 후 회원 정보를 가져오는 메서드
  MemberDto selectMember(String username);

  // 이메일 중복 체크 메서드
  int emailCheck(String memail);
  
  //메일주소를 가져오는 메서드
  String selectEmail(String M_NAME);

  MemberDto findUserByEmail(String username);

  String findById(String m_name, String m_phone);

  int updateRole(int m_id);

  void updateDriveMemberUpdate(MemberDto member);

}
