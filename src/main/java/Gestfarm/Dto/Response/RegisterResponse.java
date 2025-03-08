package Gestfarm.Dto.Response;


import Gestfarm.Model.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String message;
    private String token;
    private Boolean status;
    private User user;
}
