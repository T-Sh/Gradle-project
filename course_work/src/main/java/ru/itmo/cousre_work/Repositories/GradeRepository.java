package ru.itmo.cousre_work.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.cousre_work.Entities.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Grade findByUserAndGoal(long user, long goal);
}
