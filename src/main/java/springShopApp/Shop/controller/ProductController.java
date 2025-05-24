package springShopApp.Shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springShopApp.Shop.config.ResponseMessages;
import springShopApp.Shop.exceptions.ResourceNotFoundException;
import springShopApp.Shop.model.Product;
import springShopApp.Shop.request.AddProductRequest;
import springShopApp.Shop.request.UpdateProductRequest;
import springShopApp.Shop.response.ApiResponse;
import springShopApp.Shop.service.product.IProductService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping({"${api.prefix}/products"})
public class ProductController {

    private final IProductService productService;

    @GetMapping("/getAllProducts")
    public ResponseEntity<ApiResponse> getAllProducts() {
        final List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, products));
    }

    @GetMapping("/getProductId/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ResponseEntity<ApiResponse> response;
        try {
            final Product product = productService.getProductById(id);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, product));
        } catch (ResourceNotFoundException e) {
            response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return response;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        ResponseEntity<ApiResponse> response;
        try {
            final Product addedProduct = productService.addProduct(product);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, addedProduct));
        } catch (RuntimeException e) {
            response = ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }

        return response;
    }


    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest product, Long id) {
        ResponseEntity<ApiResponse> response;
        try{
            final Product updatedProduct = productService.updateProduct(product, id);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, updatedProduct));
        }catch(ResourceNotFoundException e){
            response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return response;
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        ResponseEntity<ApiResponse> response;
        try {
            productService.deleteProductById(id);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, null));
        }catch (ResourceNotFoundException e){
            response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return response;
    }

    @GetMapping("/getProductByBrandAndName/{brand}/{name}")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@PathVariable String brand, @PathVariable String name) {
        ResponseEntity<ApiResponse> response;
        final List<Product> products = productService.getProductsByBrandAndName(brand, name);

        try {
            if(products.isEmpty()){
                response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(ResponseMessages.SUCCESS, null));
            }else{
                response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, products));
            }
        } catch (RuntimeException e) {
            response = ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }

        return response;

    }
}
