package ru.itmo.cousre_work.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.cousre_work.Entities.Friendship;
import ru.itmo.cousre_work.Entities.User;
import ru.itmo.cousre_work.Repositories.FriendshipRepository;
import ru.itmo.cousre_work.Services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@PreAuthorize("hasAuthority('USER')")
public class FriendsController {

    private final UserService userService;
    private final FriendshipRepository friendshipRepository;

    public FriendsController(UserService userService,
                             FriendshipRepository friendshipRepository) {
        this.userService = userService;
        this.friendshipRepository = friendshipRepository;
    }

    @GetMapping("/list_of_friends")
    public String list(Model model) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        Set<User> userSet = new HashSet<>();

        List<Friendship> friendships = friendshipRepository.findByUserId(user.getId());
        for (Friendship friendship : friendships) {
            userSet.add(userService.loadUserById(friendship.getFriendId()));
        }

        friendships = friendshipRepository.findByFriendId(user.getId());
        for (Friendship friendship : friendships) {
            userSet.add(userService.loadUserById(friendship.getUserId()));
        }

        model.addAttribute("friends", userSet);
        return "list_of_friends";
    }

    @PostMapping("/filter_friends")
    public String filter_friends(@RequestParam(name = "filter") String nickname,
            Model model
    ) {

        if (nickname == null || nickname.isEmpty()) {
            return "redirect:/list_of_friends";
        }

        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        Set<User> userSet = new HashSet<>();
        List<Friendship> friendships = friendshipRepository.findByUserIdOrFriendId(user.getId(), user.getId());

        for (Friendship friendship : friendships) {
            long userId = friendship.getUserId();
            long friendId = friendship.getFriendId();
            if (userId == user.getId()) {
                if (userService.loadUserById(friendId).getNickname().contains(nickname)) {
                    userSet.add(userService.loadUserById(friendId));
                }
            }
            if (friendId == user.getId()) {
                if (userService.loadUserById(userId).getNickname().contains(nickname)) {
                    userSet.add(userService.loadUserById(userId));
                }
            }
        }

        model.addAttribute("friends", userSet);
        return "list_of_friends";
    }

    @GetMapping("/find_new_friend")
    public String find_friends(Model model) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        model.addAttribute("users", userService.loadAll());
        model.addAttribute("current", user.getId());
        return "find_friends";
    }

    @PostMapping("/filter_new_friends")
    public String find(@RequestParam(name = "filter") String filter,
            Model model
    ) {
        if (filter == null || filter.isEmpty()) {
            return "redirect:/find_friends";
        }

        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        model.addAttribute("users", userService.find_by_filter(filter));
        model.addAttribute("current", user.getId());
        return "find_friends";
    }

    @GetMapping("/add_friend")
    public String add(@RequestParam(name = "userId") String userId) {
        User user = (User) SecurityContextHolder.
                getContext().
                getAuthentication().
                getPrincipal();

        long id = Long.parseLong(userId);

        if (id == user.getId() ||
                friendshipRepository.findByUserIdAndFriendId(user.getId(), id) != null ||
                friendshipRepository.findByUserIdAndFriendId(id, user.getId()) != null) {
            return "redirect:/list_of_friends";
        }

        friendshipRepository.save(new Friendship(user.getId(), id));

        return "redirect:/list_of_friends";
    }


    @GetMapping("/remove_friend")
    public String remove_friend(@RequestParam(name = "userId") String userId) {

        long id = Long.parseLong(userId);
        friendshipRepository.removeByUserIdOrFriendId(id, id);

        return "redirect:/list_of_friends";
    }
}
