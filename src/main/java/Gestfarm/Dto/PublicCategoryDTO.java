package Gestfarm.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicCategoryDTO {
    private String name;
    private String description;
    private Integer price ;
    private String image ;
    private Integer sheep;
}
