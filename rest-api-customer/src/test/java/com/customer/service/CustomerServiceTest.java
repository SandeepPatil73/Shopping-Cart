package com.customer.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.customer.IncorrectPasswordException;
import com.customer.UnableToCreateCustomerException;
import com.customer.entities.Address;
import com.customer.entities.CustomerLogin;
import com.customer.entities.CustomerProfile;
import com.customer.services.CustomerLoginRepository;
import com.customer.services.CustomerProfileRepository;
import com.customer.services.CustomerService;
import com.customer.services.CustomerServiceImpl;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {
	
	@InjectMocks
	CustomerServiceImpl service;
	
	@Mock
	CustomerProfileRepository profileRepo;
	
	@Mock
	CustomerLoginRepository loginRepo;
	
	Address address=new Address("Vishnupuri","Nanded","Maharashtra");
	CustomerLogin customerLogin=new CustomerLogin(1,"sandeep@gmail.com","sandeep@123","User");
	CustomerProfile customer=new CustomerProfile(3, "sandeep","patil", "1234532728",address, Date.valueOf(LocalDate.of(1999,5,15)),customerLogin);

	   @BeforeEach
	    public void init() {
	        MockitoAnnotations.initMocks(this);
	    }
		
	@Test
	public void getCustomerById_Success_Test() {	
		when(profileRepo.findById(anyInt())).thenReturn(Optional.of(customer));
		CustomerProfile customer=service.getCustomerById(3);
		assertEquals(customer.getFname(), "sandeep");	
		assertNotNull(customer);	
	}
	
	@Test
	public void getCustomerById_Fail_Test() {	
		when(profileRepo.findById(anyInt())).thenReturn(null);
		assertThrows(NullPointerException.class, ()->service.getCustomerById(3));	
	}
	
	@Test
	public void getCustomers_Success_Test()
	{
		ArrayList<CustomerProfile> al=new ArrayList<>();
		al.add(customer);
		when(profileRepo.getAllCustomers()).thenReturn(al);
		List<CustomerProfile> li=service.getCustomers();	
		assertEquals(1, li.size());
	}
	
	@Test
	public void getCustomers_Fail_Test()
	{
		when(profileRepo.getAllCustomers()).thenReturn(null);
		assertThrows(NullPointerException.class, ()->service.getCustomers());
	}
	
	@Test
	public void createCustomer_Success_Test() throws UnableToCreateCustomerException {
		when(profileRepo.save(Mockito.any(CustomerProfile.class))).thenReturn(customer);
		CustomerProfile result=service.createCustomer(customer);
		assertNotNull(result);
	}
	
	@Test
	public void createCustomer_Fail_Test() throws UnableToCreateCustomerException {
		when(profileRepo.save(Mockito.any(CustomerProfile.class))).thenReturn(null);
		assertThrows(UnableToCreateCustomerException.class, ()->service.createCustomer(customer));
	}
	
	@Test
	public void getCustomerByEmail_Success_test() {
		when(loginRepo.findByEmail(anyString())).thenReturn(customerLogin);	
		customerLogin=service.getCustomerByemail("sandeep@gmail.com");
		assertEquals(customerLogin.getRole(), "User");
	}
	
	@Test
	public void getCustomerByEmail_Fail_test() {
		when(loginRepo.findByEmail(anyString())).thenReturn(null);	
		assertThrows(NullPointerException.class, ()->service.getCustomerByemail("sandeep@gmail.com"));
	}
	
	@Test
	public void loginCustomer_Success_Test() throws IncorrectPasswordException{
		
		String password = "sandeep@123" ;
		String email="sandeep@gmail.com";
		when(loginRepo.findByEmail(anyString())).thenReturn(customerLogin);
		
		CustomerProfile result=service.LoginCustomer(email, password);
		assertNotNull(result);
	}
	
	@Test
	public void loginCustomer_Fail_Test() throws IncorrectPasswordException{
		when(loginRepo.findByEmail(anyString())).thenReturn(customerLogin);
		String password = "pass";
		String email="random@gmail.com";
		
		assertThrows(IncorrectPasswordException.class, ()->service.LoginCustomer(email, password));
	}
	
	@Test
	public void deleteByIdTest() {
		service.deleteCustomer(customer.getCp_id());
		verify(profileRepo, times(1)).deleteById(customer.getCp_id());
	}
	
}
