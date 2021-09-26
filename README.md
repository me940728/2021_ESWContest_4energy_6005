# 2021_EmbededdPrj_Demo
2021년 임베디드 소프트웨어 경진대회
(The World Embeded Software Contest 2021)
1. 프로젝트 기본 정보
부문 : 제 19회 임베디드 SW경진대회[4차 산업 기반 에너지]
주최 : 산업통상자원부
주관 : 임베디드 소프트웨어, 시스템 산업협회
작품명 : 아두이노를 이용한 자동 환기 시스템 및 모니터링 서비스
팀명/팀원 : GASGASGAS / 최별규, 홍석민, 양원석, 오주현, 이강희
설명 : 
- 가스 감지 센서를 통한 실시간 가스 데이터 수집
- 일정 임계값 초과 시 작동하는 자동 환기, 알림 시스템
- 실시간 데이터 및 누적 데이터를 이용한 가스 모니터링 서비스
- 클라우드 대쉬보드 URL : www.detectiongas.com
- 프로젝트 시연 동영상 URL : 유튭

2. 개발에 사용된 기술 목록
가) SW Tool
- IDE : Eclipse, STS, VS Code, Arduino Sketch
- Database Tool : Studio 3T, MySQL Workbench 8.0 CE
- API : Postman
- Server : PuTTY, WinSCP
- 형상관리 : Github, Git bash
- 소통 : Slack
- 브라우저 : Chrome

나) 하드웨어
- Arduino Uno Board
- MQ2, MQ4 Gas sensor(실험에는 CO2 센서사용)
- 환풍기(EKS-200SAP)
- Electric Motor(SG90)
- Buzzer(Digital Buzzer V2)
- breadboard 

다) 소프트웨어
- Spring framework /  v4.3
- Maven / v4.0
- Mosquito / v1.6.9
- java / jdk_1.8.0_291
- mongoDB / 4.4.7
- MariaDB / AWS RDS
- node.js / v14.17.6 (반드시 14이상)
- Tomcat / v8.5
- AWS EC2 Ubuntu / 20.LTS

라) 주요 라이브러리 정보
- MQTT lib / org.eclipse.paho v1.2.2
- mongo-java-driver / 3.2.2
- spring-data-mongodb / 1.9.1.RELEASE
- Kakao REST API / OAUTH2, SendTalk(개인)

3. 주요 디렉터리(파일) 정보
- 2021_EmbededdPrj_Demo : spring 프로젝트 코드 디렉터리
- ArduinoCode : 아두이노 및 하드웨어 컨트롤을 위한 코드 디렉터리 
- HardwareInfo : 개발에 사용된 하드웨어 정보를 모아둔 디렉터리
- NodeServer : 센서 데이터 종합, 저장 처리를 위한 노드 서버 코드 디렉터리
---------------------------------------------------------------------------------------
2021_EmbededdPrj_Demo > WebContent > WEB-INF > view : JSP 파일 모아둔 디렉터리
2021_EmbededdPrj_Demo > WebContent > resources > js > customJs : 센서 정보를 가져와 처리하는 부분
2021_EmbededdPrj_Demo > src > poly > controller : 사용자 요청을 서비스와 매핑하는 컨트롤러 디렉터리 
2021_EmbededdPrj_Demo > src > poly > dto : 데이터 저장을 위한 저장 객체 디렉터리
2021_EmbededdPrj_Demo > src > poly > mongo : 몽고와 관련된 코드를 모아놓은 디렉터리
2021_EmbededdPrj_Demo > src > poly >service : 비즈니스 로직을 정의한 디렉터리
2021_EmbededdPrj_Demo > src > poly > util > kakaoClass.java : 센서 정보 카톡 알림 작성 부분 

4. 민감사항으로 인한 추가 데이터 입력이 필요한 파일 정보
가) 데이터베이스(MariaDB) 주소와 로그인 정보
- 경로 : 2021_EmbededdPrj_Demo > src > config >context-datasource.xml 
- 내용 : property 사용할 데이터베이스 주소 및 username, password

나) 데이터베이스(MongoDB) 주소와 로그인 정보
- 경로 : 2021_EmbededdPrj_Demo > src > config >context-mongodb.xml 
- 내용 : {}안에 있는 내용을 사용자 정보에 맞게 기입
	<mongo:mongo-client host="{DB 주소}"
		port="{포트번호}" credentials="{사용자ID@DB명}" id="mongo">
	</mongo:mongo-client>

다) kakao 로그인을 위한 사용자 정보 기입
- 경로 : 2021_EmbededdPrj_Demo > src > poly > service > impl > kakaoService
- 내용 : {} 안에 사용자 정보 기입
private final String Redirect_URI = {"카카오에 등록한 리다이렉트 주소 기입"};
private final String RESTAPI_KEY = {"카카오에서 발급한 사용자 키 기입"};
