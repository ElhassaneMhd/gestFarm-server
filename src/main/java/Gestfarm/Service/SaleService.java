package Gestfarm.Service;

import Gestfarm.Dto.PaginateDTO;
import Gestfarm.Dto.Request.SaleRequest;
import Gestfarm.Dto.SaleDTO;
import Gestfarm.Enum.SheepStatus;
import Gestfarm.Mapper.SaleMapper;
import Gestfarm.Model.Sale;
import Gestfarm.Model.Sheep;
import Gestfarm.Repository.SaleRepository;
import Gestfarm.Repository.SheepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SheepService sheepService;
    private final SheepRepository sheepRepository;
    private final SaleMapper saleMapper;

    @Autowired
    public SaleService(SaleRepository saleRepository, SheepService sheepService, SheepRepository sheepRepository, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.sheepService = sheepService;
        this.sheepRepository = sheepRepository;
        this.saleMapper = saleMapper;
    }

    public List<SaleDTO> findAll(){
        List<Sale> saleList= saleRepository.findAll();
        return  saleList.stream().map(saleMapper::mapToDto).toList();
    }

    public PaginateDTO<SaleDTO> paginate(int page, int limit) {
        int total = (int) saleRepository.count();
        Pageable pageable = PageRequest.of(page-1, limit);
        Page<Sale> sheep= saleRepository.findAll(pageable);
        List<SaleDTO> SaleDTOS= sheep.stream()
                .map(saleMapper::mapToDto)
                .toList();
        return new PaginateDTO<>(page,limit,total,SaleDTOS);
    }
    public SaleDTO findById(Integer id){
        Sale sale= saleRepository.findById(id).orElse(null);
        if (sale != null) return saleMapper.mapToDto(sale);
        return null;
    }


    @Transactional
    public ResponseEntity<Sale> save(SaleRequest saleRequest){
        ArrayList<Sheep> sheepList = new ArrayList<>();
        Sale sale = new Sale();
        sale.setName(saleRequest.name());
        sale.setAmount(saleRequest.amount());
        sale.setPrice(saleRequest.price());
        sale.setStatus(saleRequest.status());
        saleRequest.sheep().forEach(sp -> {
            Sheep sheep = sheepService.find(sp.getId());
            sheep.setSale(sale);
            sheep.setStatus(SheepStatus.SOLD);
            sheepList.add(sheep);
        });
        sale.setSheep(sheepList);
        saleRepository.save(sale);
        return ResponseEntity.ok(sale);
    }

    @Transactional
    public ResponseEntity<Object> update(Integer id ,SaleRequest request) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if (sale != null ){
            if (request.sheep()!= null){
                List<Sheep> sheepList= sale.getSheep();
                sheepList.forEach(sp -> {
                    sp.setSale(null);
                    sp.setStatus(SheepStatus.AVAILABLE);
                });
                request.sheep().forEach(sp -> {
                    Sheep sheep = sheepService.find(sp.getId());
                    sheep.setSale(sale);
                    sheep.setStatus(SheepStatus.SOLD);
                    sheepList.add(sheep);
                });
            }
            if (request.amount() != null) sale.setAmount(request.amount());
            if (request.name()!= null) sale.setName(request.name());
            if (request.price()!= null) sale.setPrice(request.price());
            if (request.status()!= null) sale.setStatus(request.status());
        }
        return ResponseEntity.ok(sale);
    }


    @Transactional
    public ResponseEntity<Object> delete(Integer id) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if (sale == null){
            return new ResponseEntity<>("Cannot delete undefined Sale" , HttpStatusCode.valueOf(404));
        }
        sale.getSheep().forEach(sheep -> {
            sheep.setStatus(SheepStatus.AVAILABLE);
            sheep.setSale(null);
            sheepRepository.save(sheep);
        });
//        sheepRepository.setSaleToNull(id);
        saleRepository.delete(sale);
        return ResponseEntity.ok("Deleted successfully");
    }

    public void multipleDelete(List<Integer> ids){
        ids.forEach(this::delete);
    }


}
