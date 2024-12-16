import {ReactNode} from "react";
import styles from './Contents.module.scss'


const Contents = ({children}: {children: ReactNode}) => {
  return (
    <div className={styles.contents}>{children}</div>
  )
}

export default Contents