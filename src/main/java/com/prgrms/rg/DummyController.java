package com.prgrms.rg;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
	@GetMapping("/")
	public Map<String, String> hello() {
		return Map.of("hello","world");
	}
}
