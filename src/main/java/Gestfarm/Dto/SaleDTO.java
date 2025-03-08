package Gestfarm.Dto;

import Gestfarm.Enum.SaleStatus;
import Gestfarm.Model.Sheep;
import Gestfarm.Model.Shipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDTO {
    private Integer id;
    private String name ;
    private Integer amount;
    private SaleStatus status;
    private Integer price;
    private List<Sheep> sheep;
    private Shipment shipment;
    private Instant updatedAt;
    private Instant createdAt;

}
