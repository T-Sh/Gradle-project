package ru.itmo.cousre_work.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.*;

import java.io.File;
import java.io.IOException;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final GoalRepository goalRepository;
    private final HistoryRepository historyRepository;
    private final FriendshipRepository friendshipRepository;
    private final PasswordEncoder passwordEncoder;

    public MainController(UserRepository userRepository,
                          CommentRepository commentRepository,
                          GoalRepository goalRepository,
                          HistoryRepository historyRepository,
                          FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.goalRepository = goalRepository;
        this.historyRepository = historyRepository;
        this.friendshipRepository = friendshipRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(8);
    }

    @GetMapping("/login")
    public String login() {
        return "greeting";
    }

    @GetMapping("/")
    public String main() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping("/edit_profile")
    public String edit() {
        return "edit_profile";
    }

    @PostMapping("/edit_profile_name")
    public String edit_name(@RequestParam(name = "nickname") String name) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        user.setNickname(name);
        userRepository.save(user);
        return "redirect:/main";
    }

    @PostMapping("/edit_profile_password")
    public String edit_password(@RequestParam(name = "previous_p") String previous_p, @RequestParam(name = "new_p") String new_p, Model model) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        if (passwordEncoder.matches(previous_p, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(new_p));
            userRepository.save(user);
            return "redirect:/main";
        } else {
            model.addAttribute("mes", "Неверный пароль!");
            return "edit_profile";
        }
    }

    @PostMapping("/edit_profile_image")
    public String edit_image(@RequestParam(name = "file", required = false) MultipartFile file) throws IOException {

        if (!(file == null) && !file.isEmpty()) {
            File uploadDir = new File(uploadPath);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            file.transferTo(new File(uploadPath + "/" + user.getId() + ".jpg"));
        }
        return "redirect:/main";
    }

    @PostMapping("/delete_image")
    public String delete_image() {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        File file = new File(uploadPath +"/" + user.getId() + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        return "redirect:/main";
    }

    @GetMapping("delete_user")
    public String delete_user() {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        delete_image();

        goalRepository.removeById(user.getId());
        historyRepository.removeByUserId(user.getId());
        friendshipRepository.removeByUserIdOrFriendId(user.getId(), user.getId());
        commentRepository.removeByAuthor(user.getId());
        userRepository.removeById(user.getId());

        return "greeting";
    }
}
