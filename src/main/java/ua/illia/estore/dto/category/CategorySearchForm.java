package ua.illia.estore.dto.category;

import lombok.Data;

@Data
public class CategorySearchForm {
    private String query;

    private Long parentId;

    private Boolean folder;
}
