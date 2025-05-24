package springShopApp.Shop.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springShopApp.Shop.config.ResponseMessages;
import springShopApp.Shop.dto.ImageDTO;
import springShopApp.Shop.exceptions.ResourceNotFoundException;
import springShopApp.Shop.model.Image;
import springShopApp.Shop.response.ApiResponse;
import springShopApp.Shop.service.image.IImageService;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping({"${api.prefix}/images"})
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,
                                                  @RequestParam Long productId) {
        ResponseEntity<ApiResponse> response;
        try {
            List<ImageDTO> imageDTOS = imageService.saveImage(files, productId);
            response = ResponseEntity.ok(new ApiResponse(ResponseMessages.UPLOAD_SUCCESS, imageDTOS));
        }catch (Exception e) {
            response = ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ResponseMessages.UPLOAD_FAILURE, e.getMessage()));
        }

        return response;
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {

        final Image image = imageService.getImageById(imageId);
        final Blob imageBlob = image.getImage();

        final int resourcePos = 1;
        final int imageLength = (int) imageBlob.length();

        final ByteArrayResource imageResource = new ByteArrayResource(
                imageBlob.getBytes(resourcePos, imageLength)
        );
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(imageResource);
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        final Image image = imageService.getImageById(imageId);
        ResponseEntity<ApiResponse> response = ResponseEntity.status(INTERNAL_SERVER_ERROR)
                                                             .body(new ApiResponse(ResponseMessages.UPLOAD_FAILURE,null));
        try {
            if(image != null){
                imageService.updateImage(file, imageId);
                response = ResponseEntity.ok(new ApiResponse(ResponseMessages.UPLOAD_SUCCESS, null));
            }
        } catch (ResourceNotFoundException e) {
            response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return response;
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        final Image image = imageService.getImageById(imageId);
        ResponseEntity<ApiResponse> response = ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(ResponseMessages.DELETE_FAILURE,null));
        try {
            if(image != null){
                imageService.deleteImageById(imageId);
                response = ResponseEntity.ok(new ApiResponse(ResponseMessages.DELETE_SUCCESS, null));
            }
        } catch (ResourceNotFoundException e) {
            response = ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

        return response;
    }

}
