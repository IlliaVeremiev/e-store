package ua.illia.estore.converters;

public interface EntityConverter<ENTITY, DTO> {
    <E extends DTO> E convert(ENTITY entity, E dto);

    <E extends DTO> E convert(ENTITY entity);
}
