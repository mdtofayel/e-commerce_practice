package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandControler {
	@Autowired
	private BrandService service;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/brands")
	public String listAll() {
		

		return "redirect:/brands/page/1?sortField=name&sortDir=asc";
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName= "listBrands", moduleURL= "/brands") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword
			) {
		service.listByPage(pageNum,helper);
		
		return "/brands/brands";
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = categoryService.listOfCategoriesInForm();
		
		model.addAttribute("listCategories",listCategories);
		model.addAttribute("brand",new Brand());
		model.addAttribute("pageTitle","Create New Brand");
		
		return"/brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand,@RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand saveBrand = service.save(brand);
			String uploadDir = "../brand-logos/"+saveBrand.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			service.save(brand);
		}
		
		redirectAttributes.addFlashAttribute("message","The Brand has been save successfully");
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id")Integer id, Model model, RedirectAttributes attributes) {
			try {
				Brand brand = service.get(id);
				List<Category> listCategories = categoryService.listOfCategoriesInForm();
				
				model.addAttribute("brand",brand);
				model.addAttribute("listCategories",listCategories);
				model.addAttribute("pageTitle","Edit Brand (ID: "+id+")");
				
				return "/brands/brand_form";
			} catch (BrandNotFoundException e) {
				attributes.addFlashAttribute("message", e.getMessage());
				return "redirect:/brands";
			}
		
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String brandDir = "../brand-logos/"+id;
			FileUploadUtil.removeDir(brandDir);
			redirectAttributes.addFlashAttribute("message", "The brand id " +id+" has been delte successfully");
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
		}
		
		return "redirect:/brands";
	}
}
