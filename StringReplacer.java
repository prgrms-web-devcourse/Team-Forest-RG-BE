import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * pinpoint의 profiler.collector.ip 수정용
 */
public class StringReplacer {
	public static void main(String[] args) throws IOException {
		String filename = "./pinpoint-agent-2.4.0/pinpoint-root.config";
		String fixed = null;
		try(var stream = new BufferedReader(new FileReader(filename))) {
			String config = stream.lines().collect(Collectors.joining("\n"));
			fixed = config.replaceFirst("profiler.collector.ip=(\\d{1,3}\\.){3}\\d{1,3}", "profiler.collector.ip=49.50.172.148");
			fixed = fixed.replaceFirst("127\\.0\\.0\\.1", "49.50.172.148");
			fixed = fixed.replaceFirst("profiler.transport.module=GRPC", "profiler.transport.module=THRIFT");
		}

		try(var fileOutputStream =new FileOutputStream(filename)){
			fileOutputStream.write(fixed.getBytes(StandardCharsets.UTF_8));
		}

	}
}
