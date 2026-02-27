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

interface Props {
  seatings: Seating[]
  seatingTypes: SeatingType[]
}

function TableFloor({ seatings, seatingTypes }: Props) {
  const getTypeName = (typeId: number) => {
    return seatingTypes.find(t => t.id === typeId)?.type ?? 'Unknown'
  }

  return (
    <div className="floor-grid">
      {seatings.map(seating => (
        <div key={seating.id} className="table-card">
          <span className="table-name">{seating.name}</span>
          <span className="table-type">{getTypeName(seating.typeId)}</span>
          <span className="table-capacity">👥 {seating.maxPeople}</span>
        </div>
      ))}
    </div>
  )
}

export default TableFloor

