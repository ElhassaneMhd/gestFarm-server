package Gestfarm.Dto.Request;

import Gestfarm.Enum.SaleStatus;
import Gestfarm.Model.Sheep;


import java.util.List;

public record SaleRequest(Integer id,
                          String name ,
                          Integer price,
                          Integer amount ,
                          SaleStatus status,
                          List<Sheep> sheep)  {
}
