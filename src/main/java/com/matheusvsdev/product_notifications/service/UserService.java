package com.matheusvsdev.product_notifications.service;

import com.matheusvsdev.product_notifications.domain.User;
import com.matheusvsdev.product_notifications.dto.CreateUserDTO;
import com.matheusvsdev.product_notifications.dto.ResponseUserDTO;
import com.matheusvsdev.product_notifications.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public ResponseUserDTO insertUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setFullName(createUserDTO.getFullName());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());

        return new ResponseUserDTO(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public Page<ResponseUserDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return users.map(ResponseUserDTO::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
