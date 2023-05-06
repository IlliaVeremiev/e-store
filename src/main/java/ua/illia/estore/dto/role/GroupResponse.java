package ua.illia.estore.dto.role;

import lombok.Data;

import java.util.List;

@Data
public class GroupResponse {

    private long id;

    private String name;

    private String description;

    private List<String> authorities;

    private String uid;

    private String type;

    private boolean system;
}
