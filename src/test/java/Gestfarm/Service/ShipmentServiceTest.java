package Gestfarm.Service;

import Gestfarm.Dto.Request.ShipmentRequest;
import Gestfarm.Mapper.ShipmentMapper;
import Gestfarm.Model.Sale;
import Gestfarm.Model.Shipment;
import Gestfarm.Model.User;
import Gestfarm.Repository.SaleRepository;
import Gestfarm.Repository.ShipmentRepository;
import Gestfarm.Repository.UserRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentMapper shipmentMapper;

    @Mock
    private UserService userService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    private Shipment testShipment;
    private ShipmentRequest testShipmentRequest;

    @BeforeEach
    void setUp() {
        testShipment = TestUtils.createTestShipment();
        testShipmentRequest = TestUtils.createTestShipmentRequest();
    }

    @Test
    void whenFindAll_thenReturnShipmentList() {
        when(shipmentRepository.findAll()).thenReturn(Arrays.asList(testShipment));
        assertNotNull(shipmentService.findAll());
    }

    @Test
    void whenPaginate_thenReturnPagedShipments() {
        Page<Shipment> page = new PageImpl<>(Arrays.asList(testShipment));
        when(shipmentRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        var result = shipmentService.paginate(1, 10);
        assertNotNull(result);
        assertEquals(1, result.getPage());
    }

    @Test
    void whenSaveShipment_withValidData_thenReturnShipment() {
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(testShipment);
        when(userService.findById(any())).thenReturn(TestUtils.createTestUser());
        when(saleRepository.findById(any())).thenReturn(Optional.of(TestUtils.createTestSale()));
        
        Shipment result = shipmentService.save(testShipmentRequest);
        assertNotNull(result);
        assertEquals(testShipment.getId(), result.getId());
    }

    @Test
    void whenUpdateShipment_withValidData_thenReturnUpdatedShipment() {
        User testUser = TestUtils.createTestUser();
        testUser.setShipments(new ArrayList<>());
        testUser.getShipments().add(testShipment);
        testShipment.setShipper(testUser);
        
        when(shipmentRepository.findById(1)).thenReturn(Optional.of(testShipment));
        when(userService.findById(any())).thenReturn(testUser);
        
        ResponseEntity<?> response = shipmentService.update(1, testShipmentRequest);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void whenDeleteShipment_withValidId_thenReturnSuccess() {
        User testUser = TestUtils.createTestUser();
        testUser.setShipments(new ArrayList<>());
        testUser.getShipments().add(testShipment);
        testShipment.setShipper(testUser);
        
        Sale testSale = TestUtils.createTestSale();
        testShipment.setSale(testSale);
        
        when(shipmentRepository.findById(1)).thenReturn(Optional.of(testShipment));
        
        ResponseEntity<?> response = shipmentService.delete(1);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void whenDeleteShipment_withInvalidId_thenReturnError() {
        when(shipmentRepository.findById(1)).thenReturn(Optional.empty());
        
        ResponseEntity<?> response = shipmentService.delete(1);
        assertEquals(404, response.getStatusCode().value());
    }
}
