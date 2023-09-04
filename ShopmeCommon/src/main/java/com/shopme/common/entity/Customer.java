package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, unique = true, length = 45)
	private String email;
	
	@Column(nullable = false, length = 64)
	private String  password;
	
	@Column(name = "first_name",nullable = false, length = 45)
	private String firstName;
	
	@Column(name = "last_name",nullable = false, length = 45)
	private String lasttName;
	
	@Column(name = "phone_number",nullable = false, length = 15)
	private String phoneNumber;
	
	@Column(nullable = false, length = 64)
	private String addressLine1;
	
	@Column(name ="address_line_2",nullable = false, length = 64)
	private String addressLine2;
	
	@Column(nullable = false, length = 45)
	private String city;
	
	@Column(nullable = false, length = 45)
	private String state;
	
	@Column(name = "postal_code",nullable = false, length = 10)
	private String postalCode;	
	
}
