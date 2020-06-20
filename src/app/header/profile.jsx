import React from 'react';
import './scss/headerBar.scss';

export default function Profile() {
  return (
    <div className="profile" data-testId="header-profile">
      <div className="photo" data-testId="profile-photo">
        <i className="ri-user-3-fill" />
      </div>
      <span data-testId="profile-name">User Name</span>
    </div>
  );
}
