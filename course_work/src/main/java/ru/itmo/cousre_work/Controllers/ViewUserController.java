package ru.itmo.cousre_work.Controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.cousre_work.Entities.Goal;
import ru.itmo.cousre_work.Entities.Grade;
import ru.itmo.cousre_work.Entities.History;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.*;
import ru.itmo.cousre_work.Services.UserService;


@Controller
public class ViewUserController {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final CostRepository costRepository;
    private final HistoryRepository historyRepository;
    private final GradeRepository gradeRepository;

    public ViewUserController(UserRepository userRepository,
                              GoalRepository goalRepository,
                              CostRepository costRepository,
                              HistoryRepository historyRepository,
                              GradeRepository gradeRepository) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.gradeRepository = gradeRepository;
        this.costRepository = costRepository;
        this.historyRepository = historyRepository;
    }

    @GetMapping("/userId")
    public String user_view(@RequestParam(name = "userId") String userId,
                            Model model) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        long id = Long.parseLong(userId);

        if (user.getId() == id) {
            return "redirect:/main";
        }

        model.addAttribute("goals", goalRepository.findByAuthor(id));
        model.addAttribute("user", userRepository.findById(id).get());
        model.addAttribute("gradeRepo", gradeRepository);
        model.addAttribute("history", historyRepository);

        return "user_view";
    }

    @PostMapping("/grade")
    public String grade(@RequestParam(name = "grade") int score,
                        @RequestParam(name = "goalId") String goalId,
                        @RequestParam(name = "userId") String userId) {

        User current_user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        Goal goal = goalRepository.findById(Long.parseLong(goalId));
        User user = userRepository.findById(Long.parseLong(userId)).get();

        if (gradeRepository.findByUserAndGoal(current_user.getId(), goal.getId()) != null) {

            History history = historyRepository.findById(
                    gradeRepository.findByUserAndGoal(current_user.getId(), goal.getId()).getHistory());

            user.setCoins(score - history.getCoins());
            userRepository.save(user);

            history.setCoins((byte) score);
            historyRepository.save(history);

        } else {

            user.setCoins(score);
            userRepository.save(user);

            History history = new History(user.getId(), costRepository.findById(9));
            history.setCoins((byte) score);
            historyRepository.save(history);

            gradeRepository.save(new Grade(history.getId(), current_user.getId(), goal.getId()));

        }

        return "redirect:/userId?userId=" + userId;
    }
}
