package com.icia.Taeumproject.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.icia.Taeumproject.Dao.MainDao;
import com.icia.Taeumproject.Dto.ApplyDto;
import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.Node;
import com.icia.Taeumproject.util.KakaoApiUtil.Point;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MainService {
    
  @Autowired
  private MainDao mDao;
  
  public void updateDelivery(int ride, Integer nodeId, Integer cycle){
    mDao.updateDelivery(ride, nodeId, cycle);
  }
  
  public void insertServ(int m_id,String fromAddress,Point startPoint,int sOrE,RedirectAttributes rttr, Timestamp A_DATE, LocalDateTime A_DateTime) {
      log.info("insertServ()");
      Node node = new Node();
      
      
      
      node.setM_id(m_id);
      node.setX(startPoint.getX());
      node.setY(startPoint.getY());
      node.setAddress(fromAddress);
      node.setKind(sOrE);
      node.setA_DATE(A_DateTime);
      node.setA_LOCALDATE(A_DATE);
      
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


   
  }
  


