package ru.itmo.cousre_work.Controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.cousre_work.Entities.Comment;
import ru.itmo.cousre_work.Entities.History;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.CommentRepository;
import ru.itmo.cousre_work.Repositories.CostRepository;
import ru.itmo.cousre_work.Repositories.HistoryRepository;
import ru.itmo.cousre_work.Repositories.UserRepository;

import java.util.Comparator;
import java.util.List;

@Controller
public class CommentController {

    private final CommentRepository commentRepository;
    private final CostRepository costRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    public CommentController(CommentRepository commentRepository, CostRepository costRepository, UserRepository userRepository, HistoryRepository historyRepository) {
        this.commentRepository = commentRepository;
        this.costRepository = costRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }

    @GetMapping("/comments")
    public String comments_list(@RequestParam(name = "goalId") String goalId,
                                Model model) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        long id = Long.parseLong(goalId);

        List<Comment> comments = commentRepository.findByGoal(id);
        comments.sort(Comparator.comparing(Comment::getDateOfCreation).reversed());

        model.addAttribute("comments", comments);
        model.addAttribute("goal", goalId);
        model.addAttribute("userRepo", userRepository);
        model.addAttribute("check", user.isAdmin());
        return "comments";
    }

    @PostMapping("/create_comment")
    public String create(@RequestParam(name = "text", required = false) String text,
                         @RequestParam(name = "goalId") String goalId) {

        if (text != null || !text.isEmpty()) {
            User user = (User) SecurityContextHolder.
                    getContext().
                    getAuthentication().
                    getPrincipal();

            user.setCoins(costRepository.findById(2).getCost());
            userRepository.save(user);

            historyRepository.save(new History(user.getId(), costRepository.findById(2)));

            commentRepository.save(new Comment(user.getId(), Long.parseLong(goalId), text));
        }

        return "redirect:/comments?goalId=" + goalId;
    }

}
