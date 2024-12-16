import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import styles from "./ItemListPage.module.scss";
import Item from "../model/Item.ts";
import {useCartContext} from "../App.tsx";
import Contents from "../components/Contents.tsx";

const ItemListPage = () => {
  const {category} = useParams()
  const [items, setItems] = useState<Item[]>([])
  const {cart, setCart} = useCartContext()
  const navigate = useNavigate()

  useEffect(() => {
    fetch(`api/items/${category}`)
      .then(res => res.json())
      .then(items => setItems(items))
  }, [category]);

  const onClickAddCart = (item: Item) => {
    const cartItemIndex = cart.findIndex(cartItem => cartItem.item.name === item.name)
    const sameItemAlreadyExistInCart = cartItemIndex >= 0
    if (sameItemAlreadyExistInCart) {
      console.log(cartItemIndex)
      cart[cartItemIndex].count += 1
      setCart([...cart])
    } else {
      setCart([...cart, {item, count: 1}])
    }
  }

  return (
    <Contents>
      <div className={styles.itemContainer}>
        <button onClick={() => navigate('/')} className={styles.backButton}>＜戻る</button>
        {items.map(item => (
          <div className={styles.itemCard} key={item.id}>
            <div className={styles.thumbnail}></div>

            <div className={styles.itemInfo}>
              <div>{item.name}</div>
              <div>価格: {item.price}</div>
              <div>在庫: {item.quantity}</div>
            </div>

            <div className={styles.buttonContainer}>
              <button onClick={() => onClickAddCart(item)}>カートに入れる</button>
            </div>
          </div>
        ))}
      </div>
    </Contents>
  )
}

export default ItemListPage