package com.icia.Taeumproject.Controller;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.icia.Taeumproject.Service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.icia.Taeumproject.Dto.MemberDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j

public class MemberController {

  @Autowired
  MemberService mServ; 

  @GetMapping("/")
  public String home() {
    log.info("/");
   
    return "home";
  }

  @GetMapping("joinForm")
  public String joinForm() {
    log.info("joinForm");

    return "joinForm";
  }

  @PostMapping("joinProc")
  public String joinProc(MemberDto member, RedirectAttributes rttr ) {

    log.info("joinProc()");
    String view = mServ.memberJoin(member, rttr);

    return view;
  }
  @GetMapping("loginForm")
  public String loginForm() {
    log.info("loginForm()");
    return "loginForm";
  } 
  @PostMapping("loginProc")
  public String loginProc(MemberDto member , Model model, RedirectAttributes rttr) {
   log.info("loginProc()");
     return mServ.loginProc(model,rttr, member);
  }
  
//메일 인증 메핑 메소드
  @GetMapping("authUser")
  public String authUser() {
    log.info("authUser()");
    return "authUser";
  }
  @GetMapping("pwdChange")
  public String pwdChange() {
    log.info("pwdChange()");
    return "pwdChange";
  }
  
  @GetMapping("findEmail")
  public String findEmail() {
    log.info("findEmail()");
    
    return "findEmail";
  }
  
  @PostMapping("findById")
  public String findById(String m_name,String m_phone,RedirectAttributes rttr) {
    log.info("findById()");
    log.info(m_name);
    
    return mServ.findById(m_name,m_phone,rttr);
  }
  
}
