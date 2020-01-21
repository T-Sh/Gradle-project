package ru.itmo.cousre_work.Controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itmo.cousre_work.Entities.Goal;
import ru.itmo.cousre_work.Entities.History;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.*;
import ru.itmo.cousre_work.Services.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class GoalsController {
    private final GoalRepository goalRepository;
    private final CommentRepository commentRepository;
    private final CostRepository costRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final UserService userService;

    public GoalsController(GoalRepository goalRepository, CommentRepository commentRepository, CostRepository costRepository, UserRepository userRepository, HistoryRepository historyRepository, UserService userService) {
        this.goalRepository = goalRepository;
        this.commentRepository = commentRepository;
        this.costRepository = costRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.userService = userService;
    }

    @GetMapping("/create_goal")
    public String create() {
        return "create_goal";
    }

    @PostMapping("/create")
    public String create(@RequestParam(name = "text") String text,
            @RequestParam String setTime, Model model)
            throws Exception {

        if (text.isEmpty()) {
            model.addAttribute("mes", "Отсутствует текст!");
            return "create_goal";
        }

        if (setTime.isEmpty()) {
            model.addAttribute("mes", "Отсутствует дата!");
            return "create_goal";
        }

        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        Date current = new Date();

        if (current.getTime() >= ft.parse(setTime).getTime()) {
            return "redirect:/create_goal";
        }

        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        Goal goal = new Goal(text, user.getId(), ft.parse(setTime));
        goalRepository.save(goal);

        userService.saveCoins(user, 3);

        return "redirect:/watch_goals";
    }

    @GetMapping("/watch_goals")
    public String watch(Model model) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        List<Goal> goals = goalRepository.findByAuthor(user.getId());
        model.addAttribute("goals", goals);
        return "watch_goals";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(name = "goalId") String goalId) {

        if (!goalRepository.findById(Long.parseLong(goalId)).isDone()) {
            User user = (User) SecurityContextHolder.
                    getContext().
                    getAuthentication().
                    getPrincipal();

            goalRepository.removeById(Long.parseLong(goalId));
            commentRepository.removeByGoal(Long.parseLong(goalId));

            userService.saveCoins(user, 5);
        }

        return "redirect:/watch_goals";
    }

    @GetMapping("/edit_goal")
    public String edit(@RequestParam(name = "goalId") String goalId, Model model) {

        if (goalRepository.findById(Long.parseLong(goalId)).isDone()) {
            return "redirect:/watch_goals";
        }

        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        Goal goal = goalRepository.findById(Long.parseLong(goalId));

        if (goal.getAuthor() == user.getId()) {
            model.addAttribute("goal", goal);
            return "edit_goal";
        } else {
            return "redirect:/watch_goals";
        }
    }

    @PostMapping("/edit_goal")
    public String edit(@RequestParam(name = "goalId") String goalId,
                       @RequestParam(name = "text", required = false) String text,
                       @RequestParam(name = "setTime", required = false) String setTime)
            throws Exception {

        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        Goal goal = goalRepository.findById(Long.parseLong(goalId));

        if (user.getId() != goal.getAuthor()) {
            return "redirect:/watch_goals";
        }

        if (!text.isEmpty()) {
            goal.setText(text);
            goalRepository.save(goal);
        }

        if (!setTime.isEmpty()) {

            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
            Date newDate = ft.parse(setTime);
            Date current = new Date();

            if (current.getTime() >= ft.parse(setTime).getTime()) {
                return "redirect:/create_goal";
            }

            if (goal.getSetTime().compareTo(newDate) <= 0) {
                userService.saveCoins(user, 6);
            } else {
                userService.saveCoins(user, 7);
            }

            goal.setSetTime(setTime);
            goalRepository.save(goal);
        }

        return "redirect:/watch_goals";
    }

    @PostMapping("/do_goal")
    public String do_goal(@RequestParam(name = "goalId") String goalId,
                          @RequestParam(name = "evidence", required = false) String evidence) {

        if (evidence != null && !evidence.isEmpty()) {

            long id = Long.parseLong(goalId);
            Goal goal = goalRepository.findById(id);

            goal.done(evidence);
            goalRepository.save(goal);
            User user = userRepository.findById(goal.getAuthor()).get();

            Date current = new Date();

            if (goal.getSetTime().compareTo(current) <= 0) {
                userService.saveCoins(user, 4);
            } else {
                userService.saveCoins(user, 4, goal);
            }
        }

        return "redirect:/watch_goals";
    }

}
