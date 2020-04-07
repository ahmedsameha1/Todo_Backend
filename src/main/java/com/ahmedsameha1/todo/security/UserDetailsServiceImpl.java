package com.ahmedsameha1.todo.security;

import com.ahmedsameha1.todo.domain_model.UserAccount;
import com.ahmedsameha1.todo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserAccountRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userRepository.findByUsername(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException(username);
        }
        return userAccount;
    }
}
