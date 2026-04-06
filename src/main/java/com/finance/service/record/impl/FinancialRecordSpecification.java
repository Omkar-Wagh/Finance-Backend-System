package com.finance.service.record.impl;

import com.finance.entity.FinancialRecord;
import com.finance.enums.Category;
import com.finance.enums.RecordType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class FinancialRecordSpecification {

    public static Specification<FinancialRecord> search(String type, String category, Double minAmount, Double maxAmount) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (type != null && !type.isBlank()) {
                predicates.add(cb.equal(root.get("type"),
                        RecordType.valueOf(type.toUpperCase())));
            }

            if (category != null && !category.isBlank()) {
                predicates.add(cb.equal(root.get("category"),
                        Category.valueOf(category.toUpperCase())));
            }

            if (minAmount != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("amount"), minAmount));
            }

            if (maxAmount != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("amount"), maxAmount));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}