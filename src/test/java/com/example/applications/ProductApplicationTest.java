package com.example.applications;

import com.example.entities.Product;
import com.example.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductApplicationTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductApplication productApplication;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1, "Notebook Dell", 3500.00f, "C:\\images\\notebook.jpg");
    }

    @Test
    void deveSalvarAImagemCorretamente() {
        when(productService.save(product)).thenReturn(true);

        productApplication.append(product);

        verify(productService, times(1)).save(product);
    }

    @Test
    void deveRemoverAImagemCorretamente() {
        int productId = 1;
        doNothing().when(productService).remove(productId);

        productApplication.remove(productId);

        verify(productService, times(1)).remove(productId);
    }

    @Test
    void deveAtualizarAImagemCorretamente() {
        int productId = 1;
        Product updatedProduct = new Product(productId, "Notebook Dell XPS", 4500.00f, "C:\\images\\notebook_new.jpg");
        doNothing().when(productService).update(updatedProduct);

        productApplication.update(productId, updatedProduct);

        verify(productService, times(1)).update(updatedProduct);
    }
}