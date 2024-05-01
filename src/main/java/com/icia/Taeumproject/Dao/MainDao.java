package com.icia.Taeumproject.Dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.Node;
import com.icia.Taeumproject.Dto.DispatchDto;

@Mapper
public interface MainDao {

   public void insert (Node node);
   


  public List<Node> selectList();

  public List<Node> endSelectList();
  
  public void updateRide( Long ride, Long id);



  public void updateDelivery(Node node)  ;



  public List<Node> selectRideList(int ride);



  public List<Node> selectLocaldate(String A_DATE);



  public List<DriverDto> selectDriverList(String DR_AREA);



  public List<Node> selectNodeArea(@Param("address")String address, @Param("A_DATE")String A_DATE);



  public void insertDispatch(DispatchDto dispatchDto);



  public List<DispatchDto> GetDriverList(Integer dr_ID);



  public List<DispatchDto> getDispatch(String date);
  
  
  
}
