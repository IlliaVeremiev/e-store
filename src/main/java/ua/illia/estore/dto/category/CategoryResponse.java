package ua.illia.estore.dto.category;

import lombok.Data;
import ua.illia.estore.dto.image.ImageResponse;

import java.util.List;
import java.util.Map;

@Data
public class CategoryResponse {

    private long id;

    private String uid;

    private String name;

    private Map<String, String> localizedName;

    private ImageResponse image;

    private ImageResponse icon;

    private boolean folder;

    private Long parentId;

    private List<CategoryClassificationParameterResponse> classificationParameters;
}
