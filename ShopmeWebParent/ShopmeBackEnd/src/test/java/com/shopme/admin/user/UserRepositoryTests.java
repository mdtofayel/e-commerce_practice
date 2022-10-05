package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("mta.ahmed@gmail.com", "mdAhmed", "Tofayel","Ahmed");
		user.addRole(roleAdmin);
		
		User savedUser = repo.save(user);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userRakib = new User("raki@gmail.com","raki123","Rakib", "Hossain");
		
		Role roleEditor = new Role(3);
		Role RoleAssistant = new Role(5);
		
		userRakib.addRole(roleEditor);
		userRakib.addRole(RoleAssistant);
		
		User savedUser = repo.save(userRakib);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listOfUsers = repo.findAll();
		
		listOfUsers.forEach(user -> System.out.println(user));
	}
	@Test
	public void testGetUserById(){
		User userAhmed = repo.findById(1).get();
		System.out.println(userAhmed);
		assertThat(userAhmed).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetais() {
		User userAhmed = repo.findById(1).get();
		userAhmed.setEnabled(true);
		userAhmed.setEmail("abc@gmail.com");
		repo.save(userAhmed);
	}
	
	@Test
	public void testUpdateRoles() {
		User userRaki = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		userRaki.getRoles().remove(roleEditor);
		userRaki.addRole(roleSalesperson);
		repo.save(userRaki);
	}
	
	@Test 
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "abc@cccc.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		
		Integer id = 1;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test 
	public void testDisableUser() {
		Integer id = 1; 
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 4;
		int pageSize   = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		int pageNumber = 0;
		int pageSize   = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword,pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
		
	}
	
}
