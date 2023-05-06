package ua.illia.estore.dto.category;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
public class CategoryCreateForm {

    @NotEmpty
    private Map<String, String> localizedName;

    @NotEmpty
    private String uid;

    private boolean folder;

    private Long parentId;
}
