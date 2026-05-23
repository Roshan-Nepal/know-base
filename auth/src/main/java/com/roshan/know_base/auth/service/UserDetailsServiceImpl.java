package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.entity.User;
import com.roshan.know_base.auth.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
         User user =  userRepo.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        Collection<GrantedAuthority> roles = user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());;
         return org.springframework.security.core.userdetails.User.builder()
                 .username(user.getUsername())
                 .password(user.getPassword())
                 .authorities(roles)
                 .build();

    }
}
