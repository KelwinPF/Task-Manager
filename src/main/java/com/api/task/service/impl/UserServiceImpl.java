package com.api.task.service.impl;
import com.api.task.entity.User;
import com.api.task.repository.UserRepository;
import com.api.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repo;

    @Override
    public User save(User u){
        return repo.save(u);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return repo.findByEmail(email);
    }
}

