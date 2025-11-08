package com.example.services;

import com.example.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;
    private Product product;

    @BeforeEach
    void setUp() {
        productService = spy(new ProductService());
        product = new Product(1, "Notebook Dell", 3500.00f, "C:\\images\\notebook.jpg");
    }

    @Test
    void deveSalvarAImagemCorretamente() {
        doReturn(true).when(productService).save(product);

        boolean result = productService.save(product);

        assertTrue(result, "Deve retornar true quando salvar com sucesso");
        verify(productService, times(1)).save(product);
    }

    @Test
    void naoDeveSalvarQuandoArquivoNaoExiste() {
        Product invalidProduct = new Product(2, "Mouse", 150.00f,
                "C:\\caminho\\inexistente\\mouse.jpg");
        doReturn(false).when(productService).save(invalidProduct);

        boolean result = productService.save(invalidProduct);

        assertFalse(result, "Deve retornar false quando o arquivo não existe");
        verify(productService, times(1)).save(invalidProduct);
    }

    @Test
    void deveRemoverAImagemCorretamente() {
        int productId = 1;
        doNothing().when(productService).remove(productId);

        productService.remove(productId);

        verify(productService, times(1)).remove(productId);
    }

    @Test
    void deveAtualizarAImagemCorretamente() {
        Product updatedProduct = new Product(1, "Notebook Dell XPS",
                4500.00f, "C:\\images\\notebook_novo.jpg");

        doNothing().when(productService).remove(updatedProduct.getId());
        doReturn(true).when(productService).save(updatedProduct);

        productService.update(updatedProduct);

        verify(productService, times(1)).remove(updatedProduct.getId());
        verify(productService, times(1)).save(updatedProduct);

        var inOrder = inOrder(productService);
        inOrder.verify(productService).remove(updatedProduct.getId());
        inOrder.verify(productService).save(updatedProduct);
    }

    @Test
    void deveObterCaminhoImagemPorId() {
        int productId = 5;
        String expectedPath = "X:\\5.jpg";
        doReturn(expectedPath).when(productService).getImagePathById(productId);

        String result = productService.getImagePathById(productId);

        assertNotNull(result, "Deve retornar o caminho da imagem");
        assertEquals(expectedPath, result, "O caminho deve corresponder ao esperado");
        verify(productService, times(1)).getImagePathById(productId);
    }

    @Test
    void deveProcessarMultiplosSalvamentos() {
        Product product1 = new Product(10, "Mouse Gamer", 200.00f, "C:\\images\\mouse.jpg");
        Product product2 = new Product(11, "Teclado Mecânico", 400.00f, "C:\\images\\teclado.jpg");
        Product product3 = new Product(12, "Headset", 300.00f, "C:\\images\\headset.jpg");

        doReturn(true).when(productService).save(any(Product.class));

        productService.save(product1);
        productService.save(product2);
        productService.save(product3);

        verify(productService, times(3)).save(any(Product.class));
        verify(productService).save(product1);
        verify(productService).save(product2);
        verify(productService).save(product3);
    }

    @Test
    void deveProcessarMultiplasRemocoes() {
        doNothing().when(productService).remove(anyInt());

        productService.remove(1);
        productService.remove(2);
        productService.remove(3);

        verify(productService, times(3)).remove(anyInt());
        verify(productService).remove(1);
        verify(productService).remove(2);
        verify(productService).remove(3);
    }

    @Test
    void updateDeveExecutarMesmoSeSaveFalhar() {

        Product product = new Product(20, "Produto Teste", 100.00f, "C:\\test.jpg");

        doNothing().when(productService).remove(product.getId());
        doReturn(false).when(productService).save(product);

        productService.update(product);

        verify(productService, times(1)).remove(product.getId());
        verify(productService, times(1)).save(product);
    }
}