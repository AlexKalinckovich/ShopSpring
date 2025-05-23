package springShopApp.Shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springShopApp.Shop.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
