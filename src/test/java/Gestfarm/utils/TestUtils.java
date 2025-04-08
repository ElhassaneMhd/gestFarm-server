package Gestfarm.utils;

import Gestfarm.Dto.Request.*;
import Gestfarm.Dto.SaleDTO;
import Gestfarm.Enum.*;
import Gestfarm.Model.*;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtils {
    
    public static UserRequest createTestUserRequest() {
        return new UserRequest(
            "testUser",
            "test@example.com",
            "password123",
            "1234567890",
            "password123",
            RoleType.USER
        );
    }

    public static User createTestUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");
        user.setPhone("1234567890");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return user;
    }

    public static Category createTestCategory() {
        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setPrice(100);
        category.setImage("test.jpg");
        return category;
    }

    public static CategoryRequest createTestCategoryRequest() {
        return new CategoryRequest(
            "Test Category",
            "Test Description",
            100,
            "test.jpg"
        );
    }

    public static Sheep createTestSheep() {
        Sheep sheep = new Sheep();
        sheep.setId(1);
        sheep.setNumber(101);
        sheep.setWeight(50);
        sheep.setAge(SheepAge.MATURE);
        sheep.setStatus(SheepStatus.AVAILABLE);
        sheep.setCategory(createTestCategory());
        return sheep;
    }

    public static SheepRequest createTestSheepRequest() {
        return new SheepRequest(
            101,
            50,
            SheepAge.MATURE,
            SheepStatus.AVAILABLE,
            createTestCategory()
        );
    }

    public static Sale createTestSale() {
        Sale sale = new Sale();
        sale.setId(1);
        sale.setName("Test Sale");
        sale.setAmount(1);
        sale.setPrice(100);
        sale.setStatus(SaleStatus.PAID);
        sale.setSheep(List.of(createTestSheep()));
        return sale;
    }

    public static SaleRequest createTestSaleRequest() {
        return new SaleRequest(
            1,
            "Test Sale",
            100,
            1,
            SaleStatus.PAID,
            List.of(createTestSheep())
        );
    }

    public static Shipment createTestShipment() {
        Shipment shipment = new Shipment();
        shipment.setId(1);
        shipment.setAddress("Test Address");
        shipment.setPhone("1234567890");
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setShippingDate(new Date(System.currentTimeMillis()));
        shipment.setSale(createTestSale());
        shipment.setShipper(createTestUser());
        return shipment;
    }

    public static ShipmentRequest createTestShipmentRequest() {
        return new ShipmentRequest(
            "Test Shipment",
            "Test Address",
            "1234567890",
            ShipmentStatus.PENDING,
            createTestUser(),
            new Date(System.currentTimeMillis()),
            createTestSale()
        );
    }

    public static Role createTestRole() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        role.setPermissions(new ArrayList<>());
        return role;
    }

    public static Permission createTestPermission() {
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setName("READ_USERS");
        return permission;
    }

    public static SaleDTO createTestSaleDTO() {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(1);
        saleDTO.setName("Test Sale");
        saleDTO.setAmount(5);
        saleDTO.setStatus(SaleStatus.PAID);
        saleDTO.setPrice(1000);
        saleDTO.setSheep(Arrays.asList(createTestSheep()));
        saleDTO.setShipment(null);
        saleDTO.setCreatedAt(Instant.now());
        saleDTO.setUpdatedAt(Instant.now());
        return saleDTO;
    }
}
