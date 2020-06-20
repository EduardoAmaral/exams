import React from 'react';
import './scss/headerBar.scss';

export default function HeaderBar() {
  return (
    <div className="header-bar" data-testId="header-bar">
      <h1>Exams</h1>
      <div className="profile" data-testId="header-profile">
        <div className="photo">
          <i className="ri-user-fill" />
        </div>
        User Name
      </div>
    </div>
  );
}
