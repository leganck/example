package org.example.tomcat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
	@RequestMapping("/home")
	public String home() {
		System.out.println( "-------------" );
		return "home";
	}
}

