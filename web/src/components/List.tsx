import {ReactNode} from 'react'


const List = ({children, gap}: {children: ReactNode, gap: number}) => {
  return (
    <div style={{display: 'flex', flexDirection: 'column', gap: `${gap}px`}}>
      {children}
    </div>
  )
}

export default List