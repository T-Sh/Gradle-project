package ru.itmo.cousre_work.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itmo.cousre_work.Entities.Role;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.UserRepository;

import java.util.Comparator;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('USER')")
public class RatingController {

    private final UserRepository userRepository;

    public RatingController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/rating")
    public String rating(Model model) {

        List<User> userList = userRepository.findByRoles(Role.USER);
        userList.sort(Comparator.comparing(User::getCoins).reversed());

        model.addAttribute("users", userList);

        return "rating";
    }

}
