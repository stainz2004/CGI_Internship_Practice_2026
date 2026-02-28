import api from '../services/api'
import { useState } from 'react'

interface SeatingType {
    id: number
    type: string
}


interface SeatingFilterResult {
    id: number
    matchesFilter: boolean
}

interface FilteringProps {
    seatingTypes: SeatingType[]
    onFilterResults: (results: SeatingFilterResult[]) => void
}

function Filtering({ seatingTypes, onFilterResults }: FilteringProps) {
    const [dateAndTime, setDateAndTime] = useState<string>('')
    const [numberOfPeople, setNumberOfPeople] = useState<number>(1)
    const [seatingTypeId, setSeatingTypeId] = useState<number | ''>('')

    const handleFilter = () => {
        const formattedDate = dateAndTime ? `${dateAndTime}:00` : undefined

        if (numberOfPeople && dateAndTime) {
            api.get<SeatingFilterResult[]>('/seating/filter/suggest', {
                params: {
                    ...(formattedDate && {dateAndTime: formattedDate}),
                    numberOfPeople,
                    ...(seatingTypeId !== '' && {seatingTypeId}),
                }
            })
                .then(response => onFilterResults(response.data))
                .catch(error => console.log(error))
        } else {
            api.get<SeatingFilterResult[]>('/seating/filter', {
                params: {
                    ...(formattedDate && {dateAndTime: formattedDate}),
                    numberOfPeople,
                    ...(seatingTypeId !== '' && {seatingTypeId}),
                }
            })
                .then(response => onFilterResults(response.data))
                .catch(error => console.error(error))
        }
    }

    return (
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

            <button onClick={handleFilter}>Filter</button>
        </div>
    )
}

export default Filtering
