package ua.illia.estore.services.media;

import org.springframework.web.multipart.MultipartFile;
import ua.illia.estore.model.product.Image;

import java.awt.image.BufferedImage;

public interface ImageService {

    Image create(String path);

    Image loadToCloudinary(MultipartFile image);

    void save(Image image);

    Image create(MultipartFile image);

    void deleteImage(long imageId);

    Image getById(long id);

    BufferedImage resizeImage(MultipartFile imageFile, int maxWidth, int maxHeight);
}
