package com.icia.Taeumproject.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.Node;

@Mapper
public interface MainDao {

   public void insert (Node node);
   


  public List<Node> selectList();

  public List<Node> endSelectList();
  
  public void updateRide( Long ride, Long id);



  public void updateDelivery(@Param("ride") int ride, @Param("id") Integer nodeId, @Param("cycle") Integer cycle);



  public List<Node> selectRideList(int ride);



  public List<Node> selectLocaldate(String A_DATE);



  public List<DriverDto> selectDriverList(String DR_AREA);
}
