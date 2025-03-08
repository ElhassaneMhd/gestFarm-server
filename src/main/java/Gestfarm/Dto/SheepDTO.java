package Gestfarm.Dto;

import Gestfarm.Enum.SheepAge;
import Gestfarm.Enum.SheepStatus;
import Gestfarm.Model.Category;
import Gestfarm.Model.Sale;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheepDTO {
    private Integer number;
    private Integer price;
    private Integer id;
    private Integer weight;
    private SheepAge age ;
    private SheepStatus status;
    private String categoryName;
    private Category category;
    private Sale sale;
}
