package springShopApp.Shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


// It's not good to provide @Data annotation here
// because @Entity working with DB relationship and
// equals and hashcode overrides are not safe in @Data case
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private String description;

    private BigDecimal price;
    private int inventory;

    // When product deleted -> relationship between this product and category will be deleted
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;


    // When product will be deleted -> all images will be deleted, if image related to this product
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public Product(String name,
                   String brand,
                   BigDecimal price,
                   int inventory,
                   String description,
                   Category category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.category = category;
    }
}
