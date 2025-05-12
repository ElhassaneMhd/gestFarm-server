package Gestfarm.Service;

import Gestfarm.Dto.CategoryDTO;
import Gestfarm.Dto.PaginateDTO;
import Gestfarm.Dto.Request.CategoryRequest;
import Gestfarm.Mapper.CategoryMapper;
import Gestfarm.Model.Category;
import Gestfarm.Repository.CategoryRepository;
import Gestfarm.Repository.SheepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SheepRepository sheepRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, SheepRepository sheepRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.sheepRepository = sheepRepository;
    }
    public List<CategoryDTO> findAll(){
        List<Category> categories= categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::mapToDTO)
                .toList();
    }
    public PaginateDTO<CategoryDTO>  paginate(int page , int limit) {
        int total = (int) categoryRepository.count();
            Pageable pageable = PageRequest.of(page-1, limit);
            Page<Category> categories= categoryRepository.findAll(pageable);
            List<CategoryDTO> categoryDTOS= categories.stream()
                    .map(categoryMapper::mapToDTO)
                    .toList();
            return new PaginateDTO<CategoryDTO>(page,limit,total,categoryDTOS);

    }

    public Category find(int id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }


    public ResponseEntity<Object> save(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.name());
        category.setDescription(categoryRequest.description());
        category.setPrice(categoryRequest.price());
        if (categoryRequest.image() != null)     category.setImage(categoryRequest.image());
        
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @Transactional
    public ResponseEntity<Object> update(int id,CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (categoryRequest.name()!=null)category.setName(categoryRequest.name());
        if (categoryRequest.description()!=null)category.setDescription(categoryRequest.description());
        if (categoryRequest.price()!=null)category.setPrice(categoryRequest.price());
        if (categoryRequest.image()!=null)category.setImage(categoryRequest.image());
        categoryRepository.save(category);
        return ResponseEntity.ok(category);
    }

    @Transactional
    public ResponseEntity<Object> delete(int id) {
        Category category= categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
//        sheepRepository.setCategoryToNull(id);
        categoryRepository.delete(category);
        return ResponseEntity.ok("Category deleted successfully");
    }

    public void multipleDelete(List<Integer> ids){
        ids.forEach(this::delete);
    }

}
