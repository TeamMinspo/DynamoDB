import Item from '../model/Item.ts'
import {ReactNode} from 'react'
import styles from './ItemCard.module.scss'

interface ItemCardProps {
  item: Item
  actionButton?: ReactNode
  count?: number
}

const ItemCard = ({item, actionButton, count}: ItemCardProps) => {
  return (
    <div className={styles.itemCard} key={item.id}>
      <div className={styles.thumbnail}></div>

      <div className={styles.itemInfo}>
        <div>{item.name}</div>
        <div>価格: {item.price}</div>
        <div>在庫: {item.quantity}</div>
        {count &&
            <div>数量: {count}</div>
        }
      </div>

      <div className={styles.buttonContainer}>
        {actionButton}
      </div>
    </div>
  )
}

export default ItemCard