package ru.itmo.cousre_work.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.cousre_work.Entities.Friendship;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUserId(Long userId);
    List<Friendship> findByFriendId(Long friendId);
    Friendship findByUserIdAndFriendId(Long user, long friend);
    List<Friendship> findByUserIdOrFriendId(long user, long friend);
    void removeByUserIdOrFriendId(Long user, long friend);
}
