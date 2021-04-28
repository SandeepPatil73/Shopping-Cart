package com.customer.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.IncorrectPasswordException;
import com.customer.UnableToCreateCustomerException;
import com.customer.entities.CustomerLogin;
import com.customer.entities.CustomerProfile;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerLoginRepository customerLoginRepository;
	@Autowired
	private CustomerProfileRepository CustomerRepository;
	
	@Override
	public CustomerProfile createCustomer(CustomerProfile customer) throws UnableToCreateCustomerException {
		CustomerProfile createCust = CustomerRepository.save(customer);
		if(createCust==null)
		{
			throw new UnableToCreateCustomerException();
		}
		return createCust;
	}
	
	@Override
	public List<CustomerProfile> getCustomers() throws NullPointerException{
		List<CustomerProfile> customers =CustomerRepository.getAllCustomers();
		if(customers==null)
		{
			throw new NullPointerException();
		}
		return customers;
	}
	
	@Override
	public void deleteCustomer(Integer id) {
		this.CustomerRepository.deleteById(id);
	}

	@Override
	public CustomerProfile getCustomerById(Integer id) throws NullPointerException {
		
		CustomerProfile customer = CustomerRepository.findById(id).get();
		if(customer==null)
			throw new NullPointerException();
		return customer;
	}

	@Override
	public CustomerProfile LoginCustomer(String email, String password) throws IncorrectPasswordException{
		CustomerProfile cust=null;
		CustomerLogin customerLogin1=customerLoginRepository.findByEmail(email);
		String pass2=customerLogin1.getPassword();
		if(pass2.equals(password))
		{
		    cust=customerLogin1.getCustomer();
		}
		else
		{
			throw new IncorrectPasswordException();
		}
	return cust;
	}
	
    @Override
	public CustomerLogin getCustomerByemail(String email) {
		CustomerLogin customer=this.customerLoginRepository.findByEmail(email);
		if(customer==null)
			throw new NullPointerException();
		return customer;
	}
}
