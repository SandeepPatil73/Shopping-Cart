package com.product.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.product.UnableToCreateProductException;
import com.product.entities.Product;
import com.product.services.ProductRepository;
import com.product.services.ProductServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
	
	@Mock
	ProductRepository repo;
	
	@InjectMocks
	ProductServiceImpl service;
	
	@BeforeEach
     void init() {
        MockitoAnnotations.initMocks(this);
    }
	
	Product product=new Product("Reliance T-shirt","full slevees T-shirt","T-shirt","Red",12000l);
	
	@Test
	public void createProduct_Success_Test() {
		repo.save(product);
		verify(repo,times(1)).save(product);
	}
	
	@Test
	public void createProduct_Fail_Test() throws UnableToCreateProductException{
		when(repo.save(Mockito.any(Product.class))).thenReturn(null);
		assertThrows(UnableToCreateProductException.class,()->service.createProduct(product));
	}
	
	@Test
	public void getProducts_Success_Test() {
		List<Product> li=new ArrayList<Product>();
		li.add(product);
		
		when(repo.loadAll()).thenReturn(li);
		List<Product> li1=service.getProducts();
		assertEquals(1, li1.size());	
	}
	
	@Test
	public void getProducts_Fail_Test() {
		when(repo.loadAll()).thenReturn(null);
		assertThrows(NullPointerException.class,()->service.getProducts());
	}
	
	@Test
	public void getProductByPid_Success_Test() {
		when(repo.findById(anyInt())).thenReturn(Optional.of(product));
		Product product2 = service.getProduct(0);
		assertEquals(product2.getName(), "Reliance T-shirt");
	}
	
	@Test
	public void getProductByPid_Fail_Test() {
		when(repo.findById(anyInt())).thenReturn(null);
		assertThrows(NullPointerException.class,()->service.getProduct(0));
	}
	
	@Test
	public void getProductByColour_Success_Test() {
		List<Product> li=new ArrayList<Product>();
		li.add(product);
		
		when(repo.findByColor(product.getColor())).thenReturn(li);
		List<Product> li1=service.getProductsByColor("Red");
		assertEquals(1, li1.size());
		
	}
	
	
	@Test
	public void getProductsByColor_Fail_Test() {
		when(repo.findByColor(anyString())).thenReturn(null);
		assertThrows(NullPointerException.class,()->service.getProductsByColor("Red"));
	}
	
	@Test
	public void getProductsByCategory_Success_Test() {
		when(repo.findByCategory(anyString())).thenReturn(Arrays.asList(product,new Product()));
		List<Product> responseProduct=service.getProductsByCategory("T-shirt");
		assertNotNull(responseProduct);
	}
	
	@Test
	public void getProductsByCategory_Fail_Test() {
		when(repo.findByCategory(anyString())).thenReturn(null);
		assertThrows(NullPointerException.class,()->service.getProductsByCategory("T-shirt"));
	}
	@Test
	public void getProductsByColorAndCategory_Success_Test() {
		when(repo.findByColorAndCategory(anyString(), anyString())).thenReturn(Arrays.asList(product,new Product()));
		List<Product> responseProduct=service.getProductsByColorAndCategory("Red","T-shirt");
		assertNotNull(responseProduct);
	}
	
	@Test
	public void getProductsByColorAndCategory_Fail_Test() {
		when(repo.findByColorAndCategory(anyString(),anyString())).thenReturn(null);
		assertThrows(NullPointerException.class,()->service.getProductsByColorAndCategory("Red","T-shirt"));
	}
	
	@Test
	public void getProductsByName_Success_Test() {
		when(repo.findByName(anyString())).thenReturn(Arrays.asList(product,new Product()));
		List<Product> responseProduct=service.getProductsByName("T-shirt Reliance");
		assertNotNull(responseProduct);
	}
	
	@Test
	public void getProductsByName_Fail_Test() {
		when(repo.findByName(Mockito.anyString())).thenReturn(Arrays.asList());
		assertThrows(NullPointerException.class,()->service.getProductsByName("T-shirt Reliance"));
	}
	
	@Test
	public void deleteByIdTest() {
		service.deleteProduct(product.getId());
		verify(repo, times(1)).deleteById(product.getId());
	}
	
	

}
