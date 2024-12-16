import {useEffect, useState} from "react";
import Category from "../model/Category.ts";
import {useNavigate} from "react-router-dom";
import Contents from "../components/Contents.tsx";

const CategoryPage = () => {
  const [categories, setCategories] = useState<Category[]>([])
  const [expandedCategory, setExpandedCategory] = useState<Category | null>(null)
  const navigate = useNavigate()

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
    <Contents>
      {categories.map(category => (
        <div key={category.name} style={{border: '1px solid black'}}>
          <div onClick={() => handleOnClick(category)}>{category.name}</div>
          {expandedCategory == category && (
            <ul>
              {category.subCategories.map(subCategory => (
                <li key={subCategory}><button onClick={() => navigate(`/${subCategory}`)}>{subCategory}</button></li>
              ))}
            </ul>
          )}
        </div>
      ))}
    </Contents>
  )
}

export default CategoryPage