package ru.ardecs.hs.hsclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// TODO: 7/5/16 remove
@RestController
public class TempController {
	@Autowired
	DefaultWsdl11Definition defaultWsdl11Definition;

	@RequestMapping(value = "/temp.wsdl", produces = MediaType.APPLICATION_XML_VALUE)
	public String wsdl() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResource("/soap.wsdl").openStream()));
		StringBuilder sb = new StringBuilder();
		while (br.ready()) {
			sb.append(br.readLine());
		}
		return sb.toString();
	}
}
