package com.coinlift.backend.services.users;

import com.coinlift.backend.entities.MyUserDetails;
import com.coinlift.backend.entities.User;
import com.coinlift.backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by their email during the authentication process.
     *
     * @param email The email of the user to load.
     * @return A custom implementation of the UserDetails interface, containing the user details.
     * @throws UsernameNotFoundException if the user with the provided email is not found in the database.
     */
    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new MyUserDetails(user);
    }
}
