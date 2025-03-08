package Gestfarm.Mapper;
import Gestfarm.Dto.SaleDTO;
import Gestfarm.Model.Sale;
import org.springframework.stereotype.Component;


@Component
public class SaleMapper {
    public SaleDTO mapToDto(Sale sale){
        SaleDTO saleDto = new SaleDTO();
        saleDto.setId(sale.getId());
        saleDto.setName(sale.getName());
        saleDto.setAmount(sale.getAmount());
        saleDto.setSheep(sale.getSheep());
        saleDto.setStatus(sale.getStatus());
        saleDto.setPrice(sale.getPrice());
        saleDto.setShipment(sale.getShipment());
        saleDto.setCreatedAt(sale.getCreatedAt());
        saleDto.setUpdatedAt(sale.getUpdatedAt());
        return saleDto;
    }
}
