package cnLabs.Micro.clientapplication.Clients;

import cnLabs.Micro.clientapplication.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

//    public User loginUser(String username, String password) {
//        String url = "http://USER-MICROSERVICE/login";
//
//        // create auth credentials
//        String authStr = "username:password";
//        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
//
//        // create headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Basic " + base64Creds);
//
//        // create request
//        HttpEntity request = new HttpEntity(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            String body = response.getBody();
//            // How to decode the body and inject user authentication?
//            // Does that include anything about the user?
//            return ???
//        } else {
//            return null;
//        }
}

