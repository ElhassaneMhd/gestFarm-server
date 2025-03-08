package Gestfarm.Mapper;

import Gestfarm.Dto.CategoryDTO;
import Gestfarm.Dto.PublicCategoryDTO;
import Gestfarm.Enum.SheepStatus;
import Gestfarm.Model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO mapToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setPrice(category.getPrice());
        categoryDTO.setImage(category.getImage());
        categoryDTO.setSheep(category.getSheep());
        return categoryDTO;
    }

    public PublicCategoryDTO mapToPublicDTO(Category category){
        PublicCategoryDTO publicCategory = new PublicCategoryDTO();
        publicCategory.setName(category.getName());
        publicCategory.setDescription(category.getDescription());
        publicCategory.setImage(category.getImage());
        publicCategory.setPrice(category.getPrice());
        publicCategory.setSheep(category.getSheep().stream().filter(sheep ->sheep.getStatus()==SheepStatus.AVAILABLE).toList().size());
        return publicCategory;
    }
}
