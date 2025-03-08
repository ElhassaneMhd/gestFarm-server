package Gestfarm.Dto;

import Gestfarm.Model.Role;
import Gestfarm.Model.Shipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipperDTO {
        private Integer id;
        private String username;
        private String email;
        private String phone;
        private List<Shipment> shipments;
}
