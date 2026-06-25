package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void search_shouldReturn_nonFeaturedProductsToo(){
        //Arrange
        List<Product> testProducts = new ArrayList<>();

        Product water = new Product(1, "water", 4.00, 1, "Hydration", "ermm", 100, true, null);
        Product bag = new Product(2, "Bag", 100.00, 2, "Hold items", "ermm", 80, true, null);
        Product shoes = new Product(3, "Shoes", 120.00, 3, "Feet holders", "ermm", 150, false, null);

        testProducts.add(water);
        testProducts.add(bag);
        testProducts.add(shoes);

        when(productRepository.findAll()).thenReturn(testProducts);

        //Act
        List<Product> product = productService.search(null, null, null, null);

        //Assert
        assertEquals(3, product.size());
    }

    @Test
    public void update_shouldUpdateStock_whenStockChanged(){
        //Arrange
        Product ogProduct = new Product(1, "product1", 100.00, 1, "ermm", "Errmm", 25, true, null);
        Product newProduct = new Product(1, "product1", 100.00, 1, "ermm", "Errmm", 50, true, null);

        when(productRepository.findById(1)).thenReturn(Optional.of(ogProduct));
        when(productRepository.save(ogProduct)).thenReturn(ogProduct);

        //Act
        Product updateProduct = productService.update(1, newProduct);

        //assert
        assertEquals(50, updateProduct.getStock());
        verify(productRepository).save(ogProduct);
    }
}
