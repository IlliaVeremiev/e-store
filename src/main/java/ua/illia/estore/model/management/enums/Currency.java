package ua.illia.estore.model.management.enums;

import lombok.Getter;

@Getter
public enum Currency {
    USD("USD", "US dollar", "United States dollar", "$"),
    UAH("UAH", "UA hryvnia", "Ukraine hryvnia", "₴");

    private final String code;

    private final String shortName;

    private final String name;

    private final String character;

    Currency(String code, String shortName, String name, String character) {
        this.code = code;
        this.shortName = shortName;
        this.name = name;
        this.character = character;
    }
}
