package Gestfarm.Dto;

import Gestfarm.Model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String role;
    private String phone;
    private Instant createdAt;
    private List<String> permissions;
}
