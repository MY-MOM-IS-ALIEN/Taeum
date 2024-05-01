package com.icia.Taeumproject.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.Taeumproject.Dao.MainDao;
import com.icia.Taeumproject.Dto.ApplyDto;
import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.DrivermanagementDto;
import com.icia.Taeumproject.Dto.Node;
import com.icia.Taeumproject.Dto.DispatchDto;
import com.icia.Taeumproject.util.KakaoApiUtil.Point;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MainService {
    
  @Autowired
  private MainDao mDao;
  

  public void updateDelivery(Integer ride, Integer nodeId, Integer cycle, String status, long D_ID){
    
    
    Node node = new Node();
    node.setDR_ID(ride);

    node.setNode_id(nodeId);
    node.setCycle(cycle);
    node.setStatus(status);
    node.setD_ID(D_ID);
    
    mDao.updateDelivery(node);
  }

  
  public void insertServ(int m_id,String fromAddress,Point startPoint,int sOrE,RedirectAttributes rttr, LocalDateTime A_DATE) {
      log.info("insertServ()");
      Node node = new Node();
      
            
      
              String aDateAsString = A_DATE.toString();
              
      node.setM_ID(m_id);
      node.setX(startPoint.getX());
      node.setY(startPoint.getY());
      node.setAddress(fromAddress);
      node.setKind(sOrE);
      node.setA_DATE(aDateAsString);
      
      
      mDao.insert(node);
      
  }



  public List<Node> selectList() {
    log.info("selectList()");
    List<Node> entities = mDao.selectList();
    
    return entities;
}



  public void updateRide(Long ride, Long id) {
    log.info("updateRide()");
    System.out.println(ride);
    mDao.updateRide(ride, id);
    
    
  }



  public List<Node> endSelectList() {
    log.info("endSelectList()");
    List<Node> endList = mDao.endSelectList();
    System.out.println(endList);
    
    return endList;
    }

  public List<Node> selectNodeList(int rideOne) {
    
   List<Node> rideNodeList = mDao.selectRideList(rideOne);
    
    return rideNodeList;
    
  }

  public List<Node> selectLocaldate(String date) {
   List<Node> selectLocaldate = mDao.selectLocaldate(date);
    return selectLocaldate;
  }

  
  public List<DriverDto> selectDriverList(String local) {
    List<DriverDto> selectDriverList = mDao.selectDriverList(local);
    return selectDriverList;
  }

  public List<Node> selectNodeArea(String address, String currentDate) {
     List<Node> selectNodeArea = mDao.selectNodeArea(address, currentDate);
    return selectNodeArea;
  }


  public List<DispatchDto> GetDriverList(Integer dr_ID) {
      List<DispatchDto> dispatchDtoList = mDao.GetDriverList(dr_ID);
    return dispatchDtoList;
  }

  public List<DispatchDto> getDispatch(String date) {
    List<DispatchDto> getDispatch = mDao.getDispatch(date);
    return getDispatch;
  }

  public List<DrivermanagementDto> DRMTList(Integer dR_ID) {
    List<DrivermanagementDto> DRMTList = mDao.DRMTList(dR_ID);
    return DRMTList;
  }

  public void isnertConfirm(DispatchDto dispatchDto) {
    mDao.insertDispatch(dispatchDto);
    
  }



 


   
  }
  


