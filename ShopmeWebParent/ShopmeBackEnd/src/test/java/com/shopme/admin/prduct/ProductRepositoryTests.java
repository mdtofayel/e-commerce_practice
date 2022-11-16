package com.shopme.admin.prduct;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;

import com.jayway.jsonpath.Option;
import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

/*@RunWith(SpringRunner.class)*/
/*@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)*/ /*
					 * @Component
					 * 
					 * @ComponentScan("com.shopme.admin.product.ProductRepository")
					 */
/*@ComponentScan({"com.shopme.admin.product","com.shopme.admin.product.ProductRepository"})*/
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
	
	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	
	@Test
	 public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class,37);
		Category category = entityManager.find(Category.class, 5);
		
		/*
		 * Brand brand = brandService.findById(1).get(); Category category =
		 * categoryService.findById(6).get()
		 */
		
		Product product = new Product();
		product.setName("Acer Aspire Desktop");
		product.setAlias("acer_aspire_desktop");
		product.setShortDescription("short drecription");
		product.setFullDescription("Full descrioption");
		
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(456);
		product.setCost(3000);
		product.setEnabled(true);
		product.setInStock(true);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		
		Product saveProduct = productRepository.save(product);
		
		assertThat(saveProduct).isNotNull();
		assertThat(saveProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllPruducts() {
		Iterable<Product> iterableProducts = productRepository.findAll();
		
		iterableProducts.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		
		Integer id = 2;
		Product product = productRepository.findById(id).get();
		
		System.out.println(product);
		
		assertThat(product).isNotNull();
	}
	
	@Test
	public void testUpdateProduct() {
		Integer id = 3;
		Product product = productRepository.findById(id).get();
		product.setPrice(500000);
		productRepository.save(product);
		
		Product updateProduct = entityManager.find(Product.class, id);
		
		assertThat(updateProduct.getPrice()).isEqualTo(500000);
		
	}
	
	@Test
	public void testDelete() {
		Integer id = 1;
		productRepository.deleteById(id);
		
		Optional<Product> result = productRepository.findById(id);
		
		assertThat(!result.isPresent());
	}
	
	@Test
	public void testSaveProductWithImages() {
		Integer productId = 2;
		Product product = productRepository.findById(productId).get();
		
		product.setMainImage("main_image.jpg");
		product.addExtraImage("extra_image_1");
		product.addExtraImage("extra_image_2");
		product.addExtraImage("extra_image_3");
		
		Product saveProduct = productRepository.save(product);
		
		assertThat(saveProduct.getImages().size()).isEqualTo(3);
		
	}
	@Test
	public void testSaveProductDetails() {
		Integer product2 = 2;
		Product product = productRepository.findById(product2).get();
		
		product.addDetails("Device Memory","128 GB");
		product.addDetails("CPU Model", "MediaTek");
		product.addDetails("OS", "Android 10");
		
		Product savedProduct = productRepository.save(product);
		assertThat(savedProduct.getDetails()).isNotEmpty();
		
	}
	
}
