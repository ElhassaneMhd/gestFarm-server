package Gestfarm.Dto;

import Gestfarm.Enum.SheepAge;
import Gestfarm.Enum.SheepStatus;
import Gestfarm.Model.Category;
import Gestfarm.Model.Sale;
import Gestfarm.Model.Shipment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicSheepDTO {
    private Integer number;
    private Integer price;
    private Integer weight;
    private SheepAge age ;
    private String categoryName;
    private Sale sale;
    private Shipment shipment;
    private ShipperDTO shipper;
}
