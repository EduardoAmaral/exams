import React, { useContext } from 'react';
import { AuthContext } from '../../context';
import style from './headerBar.module.scss';

export default function HeaderBar() {
  return (
    <header className={style.header}>
      <h1>Exams</h1>
      <Profile />
    </header>
  );
}

function Profile() {
  const context = useContext(AuthContext);

  return (
    <section className={style.profile}>
      <div className={style.photo}>
        <img src={context.user?.profileSrc} alt="profile" />
      </div>
      <span>{context.user?.name}</span>
    </section>
  );
}
