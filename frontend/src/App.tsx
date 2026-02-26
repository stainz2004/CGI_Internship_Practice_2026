import { useEffect, useState } from 'react'
import api from './services/api'

interface SeatingType {
  id: number
  type: string
}

function App() {
  const [seatingTypes, setSeatingTypes] = useState<SeatingType[]>([])

  useEffect(() => {
    api.get<SeatingType[]>('/seating-type')
      .then(response => setSeatingTypes(response.data))
      .catch(error => console.error(error))
  }, [])

  return (
    <>
      <p>Hello</p>
      <ul>
        {seatingTypes.map(seatingType => (
          <li key={seatingType.id}>{seatingType.type}</li>
        ))}
      </ul>
    </>
  )
}

export default App
