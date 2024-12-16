import {PrimaryCategory, SecondaryCategory} from "./Category.ts";

export default interface Item {
  id: string,
  name: string
  price: number,
  quantity: number,
  primaryCategory: PrimaryCategory,
  secondaryCategory: SecondaryCategory,
}