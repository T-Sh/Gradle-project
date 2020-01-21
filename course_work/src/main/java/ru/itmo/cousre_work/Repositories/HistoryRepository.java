package ru.itmo.cousre_work.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.cousre_work.Entities.History;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface HistoryRepository extends JpaRepository<History, Long> {
    void removeByUserId(long id);
    List<History> findByUserId(long id);
    History findById(long id);
}
