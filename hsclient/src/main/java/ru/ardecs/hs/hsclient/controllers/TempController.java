package ru.ardecs.hs.hsclient.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// TODO: 7/5/16 remove
@RestController
public class TempController {
	@RequestMapping(value = "/temp.wsdl", produces = MediaType.APPLICATION_XML_VALUE)
	public String wsdl() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResource("/statistic/soap.wsdl").openStream()));
		StringBuilder sb = new StringBuilder();
		while (br.ready()) {
			sb.append(br.readLine());
		}
		return sb.toString();
	}
}
