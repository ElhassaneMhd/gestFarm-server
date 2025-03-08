package Gestfarm.Dto;

import Gestfarm.Enum.ShipmentStatus;
import Gestfarm.Model.Sale;
import Gestfarm.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDTO {
    private Integer id;
    private String phone;
    private String address;
    private ShipmentStatus status;
    private Date shippingDate;
    private ShipperDTO shipper;
    private Sale sale;
    private Instant updatedAt;
    private Instant createdAt;
}
