import React from 'react';
import Profile from './profile';
import './headerBar.scss';

export default function HeaderBar() {
  return (
    <div className="header-bar" data-testid="header-bar">
      <h1>Exams</h1>
      <Profile />
    </div>
  );
}
