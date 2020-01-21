package ru.itmo.cousre_work.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.cousre_work.Entities.Goal;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByAuthor(long author);
    Goal findById(long id);
    void removeById(long id);
    void removeByAuthor(long id);
}
