package com.prgrms.rg.infrastructure.cloud;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;

/**
 * 스프링에서 처리하지 못한 예외 메시지를 자바 애플리케이션 종료 직전에 외부 시스템에 전송합니다.
 * TODO : 추상화
 */
@UtilityClass
public class CriticalMessageSender {

	private static final String SLACK_CHANNEL_ID = System.getenv("SLACK_CHANNEL_NAME");
	private static final String SLACK_AUTH_TOKEN = System.getenv("SLACK_AUTH_TOKEN");

	public static void send(Exception exception) throws Exception {
		String stackTraceMessage = createStackTraceMessage(exception);
		String messageBody = createMessageBodyFrom(stackTraceMessage);
		sendHttpRequest(messageBody);
	}

	public static void send(String message) throws Exception {
		var messageBody = createMessageBodyFrom(message);
		sendHttpRequest(messageBody);
	}

	private static void sendHttpRequest(String body) throws Exception {

		URL url = new URL("https://slack.com/api/chat.postMessage");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		// request 헤더, 토큰 설정
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setConnectTimeout(5000); // 커넥션 생성 대기 시간(해당 시간 안에 커넥션 생성 안되면 SocketTimeoutException
		conn.setReadTimeout(5000);
		conn.setRequestProperty("Authorization", "Bearer " + SLACK_AUTH_TOKEN);

		// Json을 Http 요청 body에 담는다.
		OutputStream os = conn.getOutputStream();
		os.write(body.getBytes(StandardCharsets.UTF_8));
		os.flush();

		// Http 요청을 보내고 응답을 inputStream에 받아 온다.
		// 이 메서드를 사용하지 않으면 http 요청을 보내지 않기 때문에, 무조건 사용
		conn.getInputStream();
		conn.disconnect();
	}

	private static String createMessageBodyFrom(String message) throws JsonProcessingException {
		ObjectMapper json = new ObjectMapper();
		String emoji = ":meow_sad:\t";
		StringBuilder emojiBuffer = new StringBuilder();
		emojiBuffer.append(emoji.repeat(20));
		emojiBuffer.append("\n").append(message).append("\n");
		emojiBuffer.append(emoji.repeat(20));
		return json.writeValueAsString(Map.of("channel", SLACK_CHANNEL_ID, "text", emojiBuffer));
	}

	private static String createStackTraceMessage(Exception exception) {
		var stackStream = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(stackStream));
		return stackStream.toString(StandardCharsets.UTF_8);
	}

}
