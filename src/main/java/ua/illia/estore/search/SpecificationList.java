package ua.illia.estore.search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SpecificationList<E> implements Specification<E> {

    private final List<Specification<E>> specifications = new ArrayList<>();

    @Override
    public Predicate toPredicate(@NonNull Root<E> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        return specifications.stream()
                .reduce(Specification::and)
                .orElse((r, q, c) -> c.conjunction())
                .toPredicate(root, query, criteriaBuilder);
    }

    public void add(Specification<E> specification) {
        specifications.add(specification);
    }

    public int size() {
        return specifications.size();
    }
}
