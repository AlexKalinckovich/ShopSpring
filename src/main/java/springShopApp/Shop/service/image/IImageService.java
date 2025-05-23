package springShopApp.Shop.service.image;

import org.springframework.web.multipart.MultipartFile;
import springShopApp.Shop.dto.ImageDTO;
import springShopApp.Shop.model.Image;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageDTO> saveImage(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, Long id);

}
