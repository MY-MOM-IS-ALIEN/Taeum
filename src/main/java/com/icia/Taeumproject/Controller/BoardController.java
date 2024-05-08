package com.icia.Taeumproject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.Taeumproject.Dto.BoardDto;
import com.icia.Taeumproject.Service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j

public class BoardController {
	 @Autowired
	 private BoardService bServ;
	
	@GetMapping("board")
	public String board(Model model) {
		log.info("board");
		
		bServ.boardList(model);
		return "board";
	}
	
	
	@GetMapping("boardWrite")
	public String boardWrite() {
		log.info("boardWrite");
		
		return "boardWrite";
	}
	
	// 게시글 작성하기 컨트롤러
	@PostMapping("bWriteProc")
	public String bWriteProc(BoardDto board, RedirectAttributes rttr) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName(); // 현재 로그인한 사용자의 아이디
	    
	    bServ.bWriteProc(board, rttr, username);
	    
	    // 게시글 작성 후 게시글 목록 페이지로 리다이렉트
	    return "redirect:/board";
	}
	
	@GetMapping("boardDetail")
	public String boardDetail(int B_ID, Model model) {
		log.info("boardDetail");
		
		bServ.boardDetail(B_ID, model);
		return "boardDetail";
	}
	
}














