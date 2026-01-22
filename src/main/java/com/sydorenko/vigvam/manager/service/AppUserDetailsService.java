//package com.sydorenko.vigvam.manager.service;
//
//import com.sydorenko.vigvam.manager.persistence.repository.UserRepository;
//import lombok.*;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@RequiredArgsConstructor
//public class AppUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @NonNull
//    @Override
//    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
//        return (UserDetails) userRepository.findByUserLogin(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено: " + username));
//    }
//}
