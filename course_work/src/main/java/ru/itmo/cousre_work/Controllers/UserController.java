package ru.itmo.cousre_work.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.cousre_work.Entities.Role;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.CommentRepository;
import ru.itmo.cousre_work.Repositories.UserRepository;
import ru.itmo.cousre_work.Services.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public UserController(UserService userService,
                          UserRepository userRepository,
                          CommentRepository commentRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/users")
    public String user_list(Model model) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("users", userRepository.findAll());
        return "user_list";
    }

    @PostMapping("/filter_user")
    public String find_user(@RequestParam(name = "filter", required = false) String filter, Model model) {
        if (filter == null || filter.isEmpty()) {
            return "redirect:/users";
        } else {
            model.addAttribute("roles", Role.values());
            model.addAttribute("users", userService.find_by_filter(filter));
            return "user_list";
        }
    }

    @PostMapping("/edit_user")
    public String save_user(@RequestParam Map<String, String> form, @RequestParam(name = "userId") String userId) {
        User user = userRepository.findById(Long.parseLong(userId)).get();
        final Set<String> roles = Arrays.stream(Role.values()).
                map(Role::name).
                collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("/delete_comment")
    public String delete_comment(@RequestParam(name = "comment") String commentId,
                                 Model model) {
        long id = Long.parseLong(commentId);

        long goal = commentRepository.findById(id).get().getGoal();

        commentRepository.removeById(id);

        return "redirect:/comments?goalId=" + goal;
    }
}
