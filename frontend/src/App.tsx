import { useEffect, useState } from 'react'
import api from './services/api'
import TableFloor from './components/TableFloor'
import Filtering from './components/Filtering'

interface SeatingType {
  id: number
  type: string
}

interface Seating {
    id: number,
    name: string,
    typeId: number,
    maxPeople: number
}

interface SeatingFilterResult {
  id: number
  matchesFilter: boolean
}

function App() {
  const [seatingTypes, setSeatingTypes] = useState<SeatingType[]>([])
  const [seatings, setSeatings] = useState<Seating[]>([])
  const [filterResults, setFilterResults] = useState<SeatingFilterResult[]>([])
  const [bookedIds, setBookedIds] = useState<number[]>([]);

  useEffect(() => {
    api.get<SeatingType[]>('/seating-type')
      .then(response => setSeatingTypes(response.data))
      .catch(error => console.error(error))
  }, [])

    useEffect(() => {
        api.get<Seating[]>('/seating')
            .then(response => {console.log(response.data)
                setSeatings(response.data)})
            .catch(error => console.log(error))
    }, []);

  return (
    <>
      <h1 style={{ padding: '16px 24px', margin: 0 }}>Restaurant Floor</h1>
      <Filtering seatingTypes={seatingTypes} onFilterResults={setFilterResults} onFilterBookedResults={setBookedIds} />
      <TableFloor seatings={seatings} seatingTypes={seatingTypes} filterResults={filterResults} filterResultsBooked={bookedIds} />
    </>
  )
}

export default App
