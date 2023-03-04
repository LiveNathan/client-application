package cnLabs.Micro.clientapplication.Clients;

import cnLabs.Micro.clientapplication.Model.Item;
import cnLabs.Micro.clientapplication.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class UserDetailsClient {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;


    public User getUser(String username) {
        ResponseEntity<User> response = restTemplate.getForEntity("http://USER-MICROSERVICE/user/username/" + username, User.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public User registerUser(User user) {
        ResponseEntity<User> response = restTemplate.postForEntity("http://USER-MICROSERVICE/user/", user, User.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
