package cnLabs.Micro.clientapplication.Service;

import cnLabs.Micro.clientapplication.Clients.UserDetailsClient;
import cnLabs.Micro.clientapplication.Model.CustomUserDetails;
import cnLabs.Micro.clientapplication.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserDetailsClient userDetailsClient;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDetailsClient.getUser(username);
//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        if (user != null) {
            return CustomUserDetails.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .password(user.getPassword())
                    .username(user.getUsername())
                    .build();
//            return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
//                    true, true, true, true, authorities);
        } else {
            return null;
        }
    }

    public void registerUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        User userResponse = userDetailsClient.registerUser(user);
    }

}
