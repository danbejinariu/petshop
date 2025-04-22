package org.pet.shop.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pet.shop.dto.Parents;
import org.pet.shop.dto.PetDto;
import org.pet.shop.entity.Pet;
import org.pet.shop.mapper.ParentsMapper;
import org.pet.shop.mapper.PetMapper;
import org.pet.shop.repository.PetRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetMapperTest {

    private PetMapper petMapper;

    @InjectMocks
    private ParentsMapper parentsMapper;

    @Mock
    private PetRepository petRepository;

    @BeforeEach
    void setUp() {
        petMapper = Mappers.getMapper(PetMapper.class);
        ReflectionTestUtils.setField(petMapper, "parentsMapper", parentsMapper);
    }

    @Test
    void testToEntity() {
        // Given
        UUID uuid = UUID.randomUUID();
        UUID motherUuid = UUID.randomUUID();
        UUID fatherUuid = UUID.randomUUID();
        PetDto petDto = new PetDto(uuid, "Buddy", "Dog", "Brown", new Parents(motherUuid, fatherUuid));

        when(petRepository.findById(motherUuid)).thenReturn(Optional.of(pet(motherUuid)));
        when(petRepository.findById(fatherUuid)).thenReturn(Optional.of(pet(fatherUuid)));

        // When
        Pet pet = petMapper.toEntity(petDto);

        // Then
        assertEquals(uuid, pet.getUuid());
        assertEquals("Buddy", pet.getName());
        assertEquals("Dog", pet.getType());
        assertEquals("Brown", pet.getColor());
        assertEquals(motherUuid, pet.getMother().getUuid());
        assertEquals(fatherUuid, pet.getFather().getUuid());
    }

    @Test
    void testToDto() {
        // Given
        UUID uuid = UUID.randomUUID();
        UUID motherUuid = UUID.randomUUID();
        UUID fatherUuid = UUID.randomUUID();

        Pet pet = new Pet();
        pet.setUuid(uuid);
        pet.setName("Buddy");
        pet.setType("Dog");
        pet.setColor("Brown");
        pet.setMother(pet(motherUuid));
        pet.setFather(pet(fatherUuid));

        // When
        PetDto petDto = petMapper.toDto(pet);

        // Then
        assertEquals(uuid, petDto.uuid());
        assertEquals("Buddy", petDto.name());
        assertEquals("Dog", petDto.type());
        assertEquals("Brown", petDto.color());
        assertEquals(motherUuid, petDto.parents().mother());
        assertEquals(fatherUuid, petDto.parents().father());
    }

    private Pet pet(UUID uuid) {
        Pet pet = new Pet();
        pet.setUuid(uuid);
        return pet;
    }
}