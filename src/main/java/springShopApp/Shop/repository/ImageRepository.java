package springShopApp.Shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springShopApp.Shop.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
