package com.shopme.Customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateCustomer1() {
		Integer countryId = 234;// usa
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();

		customer.setCountry(country);
		customer.setFirstName("David");
		customer.setLastName("Fountaine");
		customer.setPassword("password123");
		customer.setEmail("t@gmail.com");
		customer.setPhoneNumber("4564-445-456");
		customer.setAddressLine1("19923 west bangel");
		customer.setCity("Sacramento");
		customer.setState("California");
		customer.setPostalCode("99567");
		customer.setVerificationCode("code_123");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateCustomer2() {
		Integer countryId = 106;// india
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();

		customer.setCountry(country);
		customer.setFirstName("devan");
		customer.setLastName("Fountaine");
		customer.setPassword("password123");
		customer.setEmail("d@gmail.com");
		customer.setPhoneNumber("4564-445-456");
		customer.setAddressLine1("19923 west bangel");
		customer.setCity("Mumbai");
		customer.setState("Maharashtra");
		customer.setPostalCode("99567");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);

	}

	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();

		customers.forEach(System.out::println);

		assertThat(customers).hasSizeGreaterThan(1);
	}

	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String lastName = "Stanfield";

		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(true);

		Customer updatedCustomer = repo.save(customer);

		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}

	@Test
	public void testGetCustomer() {
		Integer customerId = 3;

		Optional<Customer> findById = repo.findById(customerId);

		assertThat(findById).isPresent();
		System.out.println(findById);
	}

	@Test
	public void testDeleteCustomer() {
		Integer customerId = 3;

		repo.deleteById(customerId);
		
		Optional<Customer> findById = repo.findById(customerId);

		assertThat(findById).isPresent();

	}
	
	@Test
	public void testFindByEmail() {
		String  email = "d@gmail.com";

		Customer findByEamil = repo.findByEmail(email);

		assertThat(findByEamil).isNotNull();
		System.out.println(findByEamil);
	}
	
	@Test
	public void testFindByVerificationCode() {
		String  verificationCdoe = "code_123";

		Customer findByCode = repo.findByVerificationCode(verificationCdoe);

		assertThat(findByCode).isNotNull();
		System.out.println(findByCode);
	}
	@Test
	public void testEnableCustomer() {
		Integer custmerId = 1;
		
		repo.enabled(custmerId);
		
		Customer customer = repo.findById(custmerId).get();
		
		assertThat(customer.isEnabled()).isTrue();
		
	
	}

}
