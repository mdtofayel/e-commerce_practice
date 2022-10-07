package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	CategoryService service;
	
	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> listCategories = service.listAll();
			System.out.println(listCategories.size());
		for(Category category : listCategories) {
			System.out.println(category.getName());
		}
		
		model.addAttribute("listCategories",listCategories);
		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		 	model.addAttribute("category", new Category());
		 	model.addAttribute("pageTitle", "Create New Category");
		 	return "categories/category_form";
	}
}
