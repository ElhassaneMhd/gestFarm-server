package Gestfarm.Model;

import Gestfarm.Enum.SheepAge;
import Gestfarm.Enum.SheepStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "sheep")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sheep {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;

    @Column(unique = true)
    private Integer number;
    private Integer weight;
    private SheepAge age;
    private SheepStatus status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private Category category;

    @ManyToOne(cascade = CascadeType.PERSIST)
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
