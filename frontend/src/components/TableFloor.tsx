import React from 'react'
import './TableFloor.css'

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
  matchesFilter: boolean
}

interface Props {
  seatings: Seating[]
  seatingTypes: SeatingType[]
  seatingPreferences: SeatingPreference[]
  filterResults: SeatingFilterResult[]
  filterResultsBooked: number[]
}

function TableFloor({ seatings, seatingTypes, seatingPreferences, filterResults, filterResultsBooked }: Props) {

  const getTypeName = (typeId: number) => {
    return seatingTypes.find(t => t.id === typeId)?.type ?? 'Unknown'
  }

  const getSeatingPreferenceName = (preferenceId: number) => {
    return seatingPreferences.find(p => p.id === preferenceId)?.name ?? 'Unknown'
  }

  const getCardStyle = (id: number): React.CSSProperties => {
    if (filterResultsBooked.includes(id)) return { backgroundColor: '#f7c5c5' }
    if (filterResults.length === 0) return {}
    const result = filterResults.find(r => r.id === id)
    if (!result?.matchesFilter) return {}
    return { backgroundColor: '#c8f7c5'}
  }

  return (
    <div className="floor-grid">
      {seatings.map(seating => (
        <div key={seating.id} className="table-card" style={getCardStyle(seating.id)}>
          <span className="table-name">{seating.name}</span>
          <span className="table-type">{getTypeName(seating.typeId)}</span>
          <span className="table-preferences">
            {seating.preferenceIds.map(id => getSeatingPreferenceName(id)).join(', ')}
          </span>
          <span className="table-capacity">👥 {seating.maxPeople}</span>
        </div>
      ))}
    </div>
  )
}

export default TableFloor

