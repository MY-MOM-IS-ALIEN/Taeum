package com.icia.Taeumproject.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icia.Taeumproject.Dto.DriverDto;
import com.icia.Taeumproject.Dto.Node;
import com.icia.Taeumproject.Dto.NodeCost;
import com.icia.Taeumproject.Dto.TourActivity;
import com.icia.Taeumproject.Dto.dispatchDto;
import com.icia.Taeumproject.Service.ApplyService;
import com.icia.Taeumproject.Service.MainService;
import com.icia.Taeumproject.Service.MemberService;
import com.icia.Taeumproject.Service.NodeCostService;
import com.icia.Taeumproject.Service.NodeService;
import com.icia.Taeumproject.parameter.NodeCostParam;
import com.icia.Taeumproject.util.JsonResult;
import com.icia.Taeumproject.util.KakaoApiUtil;
import com.icia.Taeumproject.util.KakaoApiUtil.Point;
import com.icia.Taeumproject.util.kakao.KakaoDirections;
import com.icia.Taeumproject.util.kakao.KakaoDirections.Route;
import com.icia.Taeumproject.util.kakao.KakaoDirections.Route.Section;
import com.icia.Taeumproject.util.kakao.KakaoDirections.Route.Section.Road;
import com.icia.Taeumproject.util.kakao.KakaoDirections.Route.Summary;
import com.icia.Taeumproject.util.kakao.KakaoDirections.Route.Summary.Fare;
import com.icia.Taeumproject.vrp.VrpResult;
import com.icia.Taeumproject.vrp.VrpService;
import com.icia.Taeumproject.vrp.VrpVehicleRoute;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AdminController {
  @Autowired
  public MemberService mServ;
  @Autowired
  public ApplyService aServ;

  @Autowired
  private MainService maServ;

  @Autowired
  private NodeService nodeService;
  @Autowired
  private NodeCostService nodeCostService;

  @GetMapping("adminMain")
  public String adminPage(Model model) {
    log.info("adminPage()");
    // 현재 날짜를 가져오기
    LocalDate currentDate = LocalDate.now();
    // 날짜를 yyyy-MM-dd 형식의 문자열로 변환
    String currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    // 현재 날짜를 사용하여 데이터 조회
    List<Node> nodeList = maServ.selectLocaldate(currentDateStr);
    log.info(currentDateStr);
    model.addAttribute("nodeList", nodeList);
    return "adminMain";

  }

  @GetMapping("adminDriverList")
  public String adminDriverList() {
    log.info("adminDriverList()");


  return "adminDriverList";
}
	  
@PostMapping("GetDriverList")
@ResponseBody
public List<dispatchDto> GetDriverList(int DR_ID, Model model) {
  log.info("GetDriverList");
   
    System.out.println("DR_IDDR_IDDR_IDDR_IDDR_IDDR_IDDR_IDDR_IDDR_IDDR_IDDR_ID = " + DR_ID) ;
  List<dispatchDto> dispatchDtoList = maServ.GetDriverList(DR_ID);
  model.addAttribute("GetDriverList",dispatchDtoList);
  return dispatchDtoList;
}
	 
	  @PostMapping("updateDelivery")
	  public String updateDelivery(String ride , String node_id, Integer cycle, String dateTime) {
	    log.info("updateDeliveryPROC");
	    if (ride == null || node_id == null) {
	      // ride 또는 m_id가 null인 경우에 대한 처리
	      System.out.println("ride = " + ride);
	      System.out.println("m_id/null = " + node_id);
	  } else {
	      System.out.println("ride = " + ride);
	      System.out.println("m_id = " + node_id);
	      String[] numbersAsString = node_id.split(", ");
	      
	      for (String num : numbersAsString) {
	          Integer nodeId = Integer.parseInt(num);
	          Integer riding = Integer.parseInt(ride);
	          
	          maServ.updateDelivery(riding, nodeId, cycle);
	      }
	        Integer ridding = Integer.parseInt(ride);
	        int status = 0;
	          maServ.isnertConfirm(ridding, dateTime,status);
	  }
 
	    return "adminDriverList";
	  }

  @GetMapping("/mainCenter")
  public String test(Model model) {
    int rideOne = 1;
    List<List<Node>> rideNodeList = new ArrayList<>();
    List<Node> innerList1 = new ArrayList<>();
    List<Node> innerList2 = new ArrayList<>();
    List<Node> innerList3 = new ArrayList<>();

    List<Node> nodeList = maServ.selectNodeList(rideOne);

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

  @GetMapping("adminNodeSelection")
  public String nodeSelection(@RequestParam("drID") String drID, @RequestParam("selectedLocal") String address,
      @RequestParam("selectDate") String selectDate, Model model) {
    log.info("nodeSelection()");

    List<Node> nodeList = maServ.selectNodeArea(address, selectDate);

    model.addAttribute("drID", drID);
    model.addAttribute("dr_AREA", address);
    // 날짜 모델에 추가되어야함
    model.addAttribute("nodeList", nodeList);

    return "adminNodeSelection";
  }

  @PostMapping("/vrp")
  @ResponseBody
  public JsonResult postVrp(@RequestBody List<Node> nodeList, Model model) throws IOException, InterruptedException {
    System.out.println("Vrp()");
    System.out.println(nodeList.size());

    VrpService vrpService = new VrpService();

    Node firstNode = nodeList.get(0);
    String firstNodeId = String.valueOf(firstNode.getNode_id());

    // 차량 등록
    System.out.println("Vrp 차량등록");
    vrpService.addVehicle("차량01", firstNodeId);
    Map<String, Node> nodeMap = new HashMap<>();
    Map<String, Map<String, NodeCost>> nodeCostMap = new HashMap<>();

    HashMap<Integer, TourActivity> tourMap = new HashMap<>();

    for (int i = 0; i < nodeList.size(); i++) {
      Node node = nodeList.get(i);
      // System.out.println("여기지롱 =" + node);
      String nodeId = String.valueOf(node.getNode_id());
      if (!tourMap.containsKey(node.getM_id())) {

        TourActivity tourActivity = new TourActivity();
        tourActivity.setM_id(node.getM_id());
        tourMap.put(node.getM_id(), tourActivity);

      }

      TourActivity tourActivity = tourMap.get(node.getM_id());

      if (nodeList.get(i).getKind() == 1) {
        tourActivity.setStartNode_id(node.getNode_id());

      } else {
        tourActivity.setEndNode_id(node.getNode_id());
      }

      nodeMap.put(nodeId, node);
      // System.out.println("여기 질엘 = " + nodeMap);

    }
    for (Integer m_id : tourMap.keySet()) {
      System.out.println("반복 해라 =" + m_id);
      TourActivity tourActivity = tourMap.get(m_id);

      // 조건을 모두 통과한 경우, 정상적으로 노드를 처리합니다.
      vrpService.addShipement(String.valueOf(tourActivity.getM_id()), String.valueOf(tourActivity.getStartNode_id()),
          String.valueOf(tourActivity.getEndNode_id()));
      System.out.println("tourActivity.getM_id() = " + tourActivity.getM_id());
      System.out.println("tourActivity.getstartNode_id() = " + tourActivity.getStartNode_id());
      System.out.println("tourActivity.getendNode_id() = " + tourActivity.getEndNode_id());
    }

    for (int i = 0; i < nodeList.size(); i++) {
      Node startNode = nodeList.get(i);
      for (int j = 0; j < nodeList.size(); j++) {
        Node endNode = nodeList.get(j);
        NodeCost nodeCost = getNodeCost(startNode, endNode);
        if (i == j) {
          continue;
        }
        if (nodeCost == null) {
          nodeCost = new NodeCost();
          nodeCost.setDistanceMeter(0L);
          nodeCost.setDurationSecond(0L);
        }
        Long distanceMeter = nodeCost.getDistanceMeter();
        Long durationSecond = nodeCost.getDurationSecond();
        String startNodeId = String.valueOf(startNode.getNode_id());
        String endNodeId = String.valueOf(endNode.getNode_id());
        // 비용 등록
        vrpService.addCost(startNodeId, endNodeId, durationSecond, distanceMeter);
        if (!nodeCostMap.containsKey(startNodeId)) {
          nodeCostMap.put(startNodeId, new HashMap<>());
        }
        nodeCostMap.get(startNodeId).put(endNodeId, nodeCost);
      }
    }
    List<Node> vrpNodeList = new ArrayList<>();
    VrpResult vrpResult = vrpService.getVrpResult();

    for (VrpVehicleRoute vrpVehicleRoute : vrpResult.getVrpVehicleRouteList()) {
      System.out.println(vrpVehicleRoute);
      // 모든 약을 시작점에서 픽업 한 경우만 정상 동작 하는 코드.
      if ("deliverShipment".equals(vrpVehicleRoute.getActivityName())
          || "pickupShipment".equals(vrpVehicleRoute.getActivityName())) {
        String locationId = vrpVehicleRoute.getLocationId();
        vrpNodeList.add(nodeMap.get(locationId));
      }
      System.out.println("vrpVehicleRoute = " + vrpVehicleRoute);

      // 수정된 코드
      // 시작 약국에서 픽업 몇개하고 배송 후 다시 픽업해도 되는 코드
//	      String locationId = vrpVehicleRoute.getLocationId();
//	      if(prevLocationId == null) {
//	        prevLocationId = locationId;
//	      }else if(locationId.equals(prevLocationId)) {
//	        continue;
//	      }
//	      
//	      prevLocationId = locationId;
//	      vrpNodeList.add(nodeMap.get(locationId));
    }

    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();

    for (int i = 1; i < vrpNodeList.size(); i++) {
      Node prev = vrpNodeList.get(i - 1);
      Node next = vrpNodeList.get(i);

      NodeCost nodeCost = nodeCostMap.get(String.valueOf(prev.getNode_id())).get(String.valueOf(next.getNode_id()));
      if (nodeCost != null) {
        String pathJson = nodeCost.getPathJson();
        totalDistance += nodeCost.getDistanceMeter();
        totalDuration += nodeCost.getDurationSecond();
        if (pathJson != null) {
          totalPathPointList.addAll(new ObjectMapper().readValue(pathJson, new TypeReference<List<Point>>() {
          }));
        }
      }

    }

    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance);// 전체이동거리
    jsonResult.addData("totalDuration", totalDuration);// 전체이동시간
    jsonResult.addData("totalPathPointList", totalPathPointList);// 전체이동경로
    jsonResult.addData("nodeList", vrpNodeList);// 방문지목록

    // TestResult testResult = new TestResult();
    // testResult.setTotalDistance(totalDistance);
    // testResult.setTotalDuration(totalDuration);
    // testResult.setTotalPathPointList(totalPathPointList);
    // testResult.setVrpNodeList(vrpNodeList);
//	    System.out.println("testResult VRPVRPVRP = " + testResult);
//	    System.out.println("vrpNodeList = " + vrpNodeList);

    // Model 객체에 TestResult 추가
//	    model.addAttribute("testResult", testResult);
    return jsonResult;

  }

  private NodeCost getNodeCost(Node prev, Node next) throws IOException, InterruptedException {
    NodeCostParam nodeCostParam = new NodeCostParam();
    nodeCostParam.setStartNodeId(Long.valueOf(prev.getNode_id()));
    nodeCostParam.setEndNodeId(Long.valueOf(next.getNode_id()));
    NodeCost nodeCost = nodeCostService.getOneByParam(nodeCostParam);
    if (nodeCost == null) {
      KakaoDirections kakaoDirections = KakaoApiUtil.getKakaoDirections(new Point(prev.getX(), prev.getY()),
          new Point(next.getX(), next.getY()));
      List<Route> routes = kakaoDirections.getRoutes();
      Route route = routes.get(0);
      List<Point> pathPointList = new ArrayList<Point>();
      List<Section> sections = route.getSections();
      if (sections == null) {
        // {"trans_id":"018e3d7f7526771d9332cb717909be8f","routes":[{"result_code":104,"result_msg":"출발지와
        // 도착지가 5 m 이내로 설정된 경우 경로를 탐색할 수 없음"}]}
        pathPointList.add(new Point(prev.getX(), prev.getY()));
        pathPointList.add(new Point(next.getX(), next.getY()));
        nodeCost = new NodeCost();
        nodeCost.setStartNodeId(Long.valueOf(prev.getNode_id()));// 시작노드id
        nodeCost.setEndNodeId(Long.valueOf(next.getNode_id()));// 종료노드id
        nodeCost.setDistanceMeter(0l);// 이동거리(미터)
        nodeCost.setDurationSecond(0l);// 이동시간(초)
        nodeCost.setTollFare(0);// 통행 요금(톨게이트)
        nodeCost.setTaxiFare(0);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
        nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
        nodeCost.setRegDt(new Date());// 등록일시
        nodeCost.setModDt(new Date());// 수정일시
        nodeCostService.add(nodeCost);
        return null;
      }
      List<Road> roads = sections.get(0).getRoads();
      for (Road road : roads) {
        List<Double> vertexes = road.getVertexes();
        for (int q = 0; q < vertexes.size(); q++) {
          pathPointList.add(new Point(vertexes.get(q), vertexes.get(++q)));
        }
      }
      Summary summary = route.getSummary();
      Integer distance = summary.getDistance();
      Integer duration = summary.getDuration();
      Fare fare = summary.getFare();
      Integer taxi = fare.getTaxi();
      Integer toll = fare.getToll();
      nodeCost = new NodeCost();
      nodeCost.setStartNodeId(Long.valueOf(prev.getNode_id()));// 시작노드id
      nodeCost.setEndNodeId(Long.valueOf(next.getNode_id()));// 종료노드id
      nodeCost.setDistanceMeter(distance.longValue());// 이동거리(미터)
      nodeCost.setDurationSecond(duration.longValue());// 이동시간(초)
      nodeCost.setTollFare(toll);// 통행 요금(톨게이트)
      nodeCost.setTaxiFare(taxi);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
      nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
      nodeCost.setRegDt(new Date());// 등록일시
      nodeCost.setModDt(new Date());// 수정일시
      nodeCostService.add(nodeCost);
    }
    return nodeCost;
  }

//	  @GetMapping("main")
//	  public String main(Model model) {
//	 // 카카오api로 요청해서 약국정보 가져오기
//	    List<Node> nodeList = maServ.selectList();
//	    System.out.println(nodeList);
//	    model.addAttribute("nodeList", nodeList);
//	    return "main";
//	  }

  @GetMapping("/ride")
  @ResponseBody
  public String ride(@RequestParam Long ride, @RequestParam Long id) {
    log.info("ride()");
    List<Node> nodeList = maServ.selectList();

    for (int i = 0; i < nodeList.size(); i++) {
      Long nId = Long.valueOf(nodeList.get(i).getNode_id());
      System.out.println("node_id = " + id);
      System.out.println("nId = " + nId);

      if (nId.equals(id)) {
        maServ.updateRide(ride, id);
        break;
      }
    }
    return "redirect:/main";
  }

  @GetMapping("/poi")
  @ResponseBody
  public JsonResult getPoi(@RequestParam double x, @RequestParam double y, Model model)
      throws IOException, InterruptedException {
    Point center = new Point(x, y);

    // 카카오api로 요청해서 약국정보 가져오기
    List<Node> nodeList = maServ.selectList();
    System.out.println(nodeList);

    // copiedNodeList를 사용하여 반복문 실행
    List<Node> copiedNodeList = new ArrayList<>(nodeList);
    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    for (int i = 1; i < copiedNodeList.size(); i++) {
      Node prev = copiedNodeList.get(i - 1);
      Node next = copiedNodeList.get(i);

      NodeCostParam nodeCostParam = new NodeCostParam();
      nodeCostParam.setStartNodeId(Long.valueOf(prev.getNode_id()));
      nodeCostParam.setEndNodeId(Long.valueOf(next.getNode_id()));
      System.out.println("여기까진 됌");
      NodeCost nodeCost = nodeCostService.getOneByParam(nodeCostParam);

      if (nodeCost == null) {
        KakaoDirections kakaoDirections = KakaoApiUtil.getKakaoDirections(new Point(prev.getX(), prev.getY()),
            new Point(next.getX(), next.getY()));
        List<Route> routes = kakaoDirections.getRoutes();
        Route route = routes.get(0);
        List<Point> pathPointList = new ArrayList<Point>();
        List<Section> sections = route.getSections();

        if (sections == null) {
          // {"trans_id":"018e3d7f7526771d9332cb717909be8f","routes":[{"result_code":104,"result_msg":"출발지와
          // 도착지가 5 m 이내로 설정된 경우 경로를 탐색할 수 없음"}]}
          continue;
        }
        List<Road> roads = sections.get(0).getRoads();
        for (Road road : roads) {
          List<Double> vertexes = road.getVertexes();
          for (int q = 0; q < vertexes.size(); q++) {
            pathPointList.add(new Point(vertexes.get(q), vertexes.get(++q)));
          }
        }
        Summary summary = route.getSummary();
        Integer distance = summary.getDistance();
        Integer duration = summary.getDuration();
        Fare fare = summary.getFare();
        Integer taxi = fare.getTaxi();
        Integer toll = fare.getToll();

        nodeCost = new NodeCost();
        nodeCost.setStartNodeId(Long.valueOf(prev.getNode_id()));// 시작노드id
        nodeCost.setEndNodeId(Long.valueOf(next.getNode_id()));// 종료노드id
        nodeCost.setDistanceMeter(distance.longValue());// 이동거리(미터)
        nodeCost.setDurationSecond(duration.longValue());// 이동시간(초)
        nodeCost.setTollFare(toll);// 통행 요금(톨게이트)
        nodeCost.setTaxiFare(taxi);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
        nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
        nodeCost.setRegDt(new Date());// 등록일시
        nodeCost.setModDt(new Date());// 수정일시
        nodeCostService.add(nodeCost);
      }

      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();
      totalPathPointList.addAll(new ObjectMapper().readValue(nodeCost.getPathJson(), new TypeReference<List<Point>>() {
      }));
    }

    JsonResult jsonResult = new JsonResult();
//	    jsonResult.addData("totalDistance", totalDistance);// 전체이동거리
//	    jsonResult.addData("totalDuration", totalDuration);// 전체이동시간
//	    jsonResult.addData("totalPathPointList", totalPathPointList);// 전체이동경로
    jsonResult.addData("nodeList", nodeList);// 방문지목록

//	    System.out.println("totalPathPointList = " + totalPathPointList);
    System.out.println("nodeList 마지막 " + nodeList);
    return jsonResult;
  }

  @PostMapping("selectLocaldate")
  @ResponseBody
  public List<Node> selectLocaldate(String date) {
    log.info("selectLocaldate()");
    List<Node> selectLocaldate = maServ.selectLocaldate(date);
    return selectLocaldate;
  }

  @PostMapping("selectedLocal")
  @ResponseBody
  public List<DriverDto> selectedLocal(String selectedValue, Model model) {
    List<DriverDto> driverDtoList = maServ.selectDriverList(selectedValue);
    System.out.println("local = " + selectedValue);
    model.addAttribute("driverDtoList", driverDtoList);
    return driverDtoList;
  }

  @GetMapping("adminBoard")
  public String adminBoard(Model model) {
    log.info("adminBoard()");
    
    // 현재 날짜를 가져오기
    LocalDate currentDate = LocalDate.now();
    // 날짜를 yyyy-MM-dd 형식의 문자열로 변환
    String currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    
 
    
    // 이전 달의 날짜를 가져오기
    LocalDate previousMonthDate = currentDate.minusMonths(1);
    String previousMonth = previousMonthDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    
    LocalDate twoMonthsAgoDate = currentDate.minusMonths(2);
    String twoMonthsAgo = twoMonthsAgoDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    
    // 현재 날짜를 사용하여 데이터 조회
    List<Node> nodeList = maServ.selectLocaldate(currentDateStr);
    // 이전 달의 데이터 조회
    List<Node> previousMonthNodeList = maServ.selectLocaldate(previousMonth);
    // 그 이전 달의 데이터 조회
    List<Node> twoMonthsAgoNodeList = maServ.selectLocaldate(twoMonthsAgo);
    
    int nodeListsize = nodeList.size();
    int previousMonthNodeListsize = previousMonthNodeList.size();
    int twoMonthsAgoNodeListsize = twoMonthsAgoNodeList.size();
    int TotalNodeCount = nodeListsize + previousMonthNodeListsize + twoMonthsAgoNodeListsize;
    
    model.addAttribute("currentDateStr", currentDateStr); // 현재 날짜
    model.addAttribute("nodeListsize", nodeListsize); //현재 날짜 노드
    model.addAttribute("previousMonth", previousMonth); // 현재 날짜
    model.addAttribute("previousMonthNodeListsize", previousMonthNodeListsize); //현재 날짜 노드
    model.addAttribute("twoMonthsAgo", twoMonthsAgo); // 현재 날짜
    model.addAttribute("twoMonthsAgoNodeListsize", twoMonthsAgoNodeListsize); //현재 날짜 노드
    model.addAttribute("TotalNodeCount", TotalNodeCount);
    // -------------------------------------------------------------------------------------------------
    int dispathNowListSize = getRideNodeListSize(nodeList);
    int dispathPrevListSize = getRideNodeListSize(previousMonthNodeList);
    int dispathTwoListSize = getRideNodeListSize(twoMonthsAgoNodeList);
    int TotalBechaCount = dispathNowListSize + dispathPrevListSize + dispathTwoListSize;
    

    model.addAttribute("dispathNowListsize", dispathNowListSize);
    model.addAttribute("dispathPrevListsize", dispathPrevListSize);
    model.addAttribute("dispathTwoListsize", dispathTwoListSize);
    model.addAttribute("TotalBechaCount", TotalBechaCount);
    // ---------------------------------------------------------------------------------------------------
    List<dispatchDto> dispatchNowList = maServ.getDispatch(currentDateStr);
    List<dispatchDto> dispatchPrevList = maServ.getDispatch(previousMonth);
    List<dispatchDto> dispatchTwoList = maServ.getDispatch(twoMonthsAgo);
    
    
    int dispatchNowListSize1 = getDispatchListSize(dispatchNowList);
    int dispatchPrevListSize1 = getDispatchListSize(dispatchPrevList);
    int dispatchTwoListSize1 = getDispatchListSize(dispatchTwoList);
    int totalDispatchListSize1 =  dispatchNowListSize1 + dispatchPrevListSize1 + dispatchTwoListSize1;
    
    System.out.println(dispatchNowListSize1);
    model.addAttribute("totalDispatchListSize1", totalDispatchListSize1);
    
    int dispatchNowListSize2 = getDispatchListSize1(dispatchNowList);
    int dispatchPrevListSize2 = getDispatchListSize1(dispatchPrevList);
    int dispatchTwoListSize2 = getDispatchListSize1(dispatchTwoList);
    int totalDispatchListSize2 =  dispatchNowListSize2 + dispatchPrevListSize2 + dispatchTwoListSize2;
    model.addAttribute("totalDispatchListSize2", totalDispatchListSize2);
    
    model.addAttribute("dispatchNowListSize1", dispatchNowListSize1);
    model.addAttribute("dispatchPrevListSize1", dispatchPrevListSize1);
    model.addAttribute("dispatchTwoListSize1", dispatchTwoListSize1);
    model.addAttribute("dispatchNowListSize2", dispatchNowListSize2);
    model.addAttribute("dispatchPrevListSize2", dispatchPrevListSize2);
    model.addAttribute("dispatchTwoListSize2", dispatchTwoListSize2);
    
    
    return "adminBoard";
    }
  // 배차 현황 리스트 사이즈 가져오기
  private int getRideNodeListSize(List<Node> nodeList) {
    int count = 0;
    for (Node node : nodeList) {
        if (node.getRide() != 0) {
            count++;
        }
    }
    return count;
}
  
  // 배차 현황 리스트 사이즈 가져오기
  private int getDispatchListSize(List<dispatchDto> dispatchList) {
    int count = 0;
    for (dispatchDto dispatch : dispatchList) {
        if (dispatch.getD_STATUS().equals("1")) {
            count++;
        }
    }
    return count;
}
  
  // 배차 현황 리스트 사이즈 가져오기
  private int getDispatchListSize1(List<dispatchDto> dispatchList) {
    int count = 0;
    for (dispatchDto dispatch : dispatchList) {
        if (dispatch.getD_STATUS().equals("2")) {
            count++;
        }
    }
    return count;
}
}
