package tbs.api_server.objects.jpa.Repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.function.LongSupplier;

@NoRepositoryBean
public interface CustomPagingWithoutCountRepository<T,ID extends Serializable>{
    public Page<T> findAllWithoutTotal(Specification<T> spec, Pageable pageable);
}
