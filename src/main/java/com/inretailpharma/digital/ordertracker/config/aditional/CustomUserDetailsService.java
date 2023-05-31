package com.inretailpharma.digital.ordertracker.config.aditional;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.service.user.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Qualifier("trackerUserService")
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid User");
        } else {
            Set<GrantedAuthority> grantedAuthorities = user.getRoleList()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getCode().name()))
                    .collect(Collectors.toSet());
            return new org
                    .springframework
                    .security
                    .core
                    .userdetails
                    .User(user.getEmail(), user.getId(), grantedAuthorities);
        }
    }
}
