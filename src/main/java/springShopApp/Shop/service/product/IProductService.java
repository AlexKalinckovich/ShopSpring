package springShopApp.Shop.service.product;

import springShopApp.Shop.model.Product;
import springShopApp.Shop.request.AddProductRequest;
import springShopApp.Shop.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(UpdateProductRequest request, Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrand(String brand);

    Long countProductsByName(String name);

    Long countProductsByBrandAndName(String brand, String name);
}
