package ru.itmo.cousre_work.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.cousre_work.Entities.CostOfEvent;

public interface CostRepository extends JpaRepository<CostOfEvent, Long> {
    CostOfEvent findById(long id);
}
