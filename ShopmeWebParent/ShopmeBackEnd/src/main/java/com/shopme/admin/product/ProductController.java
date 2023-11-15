package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.security.IShopUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {

		return listByPage(1, model, "name", "asc", null,0);
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId
			) {
		
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword,categoryId);
		List<Product> listProudcts = page.getContent();
		
		List<Category> listCategories = categoryService.listOfCategoriesInForm();
		
		
		long startCount = (pageNum - 1)* ProductService.PRODUCTS_PER_PAGE +1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if(endCount >= page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc")? "desc" : "asc";
		
		if (categoryId!=null) model.addAttribute("categoryId", categoryId);
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir",sortDir);
		model.addAttribute("reverseSortDir",reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listProudcts",listProudcts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("moduleURL", "/products");
		
		return "/products/products";
	}
	

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.findAll();

		Product product = new Product();

		model.addAttribute("listBrands", listBrands);
		model.addAttribute("product", product);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "/products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProuduct(Product product, @RequestParam(value="fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value="extraImage", required = false) MultipartFile[] extraImagesMultipart, RedirectAttributes redirectAttributes,
			@RequestParam(name="detailNames", required = false)String[] detailNames,
			@RequestParam(name="detailIDs", required = false)String[] detailIDs,
			@RequestParam(name="detailValues", required = false)String[] detailValues,
			@RequestParam(name="imageIDs", required = false)String[] imageIDs,
			@RequestParam(name="imageNames", required = false)String[] imageNames,
			@AuthenticationPrincipal IShopUserDetails loggedUser)
					throws IOException {
		
		if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if(loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				redirectAttributes.addFlashAttribute("message", "The Brand has been save successfully");
				return "redirect:/products";
			}			
		}

		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIDs,imageNames,product);
		ProductSaveHelper.setNewExtraImages(extraImagesMultipart, product);
		ProductSaveHelper.setProductDetails(detailIDs,detailNames,detailValues,product);
		Product savePrpduct = productService.save(product);

		ProductSaveHelper.saveUploadedImage(mainImageMultipart, extraImagesMultipart, savePrpduct);
		
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		
		redirectAttributes.addFlashAttribute("message", "The Brand has been save successfully");
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
	public String deleteProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {

		try {
			productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productMainImagesDir = "../product-images/" + id;

			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productMainImagesDir);

			ra.addFlashAttribute("message", "The Product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.findAll();
			Integer numberOfExistingExtraImages = product.getImages().size();

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (" + id + " )");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			return "products/product_form";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products/products";
		}
	}
	@GetMapping("/products/details/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
		
			model.addAttribute("product", product);
			
			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products/products";
		}
	}
}
