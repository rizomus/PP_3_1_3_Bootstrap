package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;

@Controller
public class MainController {

    private UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";


    }
    @GetMapping("/admin")
    public String adminPage(Principal principal, Model model) {

        User admin = userService.findByUsername(principal.getName());
        model.addAttribute("admin", admin);

        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }


    @PostMapping("admin/create")
    public String create(@RequestParam String username, @RequestParam int age, @RequestParam String password, @RequestParam String email,  @RequestParam Collection roles) {

        if (userService.findByUsername(username) != null) {
            return "redirect:/admin";
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setAge(age);
        newUser.setPassword(password);
        newUser.setEmail(email);
        List<Role> rollList = new ArrayList<Role>();
        for (Object role: roles) {
            rollList.add(new Role((String)role));
        }
        newUser.setRoles(rollList);

        userService.save(newUser);
        return "redirect:/admin";
    }


    @PatchMapping("admin/edit/{id}")
    public String update(@PathVariable int id, @RequestParam String username, @RequestParam int age, @RequestParam String password, @RequestParam String email,  @RequestParam Collection roles) {

        User updatedUser = userService.findById(id).get();
        updatedUser.setUsername(username);
        updatedUser.setAge(age);
        updatedUser.setPassword(password);
        updatedUser.setEmail(email);
        List<Role> rollList = new ArrayList<Role>();
        for (Object role: roles) {
            rollList.add(new Role((String)role));
        }
        updatedUser.setRoles(rollList);
        userService.update(id, updatedUser);

        return "redirect:/admin";
    }


    @PatchMapping("admin/delete/{id}")
    public String delete(@PathVariable long id) {

        userService.deleteById(id);
        return "redirect:/admin";
    }

}
