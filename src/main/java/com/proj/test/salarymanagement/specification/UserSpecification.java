package com.proj.test.salarymanagement.specification;

import com.proj.test.salarymanagement.dto.SearchFilter;
import com.proj.test.salarymanagement.dto.SearchRequestDto;
import com.proj.test.salarymanagement.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSpecification implements Specification<UserEntity> {

    private List<SearchFilter> filters;

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        for (SearchFilter filter : this.filters){
            // We can add multiple Filter Operation here.
            if("IN".equals(filter.getFilterOperation())){
                predicates.add(criteriaBuilder.in(root.get(filter.getFilterName())).value(filter.getFilterValues()));
            }
            if("GREATER_THAN".equals(filter.getFilterOperation())){
                predicates.add(criteriaBuilder.greaterThan(
                        root.get(filter.getFilterName()), filter.getFilterValue1()));
            }
            if("LESS_THAN".equals(filter.getFilterOperation())){
                predicates.add(criteriaBuilder.lessThan(
                        root.get(filter.getFilterName()), filter.getFilterValue1()));
            }
            if("GREATER_THAN_EQUAL".equals(filter.getFilterOperation())){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get(filter.getFilterName()), filter.getFilterValue1()));
            }
            if("BETWEEN".equals(filter.getFilterOperation())){
                predicates.add(criteriaBuilder.between(
                        root.get(filter.getFilterName()),filter.getFilterValue1(),filter.getFilterValue2()));
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
