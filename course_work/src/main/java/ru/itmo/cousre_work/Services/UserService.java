package ru.itmo.cousre_work.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.cousre_work.Entities.Goal;
import ru.itmo.cousre_work.Entities.History;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.CostRepository;
import ru.itmo.cousre_work.Repositories.HistoryRepository;
import ru.itmo.cousre_work.Repositories.UserRepository;
import sun.security.krb5.internal.ccache.FileCredentialsCache;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CostRepository costRepository;
    private final HistoryRepository historyRepository;

    public UserService(UserRepository userRepository,
                       CostRepository costRepository,
                       HistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.costRepository = costRepository;
        this.historyRepository = historyRepository;
    }

    public List<User> loadAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User loadUserById(long id) {
        return userRepository.findById(id).get();
    }

    public Set<User> find_by_filter(String filter) {
        Set<User> userSet = new HashSet<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            if (user.getNickname().contains(filter)) {
                userSet.add(user);
            }
        }
        return userSet;
    }

    public void saveCoins(User user, int num) {
        user.setCoins(costRepository.findById(num).getCost());
        userRepository.save(user);

        historyRepository.save(new History(user.getId(), costRepository.findById(num)));
    }

    public void saveCoins(User user, int num, Goal goal) {

        Date current = new Date();

        long coins = costRepository.findById(4).getCost() -
                costRepository.findById(8).getCost() *
                        ((current.getTime() - goal.getSetTime().getTime())
                                / (24 * 60 * 60 * 1000) + 1);

        user.setCoins(coins);
        userRepository.save(user);
        historyRepository.save(new History(user.getId(), costRepository.findById(4)));
    }

}
