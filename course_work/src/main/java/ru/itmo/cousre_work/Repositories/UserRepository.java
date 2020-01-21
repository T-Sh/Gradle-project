package ru.itmo.cousre_work.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.cousre_work.Entities.Role;
import ru.itmo.cousre_work.Entities.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByNickname(String name);
    List<User> findByRoles(Role role);
    void removeById(long id);
}
