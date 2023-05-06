package ua.illia.estore.model.security.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Authority implements GrantedAuthority {
    ;

    @JsonProperty
    private final Area area;

    @JsonProperty
    private final String permission;

    @JsonProperty
    private final String description;

    Authority(Area area, String permission, String description) {
        this.area = area;
        this.permission = permission;
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return name();
    }

    public Area getArea() {
        return area;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty
    public String getName() {
        return name();
    }

    enum Area {
        AUTHORITIES,
        PRODUCTS,
        EMPLOYEES,
        WAREHOUSES,
        GROUPS,
        STORES
    }
}
