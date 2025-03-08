package Gestfarm.Dto.Request;

import Gestfarm.Enum.ShipmentStatus;
import Gestfarm.Model.Sale;
import Gestfarm.Model.User;

import java.sql.Date;


public record ShipmentRequest(String name,
                              String address,
                              String phone,
                              ShipmentStatus status,
                              User shipper,
                              Date shippingDate,
                              Sale sale
) {
}