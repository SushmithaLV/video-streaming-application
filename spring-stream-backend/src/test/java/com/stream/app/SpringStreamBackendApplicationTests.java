package com.stream.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stream.app.services.VideoService;

@SpringBootTest
class SpringStreamBackendApplicationTests {
	
	@Autowired
	VideoService videoService;

	@Test
	void contextLoads() {
		videoService.processVideo("f9072ee5-f94e-46f8-b417-862ec3b0145f");
	}

}
