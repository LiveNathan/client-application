package cnLabs.Micro.clientapplication.Clients;

import cnLabs.Micro.clientapplication.Model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class CartServiceClient {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;


    public Object addCartItem(Long itemId, Long userId) {
        String url = "http://CART-MICROSERVICE/cart/" + userId;
        URI uri = UriComponentsBuilder.fromUriString(url).queryParam("item-id", itemId).build().encode().toUri();
        ResponseEntity<Cart> response = restTemplate.postForEntity(uri, null, Cart.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
