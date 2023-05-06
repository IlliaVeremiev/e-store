package ua.illia.estore.dto.role;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class GroupCreateForm {

    @NotEmpty
    private String name;

    @NotEmpty
    private String uid;

    @NotEmpty
    private String type;

    private String description;

    private List<String> authorities;
}
