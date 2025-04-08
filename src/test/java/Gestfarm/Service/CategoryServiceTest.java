package Gestfarm.Service;

import Gestfarm.Dto.Request.CategoryRequest;
import Gestfarm.Mapper.CategoryMapper;
import Gestfarm.Model.Category;
import Gestfarm.Repository.CategoryRepository;
import Gestfarm.Repository.SheepRepository;
import Gestfarm.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private CategoryMapper categoryMapper;
    
    @Mock
    private SheepRepository sheepRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;
    private CategoryRequest testCategoryRequest;

    @BeforeEach
    void setUp() {
        testCategory = TestUtils.createTestCategory();
        testCategoryRequest = TestUtils.createTestCategoryRequest();
    }

    @Test
    void whenFindAll_thenReturnCategoryList() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory));
        assertNotNull(categoryService.findAll());
    }

    @Test
    void whenPaginate_thenReturnPagedCategories() {
        Page<Category> page = new PageImpl<>(Arrays.asList(testCategory));
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        var result = categoryService.paginate(1, 10);
        assertNotNull(result);
        assertEquals(1, result.getPage());
    }

    @Test
    void whenSaveCategory_withValidData_thenReturnCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        
        ResponseEntity<?> response = categoryService.save(testCategoryRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void whenUpdateCategory_withValidData_thenReturnUpdatedCategory() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        
        ResponseEntity<?> response = categoryService.update(1, testCategoryRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void whenDeleteCategory_withValidId_thenReturnSuccess() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));
        
        ResponseEntity<?> response = categoryService.delete(1);
        assertEquals(200, response.getStatusCode().value());
    }
}
