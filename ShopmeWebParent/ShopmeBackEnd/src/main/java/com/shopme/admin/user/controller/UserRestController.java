package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserService;

@RestController
public class UserRestController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/users/check_email")
	public String checkDuplicateEamil(@Param("id") Integer id,@Param("email") String email) {	
		System.out.println("I got a new email: "+email);
		return userService.isEmailUnique(id, email) ?  "OK" :"Duplicated";
	}

}
