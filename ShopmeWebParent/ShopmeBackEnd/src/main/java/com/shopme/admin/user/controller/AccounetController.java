package com.shopme.admin.user.controller;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.IShopUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
public class AccounetController {

	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal IShopUserDetails loggedUser, Model model) {
		String email = loggedUser.getUsername();
		User user = service.getByEmail(email);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", "Your account details");
		return "users/account_form";
		
		
	}
	@PostMapping("/account/update")
	public String saveDetails(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile,@AuthenticationPrincipal IShopUserDetails loggedUser) throws IOException {

		if (!multipartFile.isEmpty()) {

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);

			User saveUser = service.updateAccount(user);

			String uploadDir = "user-photos/" + saveUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.updateAccount(user);
		}
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		redirectAttributes.addFlashAttribute("message", "User account details hase been update successfully.");
		
		return "redirect:/account";
	}
}
