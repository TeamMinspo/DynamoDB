import {useNavigate} from "react-router-dom";
import {ChangeEvent, useState} from "react";
import Contents from "../components/Contents.tsx";
import {ReceivedOrder} from "../model/ReceivedOrder.ts";


function InquiryPage() {
  const navigate = useNavigate()
  const [orderId, setOrderId] = useState<string>("")
  const [receivedOrder, setReceivedOrder] = useState<ReceivedOrder | null>(null)

  const onChangeInput = (e: ChangeEvent<HTMLInputElement>) => {
    setOrderId(e.target.value)
  }

  const handleInquiry = () => {
    fetch(`/api/orders/${orderId}`)
      .then(res => {
        switch (res.status) {
          case 400:
            window.alert("正しい注文番号を入力して下さい")
            return Promise.reject()
          case 404:
            window.alert("注文が見つかりませんでした")
            return Promise.reject()
          case 200:
            return res.json()
        }
      })
      .then(order => setReceivedOrder(order))
  }

  return (
    <Contents>
      <div>問い合わせ</div>
      <button onClick={() => navigate("/")}>戻る</button>
      <label>
       注文番号：
        <input onChange={onChangeInput}/>
      </label>
      <button onClick={handleInquiry}>問い合わせ</button>
      {receivedOrder && (
        <div>
          {receivedOrder.items.map(item => (
            <div>{item.item.name}</div>
          ))}
          <div>{receivedOrder.status}</div>
        </div>
      )}
    </Contents>
  )
}

export default InquiryPage