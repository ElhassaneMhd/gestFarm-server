package Gestfarm.Dto.Request;

import Gestfarm.Enum.SheepAge;
import Gestfarm.Enum.SheepStatus;
import Gestfarm.Model.Category;

public record SheepRequest(Integer number ,
                           Integer weight,
                           SheepAge age,
                           SheepStatus status,
                           Category category
) {
}

