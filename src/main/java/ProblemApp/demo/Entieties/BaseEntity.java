package ProblemApp.demo.Entieties;

import jakarta.persistence.Column;
import jakarta.persistence.PreUpdate;

import java.time.Instant;

public abstract class BaseEntity {

    @Column(nullable=false) protected Instant createdTime = Instant.now();
    @Column(nullable=false) protected Instant updatedTime = Instant.now();
    @PreUpdate
    public void onUpdate(){ this.updatedTime = Instant.now(); }
    public Instant getCreatedTime(){ return createdTime; }
    public Instant getUpdatedTime(){ return updatedTime; }

}
