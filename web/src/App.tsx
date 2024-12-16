import {BrowserRouter, Outlet, Route, Routes} from "react-router-dom";
import styles from './App.module.scss'
import CategoryPage from "./pages/CategoryPage.tsx";
import ItemListPage from "./pages/ItemListPage.tsx";
import {createContext, ReactNode, useContext, useState} from "react";
import {CartItem} from "./model/CartItem.ts";

interface CartContextValue {
  cart: CartItem[],
  setCart: (newCartItems: CartItem[]) => void
}
const CartContext = createContext<CartContextValue>({cart: [], setCart: () => {}})
export const useCartContext = () => useContext(CartContext)

const CartContextProvider = ({children}: {children: ReactNode}) => {
  const [cart, setCart] = useState<CartItem[]>([])

  return (
    <CartContext.Provider value={{cart, setCart}}>
      {children}
    </CartContext.Provider>
  )
}

function App() {
  return (
    <CartContextProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Header/>}>
            <Route path="" element={<CategoryPage/>}/>
            <Route path="/:category" element={<ItemListPage/>}/>
          </Route>
        </Routes>
      </BrowserRouter>
    </CartContextProvider>
  )
}

const Header = () => {
  const {cart} = useCartContext()
  return (
    <>
      <div className={styles.header}>
        <div>Amazones</div>
        <div>カート{cart.reduce((sum, prev) => sum + prev.count, 0)}</div>
      </div>
      <Outlet/>
    </>
  )
}


export default App
