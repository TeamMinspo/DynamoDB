import {useEffect, useState} from "react";
import Category from "../model/Category.ts";

const CategoryPage = () => {
  const [categories, setCategories] = useState<Category[]>([])
  const [expandedCategory, setExpandedCategory] = useState<Category | null>(null)

  useEffect(() => {
    fetch('/api/items/categories')
      .then(res => {
        return res.json()
      })
      .then(categories => {
        setCategories(categories)
      })
  }, []);

  const handleOnClick = (category: Category) => {
    setExpandedCategory(category)
  }

  return (
    <>
      {categories.map(category => (
        <div key={category.name} style={{border: '1px solid black'}}>
          <div onClick={() => handleOnClick(category)}>{category.name}</div>
          {expandedCategory == category && (
            <ul>
              {category.subCategories.map(subCategory => (
                <li key={subCategory}><a href={`/${subCategory}`}>{subCategory}</a></li>
              ))}
            </ul>
          )}
        </div>
      ))}
    </>
  )
}

export default CategoryPage