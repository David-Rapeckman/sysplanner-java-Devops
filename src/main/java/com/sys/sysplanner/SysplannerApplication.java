package com.sys.sysplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SysplannerApplication {

	public static void main(String[] args) {
        SpringApplication.run(SysplannerApplication.class, args);
	}

}
