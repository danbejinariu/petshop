package org.pet.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.pet.shop.dto.PetDto;
import org.pet.shop.entity.Pet;

@Mapper(componentModel = "spring", uses = ParentsMapper.class)
public interface PetMapper {

    @Mapping(target = "mother", source = "parents.mother", qualifiedByName = "toEntity")
    @Mapping(target = "father", source = "parents.father", qualifiedByName = "toEntity")
    Pet toEntity(PetDto petDto);

    @Mapping(target = "parents", source = "pet", qualifiedByName = "toDto")
    PetDto toDto(Pet pet);
}