package ru.itmo.cousre_work.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.cousre_work.Entities.Comment;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthor(long id);
    List<Comment> findByGoal(long goal);
    void removeById(long id);
    void removeByGoal(long goal);
    void removeByAuthor(long id);
}
