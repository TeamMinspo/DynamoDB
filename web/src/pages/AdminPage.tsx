import {useNavigate} from "react-router-dom";
import {ChangeEvent, useEffect, useState} from "react";
import {OrderStatus, ReceivedOrder} from '../model/ReceivedOrder.ts'

const AdminPage = () => {
  const navigate = useNavigate()
  const [orders, setOrders] = useState<ReceivedOrder[]>([])

  useEffect(() => {
    fetch("/api/orders")
      .then(res => res.json())
      .then(receivedOrders => setOrders(receivedOrders))
  }, []);

  const onChangeStatus = (event: ChangeEvent<HTMLSelectElement>, targetOrder: ReceivedOrder) => {
    const updatedOrder: ReceivedOrder = {...targetOrder, status: event.target.value as OrderStatus}
    const body = JSON.stringify(updatedOrder)
    const headers = {"Content-Type": "application/json"}
    fetch(`/api/orders/${targetOrder.id}`, {method: "PATCH", body, headers})
      .then(res => {
        if (res.status === 200) {
          window.alert("保存しました")
          const newOrders = orders.map(order => {
            if (order.id === targetOrder.id) {
              return updatedOrder
            }
            return order
          })
          setOrders(newOrders)
        } else {
          return Promise.reject()
        }
      })
      .catch(() => window.alert("保存できませんでした"))
  }

  return (
    <>
      <button onClick={() => navigate("/")}>戻る</button>
      {orders.map(order => (
        <div key={order.id}>
          <div>{order.customerName}</div>
          {order.items.map(cartItem => (
            <div key={cartItem.item.id}>
              <div>{cartItem.item.name}</div>
              <div>{cartItem.count}</div>
            </div>
          ))}
          <select value={order.status} onChange={(e) => onChangeStatus(e, order)}>
            <option value="RECEIVED">注文受付</option>
            <option value="SHIPPED">発送済み</option>
            <option value="DELIVERED">配達済み</option>
          </select>
        </div>
      ))}
    </>
  )
}

export default AdminPage