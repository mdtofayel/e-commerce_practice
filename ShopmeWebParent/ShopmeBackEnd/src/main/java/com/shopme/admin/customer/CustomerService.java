package com.shopme.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMER_PER_PAGE = 10;
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMER_PER_PAGE, sort);

		if (keyword != null) {
			return customerRepo.findAll(keyword, pageable);
		}

		return customerRepo.findAll(pageable);
	}

	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CustomerNotFoundException("Could not found an customer with ID : " + id);
		}
	}

	public List<Country> listAllCountres() {
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(Integer id, String eamil) {
		Customer existCustomer = customerRepo.findByEmail(eamil);
		
		if(existCustomer != null && existCustomer.getId() != id) {
			return false;
		}
		
		return true;
	}
	public void save( Customer customerInForm) {
		Customer customerIndDb = customerRepo.findById(customerInForm.getId()).get();
		
		if( customerInForm.getPassword() != null && !customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);
		}else {
			customerInForm.setPassword(customerIndDb.getPassword());
		}
		
		customerInForm.setEnabled(customerIndDb.isEnabled());
		customerInForm.setCreatedTime(customerIndDb.getCreatedTime());
		customerInForm.setVerificationCode(customerIndDb.getVerificationCode());
		customerRepo.save(customerInForm);
	}
	
	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = customerRepo.countById(id);
		
		if(count == null || count == 0) {
			throw new CustomerNotFoundException("Cound not find any customer with id: "+ id);
		}
		
		customerRepo.deleteById(id);
	}
}
