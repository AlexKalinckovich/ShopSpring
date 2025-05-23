package springShopApp.Shop.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import springShopApp.Shop.exceptions.ProductNotFoundException;
import springShopApp.Shop.model.Category;
import springShopApp.Shop.model.Product;
import springShopApp.Shop.repository.CategoryRepository;
import springShopApp.Shop.repository.ProductRepository;
import springShopApp.Shop.request.AddProductRequest;
import springShopApp.Shop.request.UpdateProductRequest;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public Product addProduct(final AddProductRequest request) {
        final String categoryName = request.getCategory().getName();

        final Category category = Optional.ofNullable(
                categoryRepository.findByName(categoryName)
                ).orElseGet(() -> {
                    final Category createdCategory = new Category(categoryName);
                    return categoryRepository.save(createdCategory);
                });

        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(final AddProductRequest request, final Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with that id not found."));
    }

    @Override
    public void deleteProductById(Long id) {
        final Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product with that id not found."));
    }

    private Product updateExistingProduct(final Product existingProduct, final UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        final Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findProductByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrand(String brand) {
        return productRepository.countByBrand(brand);
    }

    @Override
    public Long countProductsByName(String name) {
        return productRepository.countByName(name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
}
