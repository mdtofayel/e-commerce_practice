package com.shopme.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("")
	public String viewHomePage() {
		System.out.println("Hello mta testing.");
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		return "login";
	}
}
