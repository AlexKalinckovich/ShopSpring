package springShopApp.Shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springShopApp.Shop.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findProductByName(String name);

    List<Product> findByBrandAndName(String brand, String name);

    Long countByBrandAndName(String brand, String name);

    Long countByBrand(String brand);

    Long countByName(String name);
}
