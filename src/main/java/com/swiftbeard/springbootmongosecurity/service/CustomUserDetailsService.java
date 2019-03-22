package com.swiftbeard.springbootmongosecurity.service;

import com.swiftbeard.springbootmongosecurity.domain.Role;
import com.swiftbeard.springbootmongosecurity.domain.User;
import com.swiftbeard.springbootmongosecurity.repository.RoleRepository;
import com.swiftbeard.springbootmongosecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void saveuser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if(user != null){
            List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
            return builderUserForAuthentication(user, authorities);
        }else {
            throw new UsernameNotFoundException(("username not found"));
        }
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles){

        Set<GrantedAuthority> roles = new HashSet<>();

        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails builderUserForAuthentication(User user, List<GrantedAuthority> authorities){

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
