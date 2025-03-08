package Gestfarm.Dto;

import Gestfarm.Model.Sheep;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer price ;
    private String image ;
    private List<Sheep> sheep;

}
