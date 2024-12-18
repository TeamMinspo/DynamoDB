import {useNavigate} from 'react-router-dom'
import {ChangeEvent, useEffect, useState} from 'react'
import {OrderStatus, ReceivedOrder} from '../model/ReceivedOrder.ts'
import Contents from '../components/Contents.tsx'
import OrderCard from '../components/OrderCard.tsx'
import List from '../components/List.tsx'

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
    <Contents>
      <h2>管理者ページ</h2>
      <button onClick={() => navigate("/")}>戻る</button>
      <List gap={8}>
        {orders.map(order => (
          <OrderCard
            key={order.id}
            order={order}
            isStatusEditable={true}
            onChangeStatus={onChangeStatus}
          />
        ))}
      </List>
    </Contents>
  )
}

export default AdminPage