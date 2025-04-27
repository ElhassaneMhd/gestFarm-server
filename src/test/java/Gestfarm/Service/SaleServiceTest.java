package Gestfarm.Service;

import Gestfarm.Dto.Request.SaleRequest;
import Gestfarm.Mapper.SaleMapper;
import Gestfarm.Model.Sale;
import Gestfarm.Model.Sheep;
import Gestfarm.Repository.SaleRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Sale Service Tests")
public class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SheepRepository sheepRepository;

    @Mock
    private SaleMapper saleMapper;

    @Mock
    private SheepService sheepService;

    @InjectMocks
    private SaleService saleService;

    private Sale testSale;
    private SaleRequest testSaleRequest;
    private Sheep testSheep;

    @BeforeEach
    void setUp() {
        testSale = TestUtils.createTestSale();
        testSaleRequest = TestUtils.createTestSaleRequest();
        testSheep = TestUtils.createTestSheep();
    }

    @Test
    @Order(1)
    @DisplayName("Find All Sales - Should Return Sale List")
    void whenFindAll_thenReturnSaleList() {
        when(saleRepository.findAll()).thenReturn(Arrays.asList(testSale));
        assertNotNull(saleService.findAll());
    }

    @Test
    @Order(2)
    @DisplayName("Paginate Sales - Should Return Paged Results")
    void whenPaginate_thenReturnPagedSales() {
        Page<Sale> page = new PageImpl<>(Arrays.asList(testSale));
        when(saleRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        var result = saleService.paginate(1, 10);
        assertNotNull(result);
        assertEquals(1, result.getPage());
    }

    @Test
    @Order(3)
    @DisplayName("Save Sale - With Valid Data Should Return Sale")
    void whenSaveSale_withValidData_thenReturnSale() {
        // Mock sheep service to return test sheep
        when(sheepService.find(any(Integer.class))).thenReturn(testSheep);
        // Mock repository save
        when(saleRepository.save(any(Sale.class))).thenReturn(testSale);
        
        ResponseEntity<Sale> response = saleService.save(testSaleRequest);
        
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        // Verify sale properties were set correctly
        Sale savedSale = response.getBody();
        assertNotNull(savedSale);
        assertEquals(testSaleRequest.name(), savedSale.getName());
        assertEquals(testSaleRequest.amount(), savedSale.getAmount());
        assertEquals(testSaleRequest.price(), savedSale.getPrice());
        assertEquals(testSaleRequest.status(), savedSale.getStatus());
    }

    @Test
    @Order(4)
    @DisplayName("Update Sale - With Valid Data Should Return Updated Sale")
    void whenUpdateSale_withValidData_thenReturnUpdatedSale() {
        testSale.setSheep(new ArrayList<>());
        testSale.getSheep().add(testSheep);
        
        when(saleRepository.findById(1)).thenReturn(Optional.of(testSale));
        when(sheepService.find(any(Integer.class))).thenReturn(testSheep);
        
        ResponseEntity<?> response = saleService.update(1, testSaleRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @Order(5)
    @DisplayName("Delete Sale - With Valid Id Should Return Success")
    void whenDeleteSale_withValidId_thenReturnSuccess() {
        when(saleRepository.findById(1)).thenReturn(Optional.of(testSale));
        
        ResponseEntity<?> response = saleService.delete(1);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void whenFindById_withValidId_thenReturnSale() {
        when(saleRepository.findById(1)).thenReturn(Optional.of(testSale));
        when(saleMapper.mapToDto(any(Sale.class))).thenReturn(TestUtils.createTestSaleDTO());
        
        assertNotNull(saleService.findById(1));
    }
}
