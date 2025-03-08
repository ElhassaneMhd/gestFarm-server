package Gestfarm.Service;

import Gestfarm.Dto.PaginateDTO;
import Gestfarm.Dto.Request.ShipmentRequest;
import Gestfarm.Dto.ShipmentDTO;
import Gestfarm.Mapper.ShipmentMapper;
import Gestfarm.Model.Sale;
import Gestfarm.Model.Shipment;
import Gestfarm.Model.User;
import Gestfarm.Repository.SaleRepository;
import Gestfarm.Repository.ShipmentRepository;
import Gestfarm.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShipmentService {


    private final ShipmentRepository shipmentRepository;
    private final UserService userService;
    private final ShipmentMapper shipmentMapper;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository, SaleRepository saleRepository, UserService userService, ShipmentMapper shipmentMapper, UserRepository userRepository) {
        this.shipmentRepository = shipmentRepository;
        this.userService = userService;
        this.shipmentMapper = shipmentMapper;
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
    }

    public List<ShipmentDTO> findAll() {
        List<Shipment> shipmentsList = shipmentRepository.findAll();
        return  shipmentsList.stream().map(shipmentMapper::mapToDto).toList();
    }

    public PaginateDTO<ShipmentDTO> paginate(int page, int limit) {
        int total = (int) shipmentRepository.count();
        Pageable pageable = PageRequest.of(page-1, limit);
        Page<Shipment> sheep= shipmentRepository.findAll(pageable);
        List<ShipmentDTO> ShipmentDTOS= sheep.stream()
                .map(shipmentMapper::mapToDto)
                .toList();
        return new PaginateDTO<>(page,limit,total,ShipmentDTOS);
    }

    public Shipment find(Integer id) {
        return shipmentRepository.findById(id).orElse(null);
    }

    public Shipment save(ShipmentRequest shipmentRequest) {
        Sale sale = saleRepository.findById(shipmentRequest.sale().getId()).orElse(null);
        User shipper = userService.findById(shipmentRequest.shipper().getId());
        Shipment shipment = new Shipment();
        shipment.setAddress(shipmentRequest.address());
        shipment.setPhone(shipmentRequest.phone());
        shipment.setStatus(shipmentRequest.status());
        shipment.setShippingDate(shipmentRequest.shippingDate());
        shipment.setSale(sale);
        shipment.setShipper(shipper);
        return shipmentRepository.save(shipment);
    }

    @Transactional
    public ResponseEntity<Object> update(int id , ShipmentRequest shipmentRequest) {
        Shipment shipment = shipmentRepository.findById(id).orElse(null);
        if (shipment != null ){
            if (shipmentRequest.address() != null) shipment.setAddress(shipmentRequest.address());
            if (shipmentRequest.phone()!= null) shipment.setPhone(shipmentRequest.phone());
            if (shipmentRequest.status()!= null) shipment.setStatus(shipmentRequest.status());
            if (shipmentRequest.shippingDate()!= null) shipment.setShippingDate(shipmentRequest.shippingDate());
            if (shipmentRequest.shipper()!= null){
                User newShipper = userService.findById(shipmentRequest.shipper().getId());
                User shipper = shipment.getShipper();
                List<Shipment> shipments = shipper.getShipments()
                        .stream().filter( sp -> !sp.getId().equals(id) ).toList();
                shipper.setShipments(shipments);
                shipment.setShipper(newShipper);
            }
            if (shipmentRequest.sale()!= null) {
                Sale newSale = saleRepository.findById(shipmentRequest.sale().getId()).orElse(null);
                Sale sale = shipment.getSale();
                sale.setShipment(null);
                shipment.setSale(newSale);
            }
            return ResponseEntity.ok(shipment);
        }else {
            return ResponseEntity.badRequest().body("Undefined Shipment , check shipment Id");
        }
    }

    @Transactional
    public ResponseEntity<Object> delete(Integer id) {
        Shipment shipment = shipmentRepository.findById(id).orElse(null);
        if (shipment != null){
            Sale sale =shipment.getSale();
            sale.setShipment(null);
            User shipper = shipment.getShipper();
            List<Shipment> shipments = shipper.getShipments()
                    .stream().filter( sp -> !sp.getId().equals(id) ).toList();
            shipper.setShipments(shipments);
            shipmentRepository.delete(shipment);
            return ResponseEntity.ok("Deleted successfully");
        }
        return new ResponseEntity<>("Cannot delete undefined shipments" , HttpStatusCode.valueOf(404));
    }

    public void multipleDelete(List<Integer> ids){
        ids.forEach(this::delete);
    }
}
