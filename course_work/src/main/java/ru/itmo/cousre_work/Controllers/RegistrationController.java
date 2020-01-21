package ru.itmo.cousre_work.Controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.cousre_work.Entities.History;
import ru.itmo.cousre_work.Entities.Role;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.CostRepository;
import ru.itmo.cousre_work.Repositories.HistoryRepository;
import ru.itmo.cousre_work.Repositories.UserRepository;

import java.util.Collections;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final CostRepository costRepository;
    private final HistoryRepository historyRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository,
                                  CostRepository costRepository,
                                  HistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.costRepository = costRepository;
        this.historyRepository = historyRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(8);
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User username = userRepository.findByUsername(user.getUsername());

        if (username != null) {
            model.addAttribute("mes", "Этот логин уже используется!");
            return "registration";
        }

        User email = userRepository.findByEmail(user.getEmail());

        if (email != null) {
            model.addAttribute("mes", "Эта электронная почта уже используется!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        user.setCoins(costRepository.findById(1).getCost());
        userRepository.save(user);
        historyRepository.save(new History(user.getId(), costRepository.findById(1)));

        return "redirect:/login";
    }
}
