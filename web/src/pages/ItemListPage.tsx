import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import styles from "./ItemListPage.module.scss";
import Item from "../model/Item.ts";

const ItemListPage = () => {
  const {category} = useParams()
  const [items, setItems] = useState<Item[]>([])

  useEffect(() => {
    fetch(`api/items/${category}`)
      .then(res => res.json())
      .then(items => setItems(items))
  }, [category]);

  return (
    <div className={styles.itemContainer}>
      {items.map(item => (
        <div className={styles.itemCard}>
          <div className={styles.thumbnail}></div>

          <div className={styles.itemInfo}>
            <div key={item.name}>{item.name}</div>
            <div key={item.name}>価格: {item.price}</div>
            <div key={item.name}>在庫: {item.quantity}</div>
          </div>

          <div className={styles.buttonContainer}>
            <button>カートに入れる</button>
          </div>
        </div>
      ))}
    </div>
  )
}

export default ItemListPage