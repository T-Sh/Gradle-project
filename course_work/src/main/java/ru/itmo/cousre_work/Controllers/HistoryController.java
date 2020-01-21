package ru.itmo.cousre_work.Controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itmo.cousre_work.Entities.History;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.CostRepository;
import ru.itmo.cousre_work.Repositories.HistoryRepository;

import java.util.Comparator;
import java.util.List;

@Controller
public class HistoryController {

    private final HistoryRepository historyRepository;
    private final CostRepository costRepository;


    public HistoryController(HistoryRepository historyRepository,
                             CostRepository costRepository) {
        this.historyRepository = historyRepository;
        this.costRepository = costRepository;
    }

    @GetMapping("/history")
    public String show_history(Model model) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        List<History> historyList = historyRepository.findByUserId(user.getId());
        historyList.sort(Comparator.comparing(History::getDate).reversed());

        model.addAttribute("history", historyList);
        model.addAttribute("cost", costRepository);

        return "history";
    }

}
