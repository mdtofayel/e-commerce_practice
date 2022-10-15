package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;

	@Test
	public void testCreateReopositiry() {
		Category category = new Category("Electronics");
		Category savedCategory = repo.save(category);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(7);
		Category subCategory = new Category("iPhone", parent);
		Category savedCategory = repo.save(subCategory);
		System.out.print(savedCategory.getName() + " " + savedCategory.getId());
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testPrintHierachicalCategories() {
		Iterable<Category> categories = repo.findAll();

		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());

				Set<Category> childrent = category.getChildren();
				for (Category child : childrent) {
					System.out.println("--" + child.getName());
					printChildrent(child, 1);
				}
			}
		}
	}

	private void printChildrent(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> subCategories = parent.getChildren();
		for (Category subCategory : subCategories) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			
			printChildrent(subCategory, newSubLevel);
		}
	}
	
	@Test
	public void testListRootCategory() {
		List<Category> testListOfRootCategory = repo.findRootCategory(Sort.by("name").ascending());
		testListOfRootCategory.forEach(cat -> System.out.println(cat.getName()));
	}
	
	@Test
	public void testFindByName() {
		String name = "Computers";
		Category cat = repo.findByName(name);
		assertThat(cat).isNotNull();
		assertThat(cat.getName()).isEqualTo(name);
	}
	
	
	@Test
	public void testFindByAlias() {
		String name = "Electronics";
		
		Category category = repo.findByAlias(name);
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
}
