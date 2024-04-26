package com.icia.Taeumproject.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.Taeumproject.Dao.ApplyDao;
import com.icia.Taeumproject.Dao.MemberDao;
import com.icia.Taeumproject.Dto.ApplyDto;
import com.icia.Taeumproject.Dto.SearchDto;
import com.icia.Taeumproject.util.KakaoApiUtil;
import com.icia.Taeumproject.util.KakaoApiUtil.Point;
import com.icia.Taeumproject.util.PagingUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApplyService {
  @Autowired
  private ApplyDao aDao;
  @Autowired
  private MemberDao mDao;
  
  @Autowired
  private MainService maServ;
//transaction 관련


  private int lcnt = 10;// 한 화면(페이지)에 보여질 게시글 개수

  public String getApplyList(SearchDto sdto, HttpSession session, Model model) {
    log.info("getApplyList()");
    String view = "ApplyList";

    // DB에서 게시글 목록 가져오기
    int num = sdto.getListCnt();

    if (sdto.getListCnt() == 0) {
      sdto.setListCnt(lcnt);// 목록 개수 값 설정 (초기 10개)
    }

    sdto.setPageNum((num - 1) * sdto.getListCnt());
    List<ApplyDto> aList = aDao.selectApplyList(sdto);
    model.addAttribute("alist", aList);
    // 페이지 관련 내용 세션에 저장
    if (sdto.getColname() != null) {
      session.setAttribute("sdto", sdto);
    } else {
      session.removeAttribute("sdto");// 검색을 안한 목록을 위해 삭제.
    }
    // 별개로 페이지 번호도 저장
    session.setAttribute("pageNum", num);

    return view;
  }

  private String getPaging(SearchDto sDto) {
    log.info("getPaging()");
    String pageHtml = null;
    // 전체 게시글 개수
    int maxNum = aDao.selectApplyCnt(sDto);

    int pageCnt = 3; // 페이지에서 보여질 페이지 번호 개수

    String listName = "ApplyList?";
    if (sDto.getColname() != null) {
      // 검색 기능을 사용한 경우
      listName += "colname=" + sDto.getColname() + "&keyword=" + sDto.getKeyword() + "&";
      // <a href='/boardList?colname=b_title&keyword=3&pageNum=1...
    }
    PagingUtil paging = new PagingUtil(maxNum, sDto.getPageNum(), sDto.getListCnt(), pageCnt, listName);
    pageHtml = paging.makePaging();

    return pageHtml;
  }

  // 게시글 , 파일 저장 및 회원 정보 변경
  @Transactional
  public String applyWrite(ApplyDto apply, RedirectAttributes rttr) {
      // 현재 로그인한 사용자 정보 가져오기
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();
      
      String view = null;
      String msg = null;

      try {
          // 게시글 저장
          aDao.insertApply(apply);
          if(apply.getA_STARTADRESS() != null && !apply.getA_STARTADRESS().isEmpty()) {
        	  Point startPoint = KakaoApiUtil.getPointByAddress(apply.getA_STARTADRESS());
        	  
        	  if(startPoint != null) {
        		  int kind  = 1;
        		  maServ.insertServ(apply.getM_ID(),apply.getA_STARTADRESS(), startPoint, kind, rttr, apply.getA_LOCALDATE(), apply.getA_DATE());
        	  }
          } 
          if(apply.getA_ENDADRESS() != null && !apply.getA_ENDADRESS().isEmpty()) {
        	  Point endPoint = KakaoApiUtil.getPointByAddress(apply.getA_ENDADRESS());
        	  
        	  if(endPoint != null) {
        		  int kind = 2;
        		  maServ.insertServ(apply.getM_ID(),apply.getA_ENDADRESS(), endPoint, kind, rttr, null, null);
        	  }
          }
          // 사용자 정보 업데이트
          mDao.findUserByEmail(username);;
          view = "redirect:/";
          msg = "작성 성공";
      } catch (Exception e) {
          e.printStackTrace();
          view = "redirect:/applyForm";
          msg = "작성 실패";
      }

      // 리다이렉트된 페이지에서 사용할 수 있도록 Flash Attribute로 메시지 전달
      rttr.addFlashAttribute("msg", msg);

      return view;
  }

  public String getApply(int A_ID , Model model) {
    log.info("getApply()");
    ApplyDto apply = aDao.selectApplyCnt(A_ID);
    model.addAttribute("apply" , apply);
    return "applyDetail";
  }



}