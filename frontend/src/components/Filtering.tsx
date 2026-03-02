import api from '../services/api'
import { useState, useEffect } from 'react'

interface SeatingType {
    id: number
    type: string
}

interface SeatingPreference {
    id: number,
    name: string
}

interface SeatingFilterResult {
    id: number
}

interface FilteringProps {
    seatingTypes: SeatingType[]
    seatingPreferences: SeatingPreference[]
    onFilterResults: (results: SeatingFilterResult[]) => void
    onFilterBookedResults: (results : number[]) => void
}

function Filtering({ seatingTypes, seatingPreferences, onFilterResults, onFilterBookedResults }: FilteringProps) {
    const [dateAndTime, setDateAndTime] = useState<string>('')
    const [numberOfPeople, setNumberOfPeople] = useState<number>(1)
    const [seatingTypeId, setSeatingTypeId] = useState<number | ''>('')
    const [selectedPreference, setSelectedPreference] = useState<number | ''>('')

    useEffect(() => {
        if (!dateAndTime) return

        const formattedDate = `${dateAndTime}:00`

        api.get<number[]>('/seating/filter/booked', {
            params: {
                dateAndTime: formattedDate
            }
        })
            .then(response => onFilterBookedResults(response.data))
            .catch(error => alert(error.response?.data?.message ?? error.message))

    }, [dateAndTime])

    const handleSuggest = () => {
        if (!dateAndTime || !numberOfPeople) {
            alert('Date & Time and Number of People are required.')
            return
        }

        const formattedDate = dateAndTime ? `${dateAndTime}:00` : undefined

        api.get<SeatingFilterResult[]>('/seating/filter/suggest', {
            params: {
                ...(formattedDate && {dateAndTime: formattedDate}),
                numberOfPeople,
                ...(seatingTypeId !== '' && {seatingTypeId}),
                ...(selectedPreference !== '' && {selectedPreference})
            }
        })
            .then(response => onFilterResults(response.data))
            .catch(error => alert(error.response?.data?.message ?? error.message))
    }

    const handleFilter = () => {
        const formattedDate = dateAndTime ? `${dateAndTime}:00` : undefined

        api.get<SeatingFilterResult[]>('/seating/filter', {
            params: {
                ...(formattedDate && {dateAndTime: formattedDate}),
                numberOfPeople,
                ...(seatingTypeId !== '' && {seatingTypeId}),
                ...(selectedPreference !== '' && {selectedPreference})
            }
        })
            .then(response => onFilterResults(response.data))
            .catch(error => alert(error.response?.data?.message ?? error.message))

    }

    return (
        <div>
            <div>
                <label>
                    Date & Time:
                    <input
                        type="datetime-local"
                        value={dateAndTime}
                        onChange={e => setDateAndTime(e.target.value)}
                    />
                </label>

                <label>
                    Number of People:
                    <input
                        type="number"
                        min={1}
                        value={numberOfPeople}
                        onChange={e => setNumberOfPeople(Number(e.target.value))}
                    />
                </label>

                <label>
                    Seating Type:
                    <select
                        value={seatingTypeId}
                        onChange={e => setSeatingTypeId(e.target.value === '' ? '' : Number(e.target.value))}
                    >
                        <option value="">Any</option>
                        {seatingTypes.map(st => (
                            <option key={st.id} value={st.id}>{st.type}</option>
                        ))}
                    </select>
                </label>

                <label>
                    Seating preferences:
                    <select
                        value={selectedPreference}
                        onChange={e => setSelectedPreference(e.target.value === '' ? '' : Number(e.target.value))}
                    >
                        <option value="">Any</option>
                        {seatingPreferences.map(sp => (
                            <option key={sp.id} value={sp.id}>{sp.name}</option>
                        ))}
                    </select>
                </label>

                <button onClick={handleFilter}>Filter</button>
                <button onClick={handleSuggest}>Suggest a table</button>
            </div>
        </div>
    )
}

export default Filtering
