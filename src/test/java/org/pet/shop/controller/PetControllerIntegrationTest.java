package org.pet.shop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pet.shop.dto.PetDto;
import org.pet.shop.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    public void resetDatabase() {
        petRepository.deleteAll();
    }

    @Test
    public void createPetEndpoint() {
        // Arrange
        PetDto pet = new PetDto(null, "Buddy", "Dog", "Brown", null);
        HttpEntity<PetDto> request = new HttpEntity<>(pet);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                "/pet",
                HttpMethod.POST,
                request,
                String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Pet created successfully");
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    @Test
    public void findByUuidEndpoint() {
        // Arrange
        PetDto pet = new PetDto(null, "Mittens", "Cat", "White", null);
        ResponseEntity<String> createRepspone = restTemplate.postForEntity("/pet", pet, String.class);
        String location = createRepspone.getHeaders().get(HttpHeaders.LOCATION).get(0);

        // Act
        ResponseEntity<PetDto> response = restTemplate.getForEntity(location, PetDto.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PetDto pets = response.getBody();
        assertThat(pets.name()).isEqualTo("Mittens");
        assertThat(pets.type()).isEqualTo("Cat");
        assertThat(pets.color()).isEqualTo("White");
    }

    @Test
    public void findAllEndpoint() {
        // Arrange
        PetDto pet1 = new PetDto(null, "Buddy", "Dog", "Brown", null);
        PetDto pet2 = new PetDto(null, "Mittens", "Cat", "White", null);

        restTemplate.postForEntity("/pet", pet1, String.class);
        restTemplate.postForEntity("/pet", pet2, String.class);

        // Act
        ResponseEntity<PetDto[]> response = restTemplate.getForEntity("/pet", PetDto[].class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        List<PetDto> pets = List.of(response.getBody());
        assertThat(pets).hasSize(2);
        assertThat(pets).extracting(PetDto::name).containsExactlyInAnyOrder("Buddy", "Mittens");
    }

    //@Test
    public void updateEndpoint() {
        // Arrange
        PetDto pet = new PetDto(null, "Buddy", "Dog", "Brown", null);

        // Persist the pet
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/pet", pet, String.class);
        assertThat(createResponse.getStatusCode().is2xxSuccessful()).isTrue();
        String petId = createResponse.getBody();
        assertThat(petId).isNotNull();

        // Update the pet's details
        PetDto updatedPet = new PetDto(UUID.fromString(petId), "Buddy Updated", "Dog", "Black", null);

        HttpEntity<PetDto> request = new HttpEntity<>(updatedPet);

        // Act
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                "/pet/" + petId,
                HttpMethod.PUT,
                request,
                String.class
        );

        // Assert
        assertThat(updateResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(updateResponse.getBody()).isEqualTo("Pet data updated successfully");

        // Verify the update
        ResponseEntity<PetDto[]> getResponse = restTemplate.getForEntity("/pets", PetDto[].class);
        assertThat(getResponse.getStatusCode().is2xxSuccessful()).isTrue();

        PetDto retrievedPet = getResponse.getBody()[0];
        assertThat(retrievedPet).isNotNull();
        assertThat(retrievedPet.name()).isEqualTo("Buddy Updated");
        assertThat(retrievedPet.color()).isEqualTo("Black");
        assertThat(retrievedPet.type()).isEqualTo("Dog");
    }
}