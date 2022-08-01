package com.prgrms.rg.infrastructure.cloud.message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 스프링에서 처리하지 못한 예외 메시지를 자바 애플리케이션 종료 직전에 특정 컴포넌트에 전송시킵니다.
 * TODO : 추상화
 */
public class CriticalMessageSender {

	private static final String SLACK_CHANNEL_ID = System.getenv("SLACK_CHANNEL_NAME");
	private static final String SLACK_AUTH_TOKEN = System.getenv("SLACK_AUTH_TOKEN");

	public void send(Exception exception) throws JsonProcessingException {
		ObjectMapper json = new ObjectMapper();
		var exceptionStream = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(exceptionStream));
		String emoji = ":meow_sad:\t";
		StringBuilder emojiBuffer = new StringBuilder();
		emojiBuffer.append(emoji.repeat(20));
		emojiBuffer.append("\n").append(exceptionStream.toString(StandardCharsets.UTF_8)).append("\n");
		emojiBuffer.append(emoji.repeat(20));

		String body = json.writeValueAsString(
			Map.of("channel", SLACK_CHANNEL_ID, "text", emojiBuffer));
		try {
			URL url = new URL("https://slack.com/api/chat.postMessage");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setConnectTimeout(5000); // 커넥션 생성 대기 시간(해당 시간 안에 커넥션 생성 안되면 SocketTimeoutException
			conn.setReadTimeout(5000);
			conn.setRequestProperty("Authorization",
				"Bearer " + SLACK_AUTH_TOKEN);

			OutputStream os = conn.getOutputStream();
			os.write(body.getBytes(StandardCharsets.UTF_8));
			os.flush();

			// 리턴된 결과 읽기
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			in.lines().forEach((str) -> {
			});
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
