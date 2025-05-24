package springShopApp.Shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springShopApp.Shop.config.ResponseMessages;
import springShopApp.Shop.exceptions.AlreadyExistsException;
import springShopApp.Shop.exceptions.ResourceNotFoundException;
import springShopApp.Shop.model.Category;
import springShopApp.Shop.response.ApiResponse;
import springShopApp.Shop.service.category.ICategoryService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping({"${api.prefix}/categories"})
public class CategoryController {

    private final ICategoryService categoryService;

    public ResponseEntity<ApiResponse> getAllCategories() {
        ResponseEntity<ApiResponse> response;
        try {
            List<Category> categories = categoryService.getAllCategories();
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.FOUND, categories));
        }catch (Exception e) {
            response = ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(ResponseMessages.FAILURE, INTERNAL_SERVER_ERROR.value()));
        }
        return response;
    }

    @PostMapping("/addCategory")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        ResponseEntity<ApiResponse> response;
        try {
            final Category addedCategory = categoryService.addCategory(category);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, addedCategory));
        }catch (AlreadyExistsException e){
            response = ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
        return response;
    }

    @GetMapping("/categoryId/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        ResponseEntity<ApiResponse> response;
        try {
            final Category category = categoryService.getCategoryById(id);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, category));
        }catch (ResourceNotFoundException e){
            response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return response;
    }

    @GetMapping("/categoryName/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        ResponseEntity<ApiResponse> response;
        try {
            final Category category = categoryService.getCategoryByName(name);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, category));
        }catch (ResourceNotFoundException e){
            response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return response;
    }

    @DeleteMapping("categoryId/{id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
        ResponseEntity<ApiResponse> response;
        try{
            categoryService.deleteCategory(id);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.SUCCESS, null));
        }catch (ResourceNotFoundException e){
            response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return response;
    }

}
