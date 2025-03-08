package Gestfarm.Controller;

import Gestfarm.Dto.Request.CategoryRequest;
import Gestfarm.Model.Category;
import Gestfarm.Repository.CategoryRepository;
import Gestfarm.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('READ_CATEGORIES')")
    public ResponseEntity<Object> findAll(  ) {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/paginate")
    @PreAuthorize("hasAuthority('READ_CATEGORIES')")
    public ResponseEntity<Object> paginate(@RequestParam int page ,@RequestParam int limit  ) {
        return ResponseEntity.ok(categoryService.paginate(page,limit));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('WRITE_CATEGORIES')")
    public ResponseEntity<Object> create(@RequestBody CategoryRequest categoryRequest){
        return categoryService.save(categoryRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_CATEGORIES')")
    public ResponseEntity<Object> update(@PathVariable Integer id , @RequestBody CategoryRequest req){
       if (categoryRepository.existsById(id)){
          return ResponseEntity.ok(categoryService.update(id,req))  ;
       }
       return new ResponseEntity<>("Cannot update undefined category", HttpStatusCode.valueOf(404));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_CATEGORIES')")
    public ResponseEntity<Object> delete(@PathVariable Integer id){
        if (categoryRepository.existsById(id)){
           return  categoryService.delete(id);
        }
        return new ResponseEntity<>("Cannot delete undefined category" ,HttpStatusCode.valueOf(404));
    }

    @PreAuthorize("hasAuthority('DELETE_CATEGORIES')")
    @PostMapping("/delete/multiple")
    public void multipleDelete(@RequestBody List<Integer> ids){
        categoryService.multipleDelete(ids);
    }

}
