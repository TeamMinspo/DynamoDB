import {CartItem} from "./CartItem.ts";

export type OrderStatus = 'RECEIVED' | 'SHIPPED' | 'DELIVERED'
export interface ReceivedOrder {
  id: string
  items: CartItem[],
  customerName: string,
  status: OrderStatus,
}