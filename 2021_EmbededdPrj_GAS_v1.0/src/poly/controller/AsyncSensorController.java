package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.MongoDTO;
import poly.mongo.IMongoMapper;
import poly.util.AlertClass;

/* @Auth 최별규
 * @Version 1.1
 * 센서데이터와 몽고 비동기 처리를 위한 컨트롤러
 *  ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2021.09.11   |  최별규     | 몽고에서 센서 데이터 가져오기 초안 작성
 * | 2021.09.13   |  최별규     | 센서 데이터가 기준 값 이상이면 알림(모달, 카카오) 보내기
 * |              |             |
 * */

@Controller
public class AsyncSensorController {
	private Logger log = Logger.getLogger(this.getClass().getName());
	//--------------------------------------------------------------리소스선언부--------------------------------------------------------
	@Resource(name ="MongoMapper")
	private IMongoMapper mongoMapper;
	//-----------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------비동기로 몽고에서 값 가져옴-------------------------------------
	@SuppressWarnings({ "null", "unchecked" })
	@RequestMapping(value="/sensor/getAsyncSensorData", method = RequestMethod.GET)
	public @ResponseBody Object sensorDataAsycUpdate(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception{
		log.info("----------------------------------sensorDataAsycUpdate Start------------------------------");
		JSONObject sensorObj = new JSONObject(); // => 화면에 JSON 형식으로 뿌려주기 위한 객체
		@SuppressWarnings("resource")
		AlertClass alert = new AlertClass();
		String accessToken = (String) session.getAttribute("kakaoToken"); // 카톡 발송을 위한 세션 값 가져오기
		log.info("accessToken : " + accessToken);
		String message = "";
		//------------------------------------------------------------------------------------------------------------------------------
		int dangerGas = 200; // 임계값 설정하는 곳
		//----------------------------------------------몽고에서 데이터 가져오는 로직----------------------------------------------------
		List<MongoDTO> dailySensor = new ArrayList<MongoDTO>();
		// < 가장 최근의 센서 번호와 시간 가져오기
		dailySensor = mongoMapper.getDailySensorData();
		for(MongoDTO e : dailySensor) { // 데이터 확인
			log.info(e.getDate());
			log.info(e.getSENSOR_NUMBER());
		}
		//< 최근 정보에 해당하는 센서 데이터 가져오기 WHERE로 뽑힌 가스농도 가져오기 >
		List<MongoDTO> sensorDataMatch = new ArrayList<MongoDTO>();
		sensorDataMatch = mongoMapper.getSensorDataMatch(dailySensor);
		//--------------------------------------------------------List를 JSON으로 변환해주기----------------------------------------------
		int cnt = 1;
		for(MongoDTO e : sensorDataMatch) {
			sensorObj.put(e.getSENSOR_NUMBER(), e.getSENSOR_DATA());
			log.info(cnt + "번 센서번호 : " + e.getSENSOR_NUMBER());
			log.info(cnt + "번 가스농도 : " + e.getSENSOR_DATA());
			//-----------------------------------임계값 이상의 센서 농도가 들어온다면 kakaoTalk을 발송한다.--------------------------------
			if( Integer.parseInt( e.getSENSOR_DATA()) >= dangerGas ) {
				message = e.getSENSOR_NUMBER() + "번 센서에서 많은 양의 가스가 탐지되었습니다.";
				String resStr = alert.sendAlertMessageForKakao(accessToken, message, "/");
				log.info(resStr);
			}
			//-----------------------------------------------------------------------------------------------------------------------------
			cnt++;
		}
		log.info("----------------------------------sensorDataAsycUpdate End--------------------------------");
		return sensorObj;
	}
}
