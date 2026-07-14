package com.ecommerce.profiles.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
public abstract class BaseEntity {

    @Id
    @UuidGenerator(style = VERSION_7)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    protected void setId(UUID id) {
        this.id = id;
    }

}
