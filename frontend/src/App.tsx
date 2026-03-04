import { useEffect, useState } from 'react'
import api from './services/api'
import TableFloor from './components/TableFloor'
import Filtering from './components/Filtering'

interface SeatingType {
  id: number
  type: string
}

interface SeatingPreference {
    id: number
    name: string
}

interface Seating {
    id: number
    name: string
    typeId: number
    maxPeople: number
    preferenceIds: number[]
}

interface SeatingFilterResult {
  id: number
}

function App() {
  const [seatingTypes, setSeatingTypes] = useState<SeatingType[]>([])
  const [seatings, setSeatings] = useState<Seating[]>([])
  const [seatingPreferences, setSeatingPreferences] = useState<SeatingPreference[]>([])
  const [filterResults, setFilterResults] = useState<SeatingFilterResult[]>([])
  const [bookedIds, setBookedIds] = useState<number[]>([]);
  const [selectedSeatingId, setSelectedSeatingId] = useState<number | null>(null);
  const [bookingTime, setBookingTime] = useState<string>('');
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    api.get<SeatingType[]>('/seating-types')
      .then(response => setSeatingTypes(response.data))
      .catch(error => alert(error.response?.data?.message ?? error.message))
  }, [])

    useEffect(() => {
        api.get<Seating[]>('/seatings')
            .then(response => {console.log(response.data)
                setSeatings(response.data)})
            .catch(error => alert(error.response?.data?.message ?? error.message))
    }, []);

    useEffect(() => {
        api.get<SeatingPreference[]>('/seatings/preferences')
            .then(response => setSeatingPreferences(response.data))
            .catch(error => alert(error.response?.data?.message ?? error.message))
    }, []);

    const handleBook = () => {
        if (!selectedSeatingId || !bookingTime) return;
        const formattedDate = `${bookingTime}:00`
        api.post('/reservations', { seatingId: selectedSeatingId, startTime: formattedDate })
            .then(() => {
                alert('Booked successfully!');
                setSelectedSeatingId(null);
                setBookingTime('');
                setRefreshKey(k => k + 1);
            })
            .catch(error => alert(error.response?.data?.message ?? error.message));
    };

  return (
    <>
      <h1 style={{ padding: '16px 24px', margin: 0 }}>Restaurant Floor</h1>
      <Filtering seatingTypes={seatingTypes} seatingPreferences={seatingPreferences} onFilterResults={setFilterResults} onFilterBookedResults={setBookedIds} refreshKey={refreshKey} />
      <TableFloor seatings={seatings} seatingTypes={seatingTypes} seatingPreferences={seatingPreferences} filterResults={filterResults} filterResultsBooked={bookedIds} onSelectSeating={setSelectedSeatingId} />

      {selectedSeatingId && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
          <div style={{ background: 'white', padding: '24px', borderRadius: '8px', boxShadow: '0 4px 12px rgba(0,0,0,0.2)', minWidth: '300px' }}>
            <h3 style={{ marginTop: 0 }}>Book Table #{selectedSeatingId}</h3>
            <label style={{ display: 'block', marginBottom: '8px' }}>Select date &amp; time:</label>
            <input
              type="datetime-local"
              value={bookingTime}
              onChange={e => setBookingTime(e.target.value)}
              style={{ width: '100%', padding: '6px', boxSizing: 'border-box' }}
            />
            <div style={{ marginTop: '16px', display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
              <button onClick={() => { setSelectedSeatingId(null); setBookingTime(''); }}>Cancel</button>
              <button onClick={handleBook} disabled={!bookingTime}>Confirm</button>
            </div>
          </div>
        </div>
      )}
    </>
  )
}

export default App
