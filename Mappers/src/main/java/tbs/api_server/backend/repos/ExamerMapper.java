package tbs.api_server.backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tbs.api_server.objects.jpa.ExamUser;

import java.util.List;

@Repository
public interface ExamerMapper extends CrudRepository<ExamUser,String> {

    @Query("select u from ExamUser u where u.examid=:eid and u.uid=:uid")
    ExamUser findOne(int eid,String uid);

    List<ExamUser> findAllByExamid(int eid);

    ExamUser findExamUserByExamidAndUid(int eid,String uid);


    int countByExamid(int exam);
}
