package HTQ.BDS.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    @JsonProperty("user_names")
    @NotBlank(message = "Username is required")
    private String usernames;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
