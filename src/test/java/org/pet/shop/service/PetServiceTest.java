package org.pet.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pet.shop.dto.Parents;
import org.pet.shop.dto.PetDto;
import org.pet.shop.entity.Pet;
import org.pet.shop.mapper.PetMapper;
import org.pet.shop.repository.PetRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetMapper petMapper;

    @Captor
    private ArgumentCaptor<Pet> petCaptor;

    @Test
    void create() {
        // Given
        PetDto petDto = new PetDto(UUID.randomUUID(), "Buddy", "Dog", "Brown", new Parents(null, null));
        Pet pet = pet();
        when(petMapper.toEntity(petDto)).thenReturn(pet);
        when(petRepository.save(pet)).thenReturn(pet);

        // When
        Pet createdPet = petService.create(petDto);

        // Then
        assertNotNull(createdPet);
        verify(petMapper).toEntity(petDto);
        verify(petRepository).save(petCaptor.capture());

        assertEquals("Buddy", petCaptor.getValue().getName());
        assertEquals("Dog", petCaptor.getValue().getType());
        assertEquals("Brown", petCaptor.getValue().getColor());
    }

    private Pet pet() {
        Pet pet = new Pet();
        pet.setUuid(UUID.randomUUID());
        pet.setName("Buddy");
        pet.setType("Dog");
        pet.setColor("Brown");
        return pet;
    }

    @Test
    void findByUuid() {
        // Given
        UUID uuid = UUID.randomUUID();
        Pet pet = pet();
        PetDto petDto = new PetDto(uuid, "Buddy", "Dog", "Brown", new Parents(null, null));
        when(petRepository.findById(uuid)).thenReturn(Optional.of(pet));
        when(petMapper.toDto(pet)).thenReturn(petDto);

        // When
        PetDto result = petService.findByUuid(uuid);

        // Then
        assertNotNull(result);
        assertEquals(uuid, result.uuid());
        verify(petRepository).findById(uuid);
        verify(petMapper).toDto(pet);
    }

    @Test
    void findByUuidThrowsException() {
        // Given
        UUID uuid = UUID.randomUUID();
        when(petRepository.findById(uuid)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> petService.findByUuid(uuid));
        verify(petRepository).findById(uuid);
    }

    @Test
    void findAll() {
        // Given
        Pet pet = new Pet();
        PetDto petDto = new PetDto(UUID.randomUUID(), "Buddy", "Dog", "Brown", new Parents(null, null));
        when(petRepository.findAll()).thenReturn(List.of(pet));
        when(petMapper.toDto(pet)).thenReturn(petDto);

        // When
        List<PetDto> result = petService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(petRepository).findAll();
        verify(petMapper).toDto(pet);
    }

    @Test
    void updatePet() {
        // Given
        UUID uuid = UUID.randomUUID();
        PetDto updatedPetDto = new PetDto(uuid, "Buddy", "Dog", "Brown", new Parents(null, null));
        Pet existingPet = new Pet();
        when(petRepository.findById(uuid)).thenReturn(Optional.of(existingPet));

        // When
        petService.updatePet(updatedPetDto);

        // Then
        verify(petRepository).findById(uuid);
        verify(petRepository).save(existingPet);
    }

    @Test
    void patch() {
        // Given
        UUID uuid = UUID.randomUUID();
        PetDto partialUpdate = new PetDto(uuid, null, null, "White", null);
        Pet existingPet = new Pet();
        when(petRepository.findById(uuid)).thenReturn(Optional.of(existingPet));

        // When
        petService.patch(partialUpdate);

        // Then
        assertEquals("White", existingPet.getColor());
        verify(petRepository).findById(uuid);
        verify(petRepository).save(existingPet);
    }
}