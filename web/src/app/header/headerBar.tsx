import React from 'react';
import { useSelector } from 'react-redux';
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
  const user = useSelector((state: any) => state.user);

  return (
    <section className={style.profile}>
      <div className={style.photo}>
        <img src={user?.profileSrc} alt="profile" />
      </div>
      <span>{user?.name}</span>
    </section>
  );
}
