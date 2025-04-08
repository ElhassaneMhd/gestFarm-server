package Gestfarm.Service;

import Gestfarm.Dto.Request.SheepRequest;
import Gestfarm.Enum.SheepStatus;
import Gestfarm.Mapper.SheepMapper;
import Gestfarm.Model.Sheep;
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
public class SheepServiceTest {

    @Mock
    private SheepRepository sheepRepository;

    @Mock
    private SheepMapper sheepMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private SheepService sheepService;

    private Sheep testSheep;
    private SheepRequest testSheepRequest;

    @BeforeEach
    void setUp() {
        testSheep = TestUtils.createTestSheep();
        testSheepRequest = TestUtils.createTestSheepRequest();
    }

    @Test
    void whenGetAll_thenReturnSheepList() {
        when(sheepRepository.findAll()).thenReturn(Arrays.asList(testSheep));
        assertNotNull(sheepService.getAll());
    }

    @Test
    void whenPaginate_thenReturnPagedSheep() {
        Page<Sheep> page = new PageImpl<>(Arrays.asList(testSheep));
        when(sheepRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        var result = sheepService.paginate(1, 10);
        assertNotNull(result);
        assertEquals(1, result.getPage());
    }

    @Test
    void whenSaveSheep_withValidData_thenReturnSheep() {
        when(categoryService.find(any(Integer.class))).thenReturn(TestUtils.createTestCategory());
        when(sheepRepository.save(any(Sheep.class))).thenReturn(testSheep);
        
        Sheep result = sheepService.save(testSheepRequest);
        assertNotNull(result);
        assertEquals(testSheep.getId(), result.getId());
    }

    @Test
    void whenUpdateSheep_withValidData_thenReturnUpdatedSheep() {
        when(sheepRepository.findById(1)).thenReturn(Optional.of(testSheep));
        when(sheepRepository.save(any(Sheep.class))).thenReturn(testSheep);
        
        ResponseEntity<?> response = sheepService.update(1, testSheepRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void whenDeleteSheep_withSoldStatus_thenReturnError() {
        testSheep.setStatus(SheepStatus.SOLD);
        when(sheepRepository.findById(1)).thenReturn(Optional.of(testSheep));
        
        ResponseEntity<?> response = sheepService.delete(1);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void whenDeleteSheep_withValidStatus_thenReturnSuccess() {
        testSheep.setStatus(SheepStatus.AVAILABLE);
        when(sheepRepository.findById(1)).thenReturn(Optional.of(testSheep));
        
        ResponseEntity<?> response = sheepService.delete(1);
        assertEquals(200, response.getStatusCode().value());
    }
}
