package org.pet.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Pet {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String name;
    private String type;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mother_uuid")
    private Pet mother;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father_uuid")
    private Pet father;
}