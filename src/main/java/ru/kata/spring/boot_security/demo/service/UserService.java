package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<User> allUsers() {
        return userRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Optional<User> show(long id) {
        return userRepository.findById(id);
    }


    @Transactional
    public void save(User user) {
        userRepository.saveAndFlush(user);
    }


    @Transactional
    public void update(long id, User updatedUser) {

        User userToBeUpdated = userRepository.findById(id).get();
        userToBeUpdated = updatedUser;
        userRepository.save(userToBeUpdated);

    }


    @Transactional
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public List<User> findAll() {
       return userRepository.findAll();
    }

    @Transactional
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}