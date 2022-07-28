import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Pinpoint 설정 파일 수정 프로그램
 * collector ip 변경
 * transport module GRPC => THRIFT
 */
public class PinpointConfigurer {
	public static void main(String[] args) throws IOException {
		String filename = "./pinpoint-agent-2.4.0/pinpoint-root.config";
		String fixedConfig = null;
		String collectorIp = args[0];
		try(var reader = new BufferedReader(new FileReader(filename))) {
			String config = reader.lines().collect(Collectors.joining("\n"));
			fixedConfig = config.replaceFirst("profiler.collector.ip=(\\d{1,3}\\.){3}\\d{1,3}", "profiler.collector.ip=" + collectorIp)
				.replaceFirst("profiler.transport.module=GRPC", "profiler.transport.module=THRIFT");
		}

		try(var fileOutputStream =new FileOutputStream(filename)){
			fileOutputStream.write(fixedConfig.getBytes(StandardCharsets.UTF_8));
		}

	}
}
