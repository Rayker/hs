package ru.ardecs.hs.hsapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PingController {
	private static Logger logger = LoggerFactory.getLogger(PingController.class);

	@RequestMapping("/ping")
	public ResponseEntity ping(HttpServletRequest request) {
		logger.debug("ping request from {}", request.getRemoteAddr());
		return new ResponseEntity(HttpStatus.OK);
	}
}
