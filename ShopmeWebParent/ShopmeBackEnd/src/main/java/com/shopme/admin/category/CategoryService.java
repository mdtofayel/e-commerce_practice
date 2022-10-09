package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.math3.geometry.spherical.twod.SubCircle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository repo;

	public List<Category> listAll() {
		List<Category> findRootCategory = repo.findRootCategory();
		return listOfHierachicalCategories(findRootCategory);
	}

	private List<Category> listOfHierachicalCategories(List<Category> rootCategories) {
		List<Category> hierachicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierachicalCategories.add(Category.copyFull(rootCategory));
			
			Set<Category> children = rootCategory.getChildren();
			
			for(Category subCategory: children) {
				String name = "--" + subCategory.getName();
				hierachicalCategories.add(Category.copyFull(subCategory, name));
				
				listOfHierachicalCategories(hierachicalCategories, subCategory, 1);
				
			}
		}

		return hierachicalCategories;
	}

	public List<Category> listOfCategoriesInForm() {
		List<Category> listOfCategoriesUsedInForn = new ArrayList<>();

		Iterable<Category> categoriesInDB = repo.findAll();

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				listOfCategoriesUsedInForn.add(Category.copyIdAndName(category));
				Set<Category> children = category.getChildren();
				for (Category child : children) {
					String name = "--" + child.getName();
					listOfCategoriesUsedInForn.add(Category.copyIdAndName(child.getId(), name));

					lsitChildrem(listOfCategoriesUsedInForn, child, 1);
				}
			}
		}
		return listOfCategoriesUsedInForn;
	}

	private void lsitChildrem(List<Category> listOfCategoriesUsedInForn, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> subCategories = parent.getChildren();
		for (Category subCategory : subCategories) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			listOfCategoriesUsedInForn.add(Category.copyIdAndName(subCategory.getId(), name));
			lsitChildrem(listOfCategoriesUsedInForn, subCategory, newSubLevel);
		}
	}
	
	private void listOfHierachicalCategories(List<Category> hierachicalCategories, Category parent, int subLevel) {
		
		Set<Category> children = parent.getChildren();
		int newSublevel = subLevel + 1;
		for(Category subCategory : children) {
			String name = "";
			for(int i = 0; i < newSublevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierachicalCategories.add(Category.copyFull(subCategory,name));
			
			listOfHierachicalCategories(hierachicalCategories, subCategory, newSublevel);
		}
	}

	public Category get(Integer id) throws CategoryNotFoundException{
		try {
			return repo.findById(id).get();			
		} catch (NoSuchElementException ex ) {
			throw new CategoryNotFoundException("Could not find any category with ID: " + id);
		}
	}
	public Category save(Category category) {
		return repo.save(category);
	}

}
