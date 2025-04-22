package org.pet.shop.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.pet.shop.dto.Parents;
import org.pet.shop.entity.Pet;
import org.pet.shop.repository.PetRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ParentsMapper {

    private final PetRepository petRepository;

    @Named("toEntity")
    public Pet toEntity(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return petRepository.findById(uuid).orElse(null);
    }

    @Named("toDto")
    public Parents toDto(Pet pet) {
        if (pet == null) {
            return null;
        }
        UUID motherUuid = pet.getMother() != null ? pet.getMother().getUuid() : null;
        UUID fatherUuid = pet.getFather() != null ? pet.getFather().getUuid() : null;
        return new Parents(motherUuid, fatherUuid);
    }
}
