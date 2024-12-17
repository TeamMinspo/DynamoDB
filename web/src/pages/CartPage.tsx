import {useCartContext} from "../CartContext.ts";
import {useNavigate} from "react-router-dom";
import {CartItem} from "../model/CartItem.ts";
import Contents from "../components/Contents.tsx";
import styles from "./CartPage.module.scss"
import {ChangeEvent, useState} from "react";

interface Order {
  items: CartItem[]
  customerName: string
}

const CartPage = () => {
  const {cart, setCart} = useCartContext()
  const navigate = useNavigate()
  const [customerName, setCustomerName] = useState<string>("")

  const onClickBackButton = () => {
    navigate("/")
  }

  const onClickDelete = (deleteCartItem: CartItem) => {
    const newCart = cart.filter(cartItem => cartItem.item.name !== deleteCartItem.item.name)
    setCart(newCart)
  }

  const onChangeInput = (e: ChangeEvent<HTMLInputElement>) => {
    setCustomerName(e.target.value)
  }

  const onClickSubmitOrderButton = () => {
    if (customerName === "") {
      window.alert("お客様のお名前を入力してね")
      return
    }
    const order: Order = {items: cart, customerName}
    const body = JSON.stringify(order)
    const headers = {'Content-Type': 'application/json'}
    fetch('/api/orders', {method: "POST", body, headers})
      .then((res) => {
        switch (res.status) {
          case 201:
            return res.json()
          default:
            return Promise.reject()
        }
      })
      .then(orderId => {
          window.alert(`注文しました（注文番号: ${orderId}）`)
          setCart([])
      })
      .catch(() => {window.alert("注文できませんでした")})
  }

  return (
    <Contents>
      <h2>cart</h2>
      <button onClick={onClickBackButton}>戻る</button>
      <div className={styles.itemContainer}>
        {cart.map(cartItem => (
          <div className={styles.cartItem} key={cartItem.item.id}>
            <div className={styles.thumbnail}></div>
            <div className={styles.itemInfo}>
              <div>{cartItem.item.name}</div>
              <div>数量: {cartItem.count}</div>
            </div>
            <div className={styles.buttonContainer}>
              <button className={styles.deleteButton} onClick={() => onClickDelete(cartItem)}>削除</button>
            </div>
          </div>
        ))}
      </div>

      <label>
        お名前:
        <input onChange={onChangeInput}/>
      </label>
      <button onClick={onClickSubmitOrderButton}>注文</button>
    </Contents>
  )
}

export default CartPage