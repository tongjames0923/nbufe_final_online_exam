package tbs.api_server.objects.jpa.Repo.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.support.PageableExecutionUtils;
import tbs.api_server.objects.jpa.Repo.CustomPagingWithoutCountRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.function.LongSupplier;

@NoRepositoryBean
public class CustomPagingWithoutCountRepositoryImpl<T,ID extends Serializable>extends SimpleJpaRepository<T,ID>  implements CustomPagingWithoutCountRepository<T,ID> {

    public CustomPagingWithoutCountRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    protected <S extends T> Page<S> readPageWithoutCount(TypedQuery<S> query, Class<S> domainClass, Pageable pageable, Specification<S> spec) {
        if (pageable.isPaged()) {
            query.setFirstResult((int)pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return PageableExecutionUtils.getPage(query.getResultList(), pageable, new LongSupplier() {
            @Override
            public long getAsLong() {
                return -1;
            }
        });
    }

    @Override
    public  Page<T> findAllWithoutTotal(Specification<T> spec, Pageable pageable) {
        TypedQuery<T> query = super.getQuery(spec, pageable);
        return (Page)(pageable.isUnpaged() ? new PageImpl(query.getResultList()) : this.readPageWithoutCount(query, this.getDomainClass(), pageable, spec));
    }
}
