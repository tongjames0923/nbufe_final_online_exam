package tbs.api_server.backend.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tbs.api_server.objects.jpa.ExamCheck_Entity;

@Repository
public interface ExamCheckMapper extends CrudRepository<ExamCheck_Entity,Integer> {
    ExamCheck_Entity findFirstByQuesIdAndExamerAndExamId(int qid,String examer,int eid);
}
