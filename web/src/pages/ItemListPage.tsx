import {useNavigate, useParams} from 'react-router-dom'
import {useEffect, useState} from 'react'
import Item from '../model/Item.ts'
import {useCartContext} from '../CartContext.ts'
import Contents from '../components/Contents.tsx'
import ItemCard from '../components/ItemCard.tsx'
import List from '../components/List.tsx'

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
      cart[cartItemIndex].count += 1
      setCart([...cart])
    } else {
      setCart([...cart, {item, count: 1}])
    }
  }

  return (
    <Contents>
      <List gap={8}>
        <button onClick={() => navigate('/')}>＜戻る</button>
        {items.map(item => (
          <ItemCard
            key={item.id}
            item={item}
            actionButton={<button onClick={() => onClickAddCart(item)}>カートに入れる</button>}
          />
        ))}
      </List>
    </Contents>
  )
}

export default ItemListPage