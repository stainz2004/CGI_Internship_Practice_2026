import React from 'react'
import './TableFloor.css'

interface SeatingType {
  id: number
  type: string
}

interface Seating {
  id: number
  name: string
  typeId: number
  maxPeople: number
}

interface SeatingFilterResult {
  id: number
  matchesFilter: boolean
}

interface Props {
  seatings: Seating[]
  seatingTypes: SeatingType[]
  filterResults: SeatingFilterResult[]
  filterResultsBooked: number[]
}

function TableFloor({ seatings, seatingTypes, filterResults, filterResultsBooked }: Props) {

  const getTypeName = (typeId: number) => {
    return seatingTypes.find(t => t.id === typeId)?.type ?? 'Unknown'
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
          <span className="table-capacity">👥 {seating.maxPeople}</span>
        </div>
      ))}
    </div>
  )
}

export default TableFloor

