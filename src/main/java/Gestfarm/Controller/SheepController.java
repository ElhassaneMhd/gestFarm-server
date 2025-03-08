package Gestfarm.Controller;

import Gestfarm.Dto.Request.SheepRequest;
import Gestfarm.Enum.SheepStatus;
import Gestfarm.Model.Sheep;
import Gestfarm.Repository.SheepRepository;
import Gestfarm.Service.SheepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/sheep")
public class SheepController {

    private final SheepService sheepService;
    private final SheepRepository sheepRepository;

    @Autowired
    public SheepController(SheepService sheepService, SheepRepository sheepRepository) {
        this.sheepService = sheepService;
        this.sheepRepository = sheepRepository;
    }


    @GetMapping()
    @PreAuthorize("hasAuthority('READ_SHEEP')")
    public ResponseEntity<Object> findAll( ) {
        return ResponseEntity.ok(sheepService.getAll());
    }

    @GetMapping("/paginate")
    @PreAuthorize("hasAuthority('READ_SHEEP')")
    public ResponseEntity<Object> paginate(@RequestParam int page ,@RequestParam int limit  ) {
        return ResponseEntity.ok(sheepService.paginate(page,limit));
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('WRITE_SHEEP')")
    public Sheep create(@RequestBody SheepRequest sheepRequest) {
        return  sheepService.save(sheepRequest);
    }

    @PreAuthorize("hasAuthority('UPDATE_SHEEP')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody SheepRequest sheep) {
        if (!sheepRepository.existsById(id)){
            return  ResponseEntity.badRequest().body("Cannot update undefined Sheep");
        }else if (sheepRepository.findById(id).get().getStatus() == SheepStatus.SOLD){
            return new ResponseEntity<>("Cannot update sheep: This sheep has already been sold " , HttpStatusCode.valueOf(401));
        }
        return  sheepService.update(id, sheep);
    }

    @PreAuthorize("hasAuthority('DELETE_SHEEP')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete( @PathVariable Integer id){
        return  sheepService.delete(id);
    }

    @PreAuthorize("hasAuthority('DELETE_SHEEP')")
    @PostMapping("/delete/multiple")
    public void multipleDelete(@RequestBody List<Integer> ids){
        sheepService.multipleDelete(ids);
    }

}
