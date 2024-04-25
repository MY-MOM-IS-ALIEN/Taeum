package com.icia.Taeumproject.Service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.Taeumproject.Dao.DriverDao;
import com.icia.Taeumproject.Dto.ApplyDto;
import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.DriverFileDto;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DriverService {
	@Autowired
	private PlatformTransactionManager manager;
	@Autowired
	private TransactionDefinition definition;
	@Autowired
	private DriverDao drDao;

	public void getDriverInfo(int m_id, Model model) {
		log.info("getDriverInfo()");

		DriverDto drDto = drDao.getDriverInfo(m_id);
		model.addAttribute("driver", drDto);

		// 오늘 운행 건수 가져오기
		int todayTraffic = drDao.getTodayTraffic(m_id);
		model.addAttribute("todayTraffic", todayTraffic);

		// 이번 달 운행 건수 가져오기
		int monthTraffic = drDao.getMonthTraffic(m_id);
		model.addAttribute("monthTraffic", monthTraffic);

		// 총 운행 건수 가져오기
		int totalTraffic = drDao.getTotalTraffic(m_id);
		model.addAttribute("totalTraffic", totalTraffic);

	}

	public void getPassengerInfo(int m_id, String username, Model model) {
		log.info("getPassengerInfo()");

		DriverDto mdto = drDao.getInfo(m_id);
		model.addAttribute("driverName", mdto);
	
		List<ApplyDto> applyList = drDao.getPassengerList(m_id);
		model.addAttribute("applyList", applyList);
		log.info("applyList: {}", applyList);
		
	}

	public String getRouteList(int m_id, String username, Model model) {
		log.info("getRouteList()");

		DriverDto mdto = drDao.getInfo(m_id);
		model.addAttribute("driverName", mdto);

		List<ApplyDto> routeList = drDao.getRouteList(m_id);
		model.addAttribute("routeList", routeList);

		return "routeCheck";
	}

//	public String getDriverImage(String email) {
//		String driverImage = drDao.getDriverImage(email);
//
//		return driverImage;
//	}
	
	public String insertDriver(List<MultipartFile> files, HttpSession session ,DriverDto driver, RedirectAttributes rttr) {
		log.info("insertDriver()");
		
		TransactionStatus status = manager.getTransaction(definition);
		String view = null;
		String msg = null;
		
		
		try {
			//정보글 저장
			System.out.println(driver);
			drDao.insertDriver(driver);
			
			//파일 저장
			if(!files.get(0).isEmpty()) {//업로드 파일이 있다면
				fileUpload(files, session, driver.getM_ID()); // 여기는 컬럼을 어떻게할지 정해야함
			}
			
			//commit 수행
			manager.commit(status);
			
			view = "redirect:driverMain";
			msg = "저장 성공";
		} catch (Exception e) {
			e.printStackTrace();
			//rollback 수행
			manager.rollback(status);
			
			view = "redirect:driverJoin";
			msg = "저장 실패";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return view;
	}
	
	private void fileUpload(List<MultipartFile> files, HttpSession session, int DR_M_ID) throws Exception {
//파일 저장 실패 시 데이터베이스 롤백작업이 이루어지도록 예외를 throws 할 것.
		log.info("fileUpload()");

//파일 저장 위치 처리(session에서 저장 경로를 구함)
		String realPath = session.getServletContext().getRealPath("/");
		log.info("realPath : {}", realPath);

		realPath += "upload/";// 파일 업로드 폴더

		File folder = new File(realPath);
		if (folder.isDirectory() == false) {
			folder.mkdir();// 폴더 생성 메소드
		}

		for (MultipartFile mf : files) {
			//파일명 추출
			String oriname = mf.getOriginalFilename();

			DriverFileDto dfd = new DriverFileDto();
			dfd.setDf_oriname(oriname);
			dfd.setDR_M_ID(DR_M_ID);
			String sysname = System.currentTimeMillis() + oriname.substring(oriname.lastIndexOf("."));
			//확장자 : 파일을 구분하기 위한 식별 체계. (예. xxxx.jpg)
			dfd.setDf_sysname(sysname);

			//파일 저장
			File file = new File(realPath + sysname);
			mf.transferTo(file);

			//파일 정보 저장
			drDao.insertFile(dfd);
		}
	}

}
