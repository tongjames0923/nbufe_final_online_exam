package tbs.api_server.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tbs.api_server.objects.jpa.ExamUser;

import java.util.List;

@Repository
public interface ExamerMapper extends CrudRepository<ExamUser,String> {

    @Query("select u from ExamUser u where u.examid=:eid and u.name=:name and u.id=:id and u.number=:number")
    ExamUser findOne(int eid,String name,String id,String number);

    List<ExamUser> findAllByExamid(int eid);
}
