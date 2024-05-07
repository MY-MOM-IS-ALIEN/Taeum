package com.icia.Taeumproject.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.Taeumproject.Dao.BoardDao;
import com.icia.Taeumproject.Dto.BoardDto;
import com.icia.Taeumproject.Dto.MemberDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class BoardService {
	@Autowired
	private BoardDao bDao;
	
	public void bWriteProc(BoardDto board, RedirectAttributes rttr, String username) {
	    log.info("bWriteProc = " + board.getB_DATE());
	    
	    // 인증 객체 가져오기
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    // 권한 정보 추출
	    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	    List<String> roles = new ArrayList<>();
	    for (GrantedAuthority authority : authorities) {
	        roles.add(authority.getAuthority());
	    }
	    log.info("roles: " + roles);
	    
	    // 게시글 작성자 설정
	    board.setUsername(username);
	    
	    // 추출한 권한 정보를 문자열로 변환하여 게시글의 권한 설정
	    board.setRole(String.join(",", roles));
	    
	    // 게시글 저장
	    try {
	        bDao.insertBoard(board);
	        rttr.addFlashAttribute("msg", "작성 완료");
	    } catch (Exception e) {
	        log.error("게시글 작성 중 오류 발생: " + e.getMessage());
	        rttr.addFlashAttribute("error", "게시글 작성 중 오류가 발생했습니다.");
	    }
	}


	public void boardList(Model model) {
		List<BoardDto>boardList = bDao.getBoardList();
		model.addAttribute("boardList",boardList);
		
	}


	public void boardDetail(int b_ID, Model model) {
		BoardDto board = bDao.boardDetail(b_ID);
		model.addAttribute(board);
		
	}
}
