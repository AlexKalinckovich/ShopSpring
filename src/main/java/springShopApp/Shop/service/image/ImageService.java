package springShopApp.Shop.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import springShopApp.Shop.config.ProjectFilePaths;
import springShopApp.Shop.dto.ImageDTO;
import springShopApp.Shop.exceptions.ResourceNotFoundException;
import springShopApp.Shop.model.Image;
import springShopApp.Shop.model.Product;
import springShopApp.Shop.repository.ImageRepository;
import springShopApp.Shop.service.product.IProductService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        final Image image = getImageById(id);
        imageRepository.delete(image);
    }

    @Override
    public List<ImageDTO> saveImage(List<MultipartFile> files, Long productId) {
        final Product product = productService.getProductById(productId);
        final List<ImageDTO> savedImageDTOS = new ArrayList<>();

        for(final MultipartFile file : files) {
            try {
                final Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                final String downloadURL = ProjectFilePaths.IMAGE_DOWNLOAD_DIRECTORY + image.getId();
                image.setDownloadUrl(downloadURL);

                final Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(ProjectFilePaths.IMAGE_DOWNLOAD_DIRECTORY + savedImage.getId());

                final ImageDTO imageDTO = new ImageDTO();
                imageDTO.setImageId(savedImage.getId());
                imageDTO.setImageName(savedImage.getFileName());
                imageDTO.setDownloadUrl(savedImage.getDownloadUrl());

                savedImageDTOS.add(imageDTO);
            }catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return savedImageDTOS;
    }

    @Override
    public void updateImage(MultipartFile file, Long id) {
        final Image image = getImageById(id);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
