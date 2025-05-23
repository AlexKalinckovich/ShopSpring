package springShopApp.Shop.request;

import lombok.Data;
import springShopApp.Shop.model.Category;
import springShopApp.Shop.model.Image;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateProductRequest {

    private Long id;

    private String name;
    private String brand;
    private String description;

    private BigDecimal price;
    private int inventory;

    private Category category;

    private List<Image> images;

}
