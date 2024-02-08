import { BrowserRouter, Routes, Route } from "react-router-dom";
import Formulaire from './Formulaire'
import Resultats from "./Resultats";

function App() {
  return (
    <BrowserRouter basename='/'>
      <Routes>
        <Route path='/' element={<Formulaire />} />
        <Route path="/resultats" element={<Resultats />} />
      </Routes>
    </BrowserRouter>
  )

}

export default App
