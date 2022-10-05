package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "manege everythings");
		Role savedRole = repo.save(roleAdmin);
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role rolesSalesperson = new Role("Salesperson", "manage product price,"
				+ "customers, shipping, orders and sales report");
		
		Role rolesEditor = new Role("Editor", "manage product Categories,"
				+ "products, articles and manus");
		Role rolesShipper = new Role("Shipper", "view products, view orders"
				+ "update order status");
		Role rolesAssistant = new Role("Assistant", "manage questions and reviews");
		repo.saveAll(List.of(rolesSalesperson, rolesEditor, rolesShipper, rolesAssistant));
	}
	

}
