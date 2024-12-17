import {CartItem} from "./model/CartItem.ts";
import {createContext, useContext} from "react";

interface CartContextValue {
  cart: CartItem[],
  setCart: (newCartItems: CartItem[]) => void
}
export const CartContext = createContext<CartContextValue>({cart: [], setCart: () => {}})
export const useCartContext = () => useContext(CartContext)
