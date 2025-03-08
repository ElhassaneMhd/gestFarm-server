package Gestfarm.Controller;

import Gestfarm.Dto.Request.ShipmentRequest;
import Gestfarm.Model.Shipment;
import Gestfarm.Service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('READ_SHIPMENTS')")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    @GetMapping("/paginate")
    @PreAuthorize("hasAuthority('READ_SHIPMENTS')")
    public ResponseEntity<Object> paginate(@RequestParam int page ,@RequestParam int limit  ) {
        return ResponseEntity.ok(shipmentService.paginate(page,limit));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('WRITE_SHIPMENTS')")
    public Shipment create(@RequestBody ShipmentRequest shipment) {
       return shipmentService.save(shipment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_SHIPMENTS')")
    public ResponseEntity<Object> update(@PathVariable Integer id,@RequestBody ShipmentRequest shipmentRequest) {
        return ResponseEntity.ok(shipmentService.update(id,shipmentRequest));
    }

    @PreAuthorize("hasAuthority('DELETE_SHIPMENTS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id){
        return  shipmentService.delete(id);
    }

    @PreAuthorize("hasAuthority('DELETE_SHIPMENTS')")
    @PostMapping("/delete/multiple")
    public void multipleDelete(@RequestBody List<Integer> ids) {
        shipmentService.multipleDelete(ids);
    }
}
