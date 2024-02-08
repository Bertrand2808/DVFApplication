import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";

function Resultats() {
  const [points, setPoints] = useState([]);
  const location = useLocation();

  useEffect(() => {
    // Récupérer les paramètres de requête
    const queryParams = new URLSearchParams(location.search);
    const latitude = queryParams.get('latitude');
    const longitude = queryParams.get('longitude');
    const rayon = queryParams.get('rayon');

    // Construire l'URL pour la requête GET
    const apiUrl = `http://localhost:8080/api/transactions?latitude=${latitude}&longitude=${longitude}&rayon=${rayon}`;

    // Effectuer la requête GET
    fetch(apiUrl)
      .then(response => response.json())
      .then(data => {
        setPoints(data.points); // Stocker les points dans l'état
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }, [location.search]);

  return (
    <div>
      <h2>Résultats</h2>
      <ul>
        {points.map((point, index) => (
          <li key={index}>Latitude: {point.latitude}, Longitude: {point.longitude}</li>
        ))}
      </ul>
    </div>
  );
}

export default Resultats;
