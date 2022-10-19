package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
@Transactional
public class CategoryService {

	@Autowired
	CategoryRepository repo;

	public List<Category> listAll(String sortDir) {
		Sort sort = Sort.by("name");

		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if  (sortDir.equals("desc")) {
			sort = sort.descending();
		}

		List<Category> findRootCategory = repo.findRootCategory(sort);
		return listOfHierachicalCategories(findRootCategory, sortDir);
	}

	private List<Category> listOfHierachicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> hierachicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierachicalCategories.add(Category.copyFull(rootCategory));

			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierachicalCategories.add(Category.copyFull(subCategory, name));

				listOfSubHierachicalCategories(hierachicalCategories, subCategory, 1, sortDir);

			}
		}

		return hierachicalCategories;
	}

	private void listOfSubHierachicalCategories(List<Category> hierachicalCategories, Category parent, int subLevel,
			String sortDir) {

		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSublevel = subLevel + 1;
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSublevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierachicalCategories.add(Category.copyFull(subCategory, name));

			listOfSubHierachicalCategories(hierachicalCategories, subCategory, newSublevel, sortDir);
		}
	}

	public List<Category> listOfCategoriesInForm() {
		List<Category> listOfCategoriesUsedInForn = new ArrayList<>();

		Iterable<Category> categoriesInDB = repo.findRootCategory(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				listOfCategoriesUsedInForn.add(Category.copyIdAndName(category));
				Set<Category> children = sortSubCategories(category.getChildren());
				for (Category child : children) {
					String name = "--" + child.getName();
					listOfCategoriesUsedInForn.add(Category.copyIdAndName(child.getId(), name));

					listOfSubCategoriesInForm(listOfCategoriesUsedInForn, child, 1);
				}
			}
		}
		return listOfCategoriesUsedInForn;
	}

	private void listOfSubCategoriesInForm(List<Category> listOfCategoriesUsedInForn, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> subCategories = sortSubCategories(parent.getChildren());
		for (Category subCategory : subCategories) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			listOfCategoriesUsedInForn.add(Category.copyIdAndName(subCategory.getId(), name));
			listOfSubCategoriesInForm(listOfCategoriesUsedInForn, subCategory, newSubLevel);
		}
	}

	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any category with ID: " + id);
		}
	}

	public Category save(Category category) {
		return repo.save(category);
	}

	public String checkUnique(Integer id, String name, String alias) {

		boolean isCategoryNew = (id == null || id == 0);

		Category categoryByName = repo.findByName(name);

		if (isCategoryNew) {
			if (categoryByName != null) {
				return "DuplicateName";
			} else {
				Category categoryAlias = repo.findByAlias(alias);
				if (categoryAlias != null) {
					return "DuplicateAliasName";
				}
			}
		} else {
			if (categoryByName != null && categoryByName.getId() != id) {
				return "DuplicateName";
			}
			Category categoryAlias = repo.findByAlias(alias);
			if (categoryAlias != null && categoryAlias.getId() != id) {
				return "DuplicateAliasName";
			}
		}
		return "OK";
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}

			}
		});
		sortedChildren.addAll(children);
		return sortedChildren;
	}
	
	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
			repo.updateEnabledStatus(id, enabled);
		
	}

}
