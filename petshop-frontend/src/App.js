import "./App.css";
import React, { useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [pets, setPets] = useState([]);
  const [newPet, setNewPet] = useState({ name: "", type: "", color: "", parents: {mother: "", father: "" }});

  function getPets() {
    axios
          .get("http://localhost:8080/pet")
          .then((response) => {
            setPets(response.data);
          })
          .catch((error) => {
            console.error("Error fetching pet data:", error);
          });
  }

  useEffect(() => {
    getPets();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === "mother") {
        setNewPet({ ...newPet, parents: { ...newPet.parents, mother: value } });
    } else if (name === "father") {
        setNewPet({ ...newPet, parents: { ...newPet.parents, father: value } });
    } else {
        setNewPet({ ...newPet, [name]: value });
    }
  };

  const handleAddPet = () => {
    axios
      .post("http://localhost:8080/pet", newPet)
      .then((response) => {
        getPets();
        //setPets([...pets, response.data]);
        setNewPet({ name: "", type: "", color: "", parents: {mother: "", father: "" }});
      })
      .catch((error) => {
        console.error("Error adding pet:", error);
      });
  };

  const isAddPetDisabled = !newPet.name || !newPet.type || !newPet.color;

  return (
    <div style={{ padding: "20px" }}>
      <h1>Petshop</h1>
      <div>
        <h2>Add a New Pet</h2>
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={newPet.name}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="type"
          placeholder="Type"
          value={newPet.type}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="color"
          placeholder="Color"
          value={newPet.color}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="mother"
          placeholder="Mother (optional)"
          value={newPet.parents.mother}
          onChange={handleInputChange}
        />
        <input
          type="text"
          name="father"
          placeholder="Father (optional)"
          value={newPet.parents.father}
          onChange={handleInputChange}
        />
        <button onClick={handleAddPet} disabled={isAddPetDisabled}>Add Pet</button>
      </div>
      <table border="1" cellPadding="10" style={{ width: "100%", textAlign: "left" }}>
        <thead>
          <tr>
            <th>UUID</th>
            <th>Name</th>
            <th>Type</th>
            <th>Color</th>
            <th>Mother</th>
            <th>Father</th>
          </tr>
        </thead>
        <tbody>
          {pets.map((pet) => (
            <tr key={pet.uuid}>
              <td>{pet.uuid}</td>
              <td>{pet.name}</td>
              <td>{pet.type}</td>
              <td>{pet.color}</td>
              <td>{pet.parents && pet.parents.mother || "N/A"}</td>
              <td>{pet.parents && pet.parents.father || "N/A"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;