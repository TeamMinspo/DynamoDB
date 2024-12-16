export type PrimaryCategory = 'FOOD' | 'FASHION' | 'ELECTRONICS' | 'OFFICE'
export type SecondaryCategory = 'MEAT' | 'FISH' | 'DRINK' | 'NOODLE' | 'SNACK' | 'ALCOHOL' | 'TOPS' | 'OUTER' | 'BOTTOMS' | 'CAPS' | 'ACCESSORIES' | 'BAGS' | 'PC' | 'MONITOR' | 'KEYBOARD' | 'MOUSE' | 'DESK' | 'CHAIR' | 'STATIONERY'

export default interface Category {
  name: PrimaryCategory
  subCategories: SecondaryCategory[]
}