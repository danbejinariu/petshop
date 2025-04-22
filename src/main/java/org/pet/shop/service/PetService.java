package org.pet.shop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.pet.shop.dto.PetDto;
import org.pet.shop.entity.Pet;
import org.pet.shop.mapper.PetMapper;
import org.pet.shop.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetMapper petMapper;
    private final PetRepository petRepository;

    public Pet create(PetDto petDto) {
        Pet pet = petMapper.toEntity(petDto);
        return petRepository.save(pet);
    }

    public PetDto findByUuid(UUID uuid) {
        Pet pet = findEntityByUuid(uuid);
        return petMapper.toDto(pet);
    }

    public List<PetDto> findAll() {
        return petRepository.findAll().stream()
                .map(petMapper::toDto)
                .toList();
    }

    public void updatePet(PetDto updatedPet) {
        updatedPet.checkUuid();
        Pet existingPet = findEntityByUuid(updatedPet.uuid());
        updateAllFields(updatedPet, existingPet);
        petRepository.save(existingPet);
    }

    private void updateAllFields(PetDto updatedPet, Pet existingPet) {
        existingPet.setName(updatedPet.name());
        existingPet.setType(updatedPet.type());
        existingPet.setColor(updatedPet.color());
        existingPet.setMother(petRepository.findById(updatedPet.parents().mother()).orElse(null));
        existingPet.setFather(petRepository.findById(updatedPet.parents().father()).orElse(null));
    }

    public void patch(PetDto partialUpdate) {
        partialUpdate.checkUuid();
        Pet pet = findEntityByUuid(partialUpdate.uuid());
        updatePartialFields(partialUpdate, pet);
        petRepository.save(pet);
    }

    private Pet findEntityByUuid(UUID uuid) {
        return petRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with uuid: " + uuid));
    }

    private void updatePartialFields(PetDto partialUpdate, Pet pet) {
        if (partialUpdate.name() != null) {
            pet.setName(partialUpdate.name());
        }
        if (partialUpdate.type() != null) {
            pet.setType(partialUpdate.type());
        }
        if (partialUpdate.color() != null) {
            pet.setColor(partialUpdate.color());
        }
        if (partialUpdate.parents() != null && partialUpdate.parents().mother() != null) {
            pet.setMother(petRepository.findById(partialUpdate.parents().mother()).orElse(null));
        }
        if (partialUpdate.parents() != null && partialUpdate.parents().father() != null) {
            pet.setFather(petRepository.findById(partialUpdate.parents().father()).orElse(null));
        }
    }
}
