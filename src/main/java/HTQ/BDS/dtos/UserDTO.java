package HTQ.BDS.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @JsonProperty("user_names")
    @NotBlank(message = "Username is required")
    private String usernames;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email invalidate")
    private String email;
    @JsonProperty("profile_picture")
    private String profilePicture;
    @JsonProperty("role_id")
    @NotNull(message = "Role ID is required")
    private Long roleId;
}
