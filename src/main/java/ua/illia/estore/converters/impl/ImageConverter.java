package ua.illia.estore.converters.impl;

import org.springframework.stereotype.Component;
import ua.illia.estore.converters.EntityConverter;
import ua.illia.estore.dto.image.ImageResponse;
import ua.illia.estore.model.product.Image;
import ua.illia.estore.model.product.data.ProductImage;

@Component
public class ImageConverter implements EntityConverter<Image, ImageResponse> {

    @Override
    public <E extends ImageResponse> E convert(Image image, E dto) {
        dto.setId(image.getId());
        dto.setPath(image.getPath());
        return dto;
    }

    @Override
    public ImageResponse convert(Image image) {
        return convert(image, new ImageResponse());
    }

    public ImageResponse convert(ProductImage image) {
        ImageResponse dto = new ImageResponse();
        dto.setId(image.getImageId());
        dto.setPath(image.getPath());
        return dto;
    }
}
