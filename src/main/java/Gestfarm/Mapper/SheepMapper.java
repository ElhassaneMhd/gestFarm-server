package Gestfarm.Mapper;

import Gestfarm.Dto.PublicSheepDTO;
import Gestfarm.Dto.SheepDTO;
import Gestfarm.Model.Sheep;
import org.springframework.stereotype.Component;


@Component
public class SheepMapper {

    private final UserMapper userMapper;

    public SheepMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SheepDTO mapToDTO(Sheep sheep) {
        SheepDTO sheepDTO = new SheepDTO();
        sheepDTO.setId(sheep.getId());
        sheepDTO.setNumber(sheep.getNumber());
        sheepDTO.setPrice(0);
        sheepDTO.setWeight(sheep.getWeight());
        sheepDTO.setStatus(sheep.getStatus());
        sheepDTO.setAge(sheep.getAge());
        if (sheep.getCategory() != null) {
            sheepDTO.setCategory(sheep.getCategory());
            sheepDTO.setCategoryName(sheep.getCategory().getName());
            sheepDTO.setPrice(sheep.getCategory().getPrice()*sheep.getWeight());
        }
        if (sheep.getSale() !=null){
            sheepDTO.setStatus(sheep.getStatus());
            sheepDTO.setSale(sheep.getSale());
        }
        return sheepDTO;
    }

    public PublicSheepDTO mapToPublicSheep(Sheep sheep){
        PublicSheepDTO publicSheep = new PublicSheepDTO();
        publicSheep.setNumber(sheep.getNumber());
        publicSheep.setAge(sheep.getAge());
        publicSheep.setWeight(sheep.getWeight());
        if (sheep.getCategory() != null){
            publicSheep.setCategoryName(sheep.getCategory().getName());
            publicSheep.setPrice(sheep.getCategory().getPrice()*sheep.getWeight());
        }
        if (sheep.getSale() != null){
            publicSheep.setSale(sheep.getSale());
            if (sheep.getSale().getShipment() != null){
                publicSheep.setShipment(sheep.getSale().getShipment());
                if (sheep.getSale().getShipment().getShipper() != null){
                    publicSheep.setShipper(userMapper.mapToShipper(sheep.getSale().getShipment().getShipper())
                    );
                }
            }
        }

        return publicSheep;
    }
}
