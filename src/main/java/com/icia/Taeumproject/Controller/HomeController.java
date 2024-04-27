package com.icia.Taeumproject.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.icia.Taeumproject.Dto.ApplyDto;
import com.icia.Taeumproject.Dto.SearchDto;
import com.icia.Taeumproject.Service.ApplyService;
import com.icia.Taeumproject.Service.MainService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@Slf4j
public class HomeController {
      @Autowired
      private ApplyService aServ;
      
      @Autowired
      private MainService maServ;
      
      @GetMapping("applyList")
      public String applyList(SearchDto sdto, HttpSession session , Model model) {
        log.info("applyList()");
        String view = aServ.getApplyList(sdto,session,model);
        return view;
      }
      
      @GetMapping("mainOfmain")
      public String mainOfmain() {
    	  return "mainOfmain";
      }
      
     @GetMapping("applyForm")
     public String applyForm() {
       
         return "applyForm";
     }
         @PostMapping("applyProc")
         public String applyProc(ApplyDto apply, RedirectAttributes rttr) {
             log.info("applyProc");

             // 현재 로그인한 사용자 정보 가져오기
             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

             String view = aServ.applyWrite(apply, rttr);
            
             
             
             return view;
         }
     }
