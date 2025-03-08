package Gestfarm.Controller;

import Gestfarm.Dto.*;
import Gestfarm.Enum.SheepStatus;
import Gestfarm.Mapper.CategoryMapper;
import Gestfarm.Mapper.SheepMapper;
import Gestfarm.Model.Sheep;
import Gestfarm.Repository.CategoryRepository;
import Gestfarm.Repository.SheepRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final SheepRepository sheepRepository;
    private final SheepMapper sheepMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public PublicController( SheepRepository sheepRepository, SheepMapper sheepMapper, CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.sheepRepository = sheepRepository;
        this.sheepMapper = sheepMapper;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getCategoriesName(){
        List<PublicCategoryDTO> categories = categoryRepository.findAll()
                .stream().map(categoryMapper::mapToPublicDTO).toList();
        return  ResponseEntity.ok().body(categories);
    }

    @GetMapping("/sheep/{number}")
    public ResponseEntity<Object> getSheepByNumber(@PathVariable int number){
        if (sheepRepository.existsByNumber(number)){
            Sheep sheep = sheepRepository.findByNumber(number);
            return ResponseEntity.ok().body(sheepMapper.mapToPublicSheep(sheep));
        }else{
            return ResponseEntity.badRequest().body("Undefined Sheep");
        }

    }
    @GetMapping("/sheep/available")
    public ResponseEntity<Object> getAvailableSheep(@RequestParam int page ,@RequestParam int limit){
        Pageable pageable = PageRequest.of(page-1, limit);
        int total = sheepRepository.findByStatus(SheepStatus.AVAILABLE).size();
        Page<Sheep> sheepPage = sheepRepository.findPaginateByStatus(SheepStatus.AVAILABLE, pageable);
        List<PublicSheepDTO> publicSheepDTOS = sheepPage.map(sheepMapper::mapToPublicSheep).toList();

        return ResponseEntity.ok().body(new PaginateDTO<>(page,limit,total,publicSheepDTOS));
    }

}
