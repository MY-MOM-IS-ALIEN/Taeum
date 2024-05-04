package com.icia.Taeumproject.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.Taeumproject.Dto.DispatchDto;
import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.MemberDto;
import com.icia.Taeumproject.Dto.Node;
import com.icia.Taeumproject.Dto.SecurityUserDTO;
import com.icia.Taeumproject.Service.DriverService;
import com.icia.Taeumproject.Service.MainService;
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

	@Autowired
	private MainService maServ;

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
		System.out.println("엉엉엉" + model);

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
	@GetMapping("getDriverImage")
	@ResponseBody
	public String driverProfile(@RequestParam("M_ID") int M_ID, Model model) {
		log.info("driverProfile()");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		log.info("m_id: {}", m_id);

		return drServ.getDriverImage(M_ID, model);

	}

	@GetMapping("driverJoin")
	public String driverJoin() {
		log.info("driverJoin()");

		return "driverJoin";
	}

	/*
	 * @PostMapping("driverJoinProc") public String
	 * driverJoinProc(@RequestPart("files") List<MultipartFile> files, HttpSession
	 * session, DriverDto driver, RedirectAttributes rttr) {
	 * log.info("driverJoinProc()");
	 * 
	 * log.info("driver: {}", driver); String view = drServ.insertDriver(files,
	 * session, driver, rttr);
	 * 
	 * return view; }
	 */

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
	public String driverUpdateProc(List<MultipartFile> files, DriverDto driver, RedirectAttributes rttr,
			HttpSession session) {
		log.info("driverUpdateProc()");
		String view = null;

		MemberDto member = new MemberDto();
		int mid = driver.getM_ID();
		String m_name = driver.getM_NAME();
		String m_phone = driver.getM_PHONE();
		member.setM_ID(mid);
		member.setM_NAME(m_name);
		member.setM_PHONE(m_phone);

		// 기사 정보 업데이트
		mServ.DriveMemberUpdate(member);

		// 프로필 이미지 업데이트
		drServ.updateDriverProfile(mid);

		// 로그인 시 저장된 이름 정보 추출
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		String name = ((SecurityUserDTO) principal).getM_NAME();

		// 그 외 업데이트 처리
		view = drServ.driverUpdateProc(files, driver, rttr, session);

		return view;
	}

	

	@GetMapping("/mainCenter")
	public String test(Model model) {
		
		// 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();
        
        // 날짜를 원하는 형식으로 포맷팅하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		int DR_ID = (m_id - 1);

		List<List<Node>> rideNodeList = new ArrayList<>();
		List<Node> innerList1 = new ArrayList<>();
		List<Node> innerList2 = new ArrayList<>();
		List<Node> innerList3 = new ArrayList<>();
		
		List<Node> nodeList = maServ.selectNodeList(DR_ID);

		for (Node node : nodeList) {
			if (node.getCycle() == 1) {
				innerList1.add(node);
			} else if (node.getCycle() == 2) {
				innerList2.add(node);
			} else {
				innerList3.add(node);
			}
		}
		rideNodeList.add(innerList1);
		rideNodeList.add(innerList2);
		rideNodeList.add(innerList3);
		System.out.println("rideNodeList ==  == = = = =- = = " + rideNodeList);
		model.addAttribute("rideNodeList", rideNodeList);

		System.out.println(innerList1);
		System.out.println(innerList2);
		System.out.println(innerList3);
		// model.addAttribute("innerList1", innerList1);
		model.addAttribute("nodeList", nodeList);

		return "mainCenter"; // 뷰 이름 반환
	}
	

	@PostMapping("/newNodeList")
	public List<List<Node>> newNodeList(@RequestParam("selectedDate") String dateString, Model model,RedirectAttributes rttr) {
	    // 사용자 정보 가져오기
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    Object principal = authentication.getPrincipal();
	    int m_id = ((SecurityUserDTO) principal).getM_ID();
	    int DR_ID = (m_id - 1);
	    String view = null;
	    // 선택된 날짜에 해당하는 노드 리스트 가져오기
	    List<Node> nodeList = maServ.selectNodeListToday(DR_ID, dateString);
	    
	    // 노드 리스트를 세 부분으로 분리하여 모델에 추가
	    List<Node> innerList1 = new ArrayList<>();
	    List<Node> innerList2 = new ArrayList<>();
	    List<Node> innerList3 = new ArrayList<>();
	    for (Node node : nodeList) {
	        if (node.getCycle() == 1) {
	            innerList1.add(node);
	        } else if (node.getCycle() == 2) {
	            innerList2.add(node);
	        } else {
	            innerList3.add(node);
	        }
	    }
	    
	    List<List<Node>> rideNodeList = new ArrayList<>();
	    rideNodeList.add(innerList1);
	    rideNodeList.add(innerList2);
	    rideNodeList.add(innerList3);
	    
	    // 모델에 데이터 추가
	    System.out.println(rideNodeList);
	    
	    return rideNodeList;
	}

	@GetMapping("driverAutoJoin")
	public String driverAutoJoin(RedirectAttributes rttr) {
		log.info("driverAutoJoin()");

		for (int i = 2; i <= 4; i++) {
			MemberDto member = new MemberDto();
			member.setUsername("alsdn547" + i + "@naver.com");
			member.setPassword("1111");
			member.setM_NAME("드라이버" + (i - 1));
			member.setM_PHONE("null");
			member.setRole("DRIVER");

			mServ.memberJoin(member, rttr);
			int m_id = i;
			mServ.updateRole(m_id);
		}

		for (int i = 1; i <= 3; i++) {
			DriverDto driver = new DriverDto();
			driver.setDR_CARNUM(15 + "더" + (9018 + i));
			driver.setDR_AREA("부평구");
			driver.setM_ID(i + 1);

			drServ.insertDriver(driver, rttr);
		}

		return "redirect:/adminMain";
	}

	@PostMapping("/acceptNode")
	public String acceptNode(@RequestBody List<Node> data) {
		log.info("acceptNode()");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Object principal = authentication.getPrincipal();
		int m_id = ((SecurityUserDTO) principal).getM_ID();
		int DR_ID = (m_id-1);
	    
	    System.out.println("여기지롱" + data);
	    drServ.updateConfirm(data,DR_ID);
	    return "mainCenter"; // 적절한 응답 처리
	}
	
	@PostMapping("/deniedNode")
	public String deniedNode(@RequestBody List<DispatchDto> dataToSend) {
	    log.info("deniedNode()");

	    // 사용자 정보 확인
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    Object principal = authentication.getPrincipal();
	    int m_id = ((SecurityUserDTO) principal).getM_ID();
	    int DR_ID = (m_id - 1);

	    drServ.updateCancle(dataToSend,DR_ID);

	    return "mainCenter"; // 적절한 응답 처리





	 

	}
}

