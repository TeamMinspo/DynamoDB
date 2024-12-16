import {BrowserRouter, Outlet, Route, Routes} from "react-router-dom";
import styles from './App.module.scss'
import CategoryPage from "./pages/CategoryPage.tsx";
import ItemListPage from "./pages/ItemListPage.tsx";

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Header/>}>
          <Route path="" element={<CategoryPage/>}/>
          <Route path="/:category" element={<ItemListPage/>}/>
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

const Header = () => {
  return (
    <>
      <div className={styles.header}>
        Amazones
      </div>
      <Outlet/>
    </>
  )
}


export default App
