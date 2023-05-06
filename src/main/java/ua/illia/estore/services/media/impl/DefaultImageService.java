package ua.illia.estore.services.media.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.util.Asserts;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.illia.estore.configuration.exceptions.NotFoundException;
import ua.illia.estore.model.product.Image;
import ua.illia.estore.repositories.ImageRepository;
import ua.illia.estore.services.media.ImageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class DefaultImageService implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Image create(String path) {
        Image image = new Image();
        image.setPath(path);
        return imageRepository.save(image);
    }

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Image loadToCloudinary(MultipartFile imageMF) {
        try {
            String extension = FilenameUtils.getExtension(imageMF.getOriginalFilename());
            Asserts.notNull(extension, "file.without.extension");
            BufferedImage resizedImage = resizeImage(imageMF, 551, 630);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, extension, baos);
            byte[] bytes = baos.toByteArray();
            Map map = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
            Image image = new Image();
            image.setPath(map.get("secure_url").toString());
            image.setExternalId(map.get("public_id").toString());
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void deleteCloudinaryImage(Image image) {
        try {
            Map map = cloudinary.uploader().destroy(image.getExternalId(), ObjectUtils.emptyMap());
            if ("ok".equals(map.get("result"))) {
                imageRepository.delete(image);
            } else {
                //TODO Log that image was not removed from repository and cloudinary
                throw new NotFoundException("Cloudinary file was not found for image: " + image.getId(), "cloudinary.image.id");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Image image) {
        imageRepository.save(image);
    }

    @Override
    public Image create(MultipartFile image) {
        return loadToCloudinary(image);
    }

    @Override
    public void deleteImage(long imageId) {
        deleteCloudinaryImage(getById(imageId));
    }

    @Override
    public Image getById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Image with id: " + id + " not found", "image.id"));
    }

    @Override
    public BufferedImage resizeImage(MultipartFile imageFile, int maxWidth, int maxHeight) {
        try {
            BufferedImage image = ImageIO.read(imageFile.getInputStream());
            return Scalr.resize(image, maxHeight);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
