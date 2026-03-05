import { useEffect, useState } from 'react'
import api from './services/api'
import TableFloor from './components/TableFloor'
import Filtering from './components/Filtering'
import BookingModal from './components/BookingModal'

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
        Promise.all([
            api.get<SeatingType[]>('/seating-types'),
            api.get<Seating[]>('/seatings'),
            api.get<SeatingPreference[]>('/seatings/preferences')
        ])
            .then(([typesRes, seatingsRes, preferencesRes]) => {
                setSeatingTypes(typesRes.data)
                setSeatings(seatingsRes.data)
                setSeatingPreferences(preferencesRes.data)
            })
            .catch(error => alert(error.response?.data?.message ?? error.message))
    }, [])

    const handleBook = () => {
        if (!selectedSeatingId || !bookingTime) return;
        const formattedDate = `${bookingTime}:00`
        api.post('/reservations', { seatingId: selectedSeatingId, startTime: formattedDate })
            .then(() => {
                alert('Booked successfully!');
                setSelectedSeatingId(null);
                setRefreshKey(k => k + 1);
            })
            .catch(error => alert(error.response?.data?.message ?? error.message));
    };

  return (
    <>
      <h1 style={{ padding: '16px 24px', margin: 0 }}>Restaurant Floor</h1>
      <Filtering seatingTypes={seatingTypes} seatingPreferences={seatingPreferences} onFilterResults={setFilterResults} onFilterBookedResults={setBookedIds} onDateAndTimeChange={setBookingTime} refreshKey={refreshKey} />
      <TableFloor seatings={seatings} seatingTypes={seatingTypes} seatingPreferences={seatingPreferences} filterResults={filterResults} filterResultsBooked={bookedIds} onSelectSeating={setSelectedSeatingId} />

      {selectedSeatingId && (
        <BookingModal
          selectedSeatingId={selectedSeatingId}
          bookingTime={bookingTime}
          onConfirm={handleBook}
          onCancel={() => setSelectedSeatingId(null)}
        />
      )}
    </>
  )
}

export default App
