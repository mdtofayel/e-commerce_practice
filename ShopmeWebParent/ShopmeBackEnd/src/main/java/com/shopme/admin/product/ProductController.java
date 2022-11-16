package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired 
	private BrandService brandService;
	
	@GetMapping("/products")
	public String listAll(Model model) {
		
		List<Product> listProudcts = productService.listAll();
		model.addAttribute("listProudcts",listProudcts);
		return "/products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands  = brandService.findAll();
		
		Product product = new Product();
		
		model.addAttribute("listBrands",listBrands);
		model.addAttribute("product",product);
		model.addAttribute("pageTitle","Create New Product");
		
	return "/products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProuduct(Product product,@RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setMainImage(fileName);
			
			Product savePrpduct = productService.save(product);
			String uploadDir = "../product-images/"+savePrpduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			productService.save(product);
		}
		
		redirectAttributes.addFlashAttribute("message","The Brand has been save successfully");
		return "redirect:/products";
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {

		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name="id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			productService.delete(id);
			ra.addFlashAttribute("message", "The Product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/products";
	}
}
