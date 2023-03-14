package cnLabs.Micro.clientapplication.Clients;

import cnLabs.Micro.clientapplication.Model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    public Cart getCartByUserId(Long userId) {
        ResponseEntity<Cart> response = restTemplate.getForEntity("http://CART-MICROSERVICE/cart/" + userId, Cart.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public Cart removeCartItem(Long itemId, Long userId) {
        ResponseEntity<Cart> response = restTemplate.exchange("http://CART-MICROSERVICE/cart/" + userId + "/" + itemId, HttpMethod.DELETE, null, Cart.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public Cart removeCartItemCompletely(Long itemId, Long userId) {
        ResponseEntity<Cart> response = restTemplate.exchange("http://CART-MICROSERVICE/cart/delete-item/" + userId + "/" + itemId, HttpMethod.DELETE, null, Cart.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
