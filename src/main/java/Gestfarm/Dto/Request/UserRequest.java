package Gestfarm.Dto.Request;

import Gestfarm.Enum.RoleType;

public record UserRequest(
        String username,
        String email,
        String password,
        String phone,
        String passwordConfirmation,
        RoleType role
) {

}
