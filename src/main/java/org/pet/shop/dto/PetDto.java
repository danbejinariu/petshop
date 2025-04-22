package org.pet.shop.dto;

import java.util.UUID;

public record PetDto(UUID uuid,
                     String name,
                     String type,
                     String color,
                     Parents parents) {

    public void checkUuid() {
        if (uuid == null) {
            throw new IllegalArgumentException("Pet UUID must not be null");
        }
    }
}
