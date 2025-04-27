package Gestfarm.Service;

import Gestfarm.Dto.Request.CategoryRequest;
import Gestfarm.Mapper.CategoryMapper;
import Gestfarm.Model.Category;
import Gestfarm.Repository.CategoryRepository;
import Gestfarm.Repository.SheepRepository;
import Gestfarm.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Category Service Tests")
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
    @Order(1)
    @DisplayName("Find All Categories - Should Return Category List")
    void whenFindAll_thenReturnCategoryList() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory));
        assertNotNull(categoryService.findAll());
    }

    @Test
    @Order(2)
    @DisplayName("Paginate Categories - Should Return Paged Results")
    void whenPaginate_thenReturnPagedCategories() {
        Page<Category> page = new PageImpl<>(Arrays.asList(testCategory));
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        var result = categoryService.paginate(1, 10);
        assertNotNull(result);
        assertEquals(1, result.getPage());
    }

    @Test
    @Order(3)
    @DisplayName("Save Category - With Valid Data Should Return Category")
    void whenSaveCategory_withValidData_thenReturnCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        
        ResponseEntity<?> response = categoryService.save(testCategoryRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @Order(4)
    @DisplayName("Update Category - With Valid Data Should Return Updated Category")
    void whenUpdateCategory_withValidData_thenReturnUpdatedCategory() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        
        ResponseEntity<?> response = categoryService.update(1, testCategoryRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @Order(5)
    @DisplayName("Delete Category - With Valid Id Should Return Success")
    void whenDeleteCategory_withValidId_thenReturnSuccess() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));
        
        ResponseEntity<?> response = categoryService.delete(1);
        assertEquals(200, response.getStatusCode().value());
    }
}
