package Gestfarm.Model;

import Gestfarm.Enum.ShipmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.Instant;


@Entity
@Table(name = "Shipments")
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address;
    private String phone;
    private ShipmentStatus status;


    @Column(name = "shipping_date", nullable = false)
    private Date shippingDate;

    @JoinColumn(name = "user_id",nullable = true)
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private User shipper;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sale_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private Sale sale;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

}
