package org.pet.shop.controller;

import lombok.RequiredArgsConstructor;
import org.pet.shop.dto.PetDto;
import org.pet.shop.entity.Pet;
import org.pet.shop.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<String> createPet(@RequestBody PetDto pet) {
        Pet createdPet = petService.create(pet);
        return ResponseEntity
                .created(URI.create("/pet/" + createdPet.getUuid()))
                .body("Pet created successfully");
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PetDto> findByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(petService.findByUuid(uuid));
    }

    @GetMapping
    public ResponseEntity<List<PetDto>> findAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody PetDto updatedPet) {
        petService.updatePet(updatedPet);
        return ResponseEntity.ok("Pet data updated successfully");
    }

    @PatchMapping
    public ResponseEntity<String> patch(@RequestBody PetDto partialUpdate) {
        petService.patch(partialUpdate);
        return ResponseEntity.ok("Pet data partially updated successfully");
    }
}