package com.icia.Taeumproject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.icia.Taeumproject.Service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j

public class BoardController {
	 @Autowired
	 private BoardService bServ;
	
	@GetMapping("board")
	public String board() {
		log.info("board");
		
		return "board";
	}
	
	@PostMapping("boardProc")
	public String boardProc() {
		log.info("boardProc");
			
		
		return "board";
	}
	
}
