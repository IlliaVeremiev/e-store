package ua.illia.estore.model.security.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StoreAuthority implements GrantedAuthority {
    ;

    @JsonProperty
    private final String description;

    StoreAuthority(String description) {
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return name();
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty
    public String getName() {
        return name();
    }
}
