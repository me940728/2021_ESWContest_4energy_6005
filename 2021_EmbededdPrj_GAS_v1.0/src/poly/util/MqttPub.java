package poly.util;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/* @Auth 최별규
 * @Version 1.1
 * MQTT 발행을 위한 코드부분 
 *  ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2021.00.00   |  최별규     | 초안 작성
 * |              |  홍석민     | 초안 작성
 * |              |  양원석     | 초안 작성 
 * */

public class MqttPub {
	
	public static void mqttPub(String username, String password, String topic, String content) {
        int qos = 0;
        String broker = "{ }"; // => tcp://ip:1883
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            
            sampleClient.disconnect();
            System.out.println("Disconnected");
			/* System.exit(0); */
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
	}
}
