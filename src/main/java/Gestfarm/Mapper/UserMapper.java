package Gestfarm.Mapper;

import Gestfarm.Dto.ShipperDTO;
import Gestfarm.Dto.UserDTO;
import Gestfarm.Model.Permission;
import Gestfarm.Model.Shipment;
import Gestfarm.Model.User;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserMapper {

    public UserDTO mapToDto(User user){
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().getName());
        userDto.setPermissions(user.getRole().getPermissions().stream().map(Permission::getName).toList());
        userDto.setPhone(user.getPhone());
        userDto.setCreatedAt(user.getCreatedAt());
        return userDto;
    }

    public ShipperDTO mapToShipper(User user){
        List<Shipment> shipmentList = user.getShipments();
        ShipperDTO shipperDTO = new ShipperDTO();
        shipperDTO.setId(user.getId());
        shipperDTO.setUsername(user.getUsername());
        shipperDTO.setEmail(user.getEmail());
        shipperDTO.setPhone(user.getPhone());
        shipperDTO.setShipments(shipmentList);
        return shipperDTO;
    }

}