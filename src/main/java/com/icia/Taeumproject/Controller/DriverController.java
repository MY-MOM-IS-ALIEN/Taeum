package com.icia.Taeumproject.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.MemberDto;
import com.icia.Taeumproject.Dto.SecurityUserDTO;
import com.icia.Taeumproject.Service.DriverService;
import com.icia.Taeumproject.Service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DriverController {

	@Autowired
	private DriverService drServ;
	
	@Autowired
	private MemberService mServ;
	
	@GetMapping("driverMain")
	public String driverMain(Model model) {
		log.info("driverMain()");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 로그인 시 정보 가져오기
		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		log.info("m_id: {}", m_id);

		drServ.getDriverInfo(m_id, model);
		
		return "driverMain";
	}

	// 로그인 후 출력될 기사 메인 화면 이동 및 기사 개인정보 가져오기
	@GetMapping("driverModify")
	public String driverModify(Model model) {
		log.info("driverModify()");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 로그인 시 정보 가져오기
		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		log.info("m_id: {}", m_id);

		drServ.getDriverInfo(m_id, model);

		return "driverModify";
	}

	// 운행 전 화면 이동
	@GetMapping("standBy")
	public String moveToStandBy(Model model) {
		log.info("moveToStandBy()");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		log.info("m_id: {}", m_id);

		String username = authentication.getName();
		log.info("username: {}", username);

		drServ.getPassengerInfo(m_id, username, model);

		return "standBy";
	}

	// 경로 확인 및 운행 리스트 띄우기 및 페이지 이동
	@GetMapping("routeCheck")
	public String moveToRoute(Model model) {
		log.info("moveToRoute()");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		log.info("m_id: {}", m_id);

		String username = authentication.getName();
		log.info("username: {}", username);

		drServ.getRouteList(m_id, username, model);

		return "routeCheck";
	}

	// 기사 프로필 이미지 가져오기
	@PostMapping("getDriverImage")
	@ResponseBody
	public String driverProfile(@RequestParam("M_ID") int M_ID, Model model) {
		log.info("driverProfile()");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		log.info("m_id: {}", m_id);

		String profileAjax = drServ.getDriverImage(M_ID, model);

		return profileAjax;
	}

	@GetMapping("driverJoin")
	public String driverJoin() {
		log.info("driverJoin()");

		return "driverJoin";
	}

	@PostMapping("driverJoinProc")
	public String driverJoinProc(@RequestPart("files") List<MultipartFile> files,
											HttpSession session,
											DriverDto driver,
											RedirectAttributes rttr) {
		log.info("driverJoinProc()");
		
		log.info("driver: {}", driver);
		String view = drServ.insertDriver(files, session, driver, rttr);

		return view;
	}
	
	@GetMapping("driverUpdate")
	public String driverUpdate(Model model) {
		log.info("driverUpdate()");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		log.info("m_id: {}", m_id);
		
		drServ.getDriverInfo(m_id, model);
		System.out.println(model);
		
		
		return "driverUpdate";
	}

	@PostMapping("driverUpdateProc")
	public String driverUpdateProc(DriverDto driver,RedirectAttributes rttr,HttpSession session) {
		log.info("driverUpdateProc()");
		String view = null;
		
		MemberDto member = new MemberDto();
		int mid = driver.getM_ID();
		String m_name = driver.getM_NAME();
		String m_phone = driver.getM_PHONE();
		member.setM_ID(mid);
		member.setM_NAME(m_name);
		member.setM_PHONE(m_phone);
		
		mServ.DriveMemberUpdate(member);
		view = drServ.driverUpdateProc(driver,rttr,session);
		
		return view;
	}
}
