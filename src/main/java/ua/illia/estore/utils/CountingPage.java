package ua.illia.estore.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
public class CountingPage<T> extends PageImpl<T> {

    @Getter
    private final Map<String, Object> rangeLimits;

    public CountingPage(List<T> content, Pageable pageable, long total, Map<String, Object> rangeLimits) {
        super(content, pageable, total);
        this.rangeLimits = rangeLimits;
    }

    @Override
    public <U> CountingPage<U> map(Function<? super T, ? extends U> converter) {
        return new CountingPage<>(getConvertedContent(converter), getPageable(), getTotalElements(), rangeLimits);
    }
}
