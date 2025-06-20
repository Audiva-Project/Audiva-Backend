package com.example.audiva.service;

import com.example.audiva.entity.Role;
import com.example.audiva.entity.User;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<Role> role = user.getRoles();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.toString());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Arrays.asList(grantedAuthority));
        return userDetails;
    }
}
