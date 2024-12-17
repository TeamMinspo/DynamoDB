import {BrowserRouter, Outlet, Route, Routes, useNavigate} from "react-router-dom";
import styles from './App.module.scss'
import CategoryPage from "./pages/CategoryPage.tsx";
import ItemListPage from "./pages/ItemListPage.tsx";
import {ReactNode, useState} from "react";
import {CartItem} from "./model/CartItem.ts";
import {CartContext, useCartContext} from "./CartContext.ts";
import CartPage from "./pages/CartPage.tsx";
import InquiryPage from "./pages/InquiryPage.tsx";
import AdminPage from "./pages/AdminPage.tsx";


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
            <Route path="/cart" element={<CartPage />}/>
            <Route path="/inquiry" element={<InquiryPage />}/>
            <Route path="/admin" element={<AdminPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </CartContextProvider>
  )
}

const Header = () => {
  const {cart} = useCartContext()
  const navigate = useNavigate()

  function getCountOfItemsInCart() {
    return cart.reduce((sum, prev) => sum + prev.count, 0);
  }

  return (
    <>
      <div className={styles.header}>
        <div>Amazones</div>
        <div onClick={() => navigate("/cart")}>カート{getCountOfItemsInCart()}</div>
        <div onClick={() => navigate("inquiry")}>商品問い合わせ</div>
      </div>
      <Outlet/>
    </>
  )
}


export default App
