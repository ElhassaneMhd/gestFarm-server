package Gestfarm.Repository;

import Gestfarm.Enum.SheepStatus;
import Gestfarm.Model.Sheep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SheepRepository extends JpaRepository<Sheep, Integer> {

    Page<Sheep> findPaginateByStatus(SheepStatus status, Pageable pageable);

    Sheep findByNumber(int number);
    List<Sheep> findByStatus(SheepStatus sheepStatus);
    @Transactional
    @Modifying
    @Query("UPDATE Sheep s SET s.sale = NULL WHERE s.sale.id = :saleId")
    void setSaleToNull(Integer saleId);
    boolean existsByNumber(int number);
}
