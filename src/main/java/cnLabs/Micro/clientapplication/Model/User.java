package cnLabs.Micro.clientapplication.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

//    @JsonProperty("id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;

}