package Gestfarm.Mapper;

import Gestfarm.Dto.ShipmentDTO;
import Gestfarm.Model.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {

    private final UserMapper userMapper;
    @Autowired
    public ShipmentMapper ( UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public ShipmentDTO mapToDto(Shipment shipment){
        ShipmentDTO shipmentDTO = new ShipmentDTO();
        shipmentDTO.setId(shipment.getId());
        shipmentDTO.setAddress(shipment.getAddress());
        shipmentDTO.setPhone(shipment.getPhone());
        shipmentDTO.setStatus(shipment.getStatus());
        shipmentDTO.setShippingDate(shipment.getShippingDate());
        shipmentDTO.setSale(shipment.getSale());
        if (shipment.getShipper() != null){
            shipmentDTO.setShipper(userMapper.mapToShipper(shipment.getShipper()));
        }
        return shipmentDTO;
    }
}
