package tbs.logserver.backend.Specifications;

import org.springframework.data.jpa.domain.Specification;
import tbs.api_server.objects.simple.LogPojo;

import javax.persistence.criteria.*;

public class LogpageSpec implements Specification<LogPojo> {

    String v;
    String f;

    public LogpageSpec(String field, String val) {
        f = field;
        v = val;
    }

    @Override
    public Predicate toPredicate(Root<LogPojo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return query.where(criteriaBuilder.like(root.get(f), v + "%"))
                .orderBy(criteriaBuilder.desc(root.get("log_begin"))).getRestriction();
    }
}
