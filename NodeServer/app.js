// mqtt 서버 연결
const mqtt = require('mqtt');
const url = 'mqtt://13.125.190.107'
const options = {
    port: '1883',
    //protocol: 'mqtts',
    username: 'admin',
    password: '1234',
    clean: true,
};

// mqtt 몽고 디비 연결
var mongoose = require('mongoose');
mongoose.connect('mongodb://yang:1234@13.125.190.107:19407/test');
var db = mongoose.connection;

// 몽고 연결 실패
db.on('error', function() {
    console.log('## Connection Failed! ##');
});
// 몽고 연결 성공
db.once('open', function() {
    console.log('## Connected! ##');
});
// 몽고 Schema 생성
var schema = mongoose.Schema({
    date: 'string',
    SENSOR_NUMBER: 'string',
    SENSOR_DATA: 'string'
});
// 몽고 정의된 스키마를 객체처럼 사용할 수 있도록 model() 함수로 컴파일
var data = mongoose.model('Schema', schema);


// 클라이언트 연결
const client = mqtt.connect(url, options);

// 연결 시 띄울 메시지
client.on("connect", function () {
    console.log("connetecd " + client.connected);
})

// 에러 발생 시
client.on("error", function (error) {
    console.log("Can't connect" + error);
    process.exit(1)
});

// 토픽을 통한 구독
const topic = 'test';
client.subscribe(topic, { qos: 0 }, function () {
    console.log("구독 성공");
});

// client.subscribe(topic);
/* qos는 3가지 수준으로 0~2 레벨 까지 있는데
0은 실패해도 계속 무시 UDP 마냥
1은 성공할 때 까지 쏜다.
2는 중복처리도 해준다 정확히 쏨
*/

// 메시지 받기
client.on('message', function (topic, message, packet) {
    console.log(`topic : ${topic.toString()}, 
                message :  ${message.toString()}`);

    //몽고 디비로 보낼 데이터 설정
    //센서 1번 사용
    const sensorName = message.toString().split("/")[0];
    //센서 데이터 저장
    const sensorData = message.toString().split("/")[1];
    //센서 저장 시간
    var now = NOW();

    var finalData = new data({
        date : now,
        SENSOR_NUMBER : sensorName,
        SENSOR_DATA : sensorData
    });

    finalData.save(function(error,data) {
        if(error) {
            console.log(error);
        } else {
            console.log('sensorName : ' + sensorName +' | sensorData : ' + sensorData + ' | saveDate : ' + now + 'insert success !');
        }
    })

});

function NOW() {

    var date = new Date();
    var aaaa = date.getUTCFullYear();
    var gg = date.getUTCDate();
    var mm = (date.getUTCMonth() + 1);

    if (gg < 10)
        gg = "0" + gg;

    if (mm < 10)
        mm = "0" + mm;

    var cur_day = aaaa + "." + mm + "." + gg;

    var hours = date.getUTCHours()
    var minutes = date.getUTCMinutes()
    var seconds = date.getUTCSeconds()

    if (hours < 10)
        hours = "0" + hours;

    if (minutes < 10)
        minutes = "0" + minutes;

    if (seconds < 10)
        seconds = "0" + seconds;

    return cur_day + "-" + hours + ":" + minutes + ":" + seconds;

}
// 종료 분기문을 써서 종료할 수 있음
// client.end();