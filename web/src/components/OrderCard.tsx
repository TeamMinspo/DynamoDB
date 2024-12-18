import styles from './OrderCard.module.scss'
import ItemCard from './ItemCard.tsx'
import {ReceivedOrder} from '../model/ReceivedOrder.ts'
import {ChangeEvent} from 'react'

interface OrderCardProps {
  order: ReceivedOrder,
  isStatusEditable?: boolean,
  onChangeStatus?: (event: ChangeEvent<HTMLSelectElement>, order: ReceivedOrder) => void,
}

const OrderCard = (
  {
    order,
    isStatusEditable = false,
    onChangeStatus = () => {}
  }: OrderCardProps
) => {
  return (
    <div className={styles.order} key={order.id}>
      <div className={styles.orderInfo}>
        <div>
          <div>注文者: {order.customerName}</div>
          <div>注文ID: {order.id}</div>
        </div>

        {isStatusEditable
          ? (
            <select value={order.status} onChange={(e) => onChangeStatus(e, order)}>
              <option value="RECEIVED">注文受付</option>
              <option value="SHIPPED">発送済み</option>
              <option value="DELIVERED">配達済み</option>
            </select>
          )
          : (<div>{order.status}</div>)
        }

      </div>

      <div className={styles.itemContainer}>
        {order.items.map(cartItem => (
          <ItemCard key={cartItem.item.id} item={cartItem.item} count={cartItem.count}/>
        ))}
      </div>
    </div>
  )
}

export default OrderCard