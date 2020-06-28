import React from 'react';
import { useSelector } from 'react-redux';

export default function Profile() {
  const user = useSelector((state) => state.user);

  return (
    <div className="profile" data-testid="header-profile">
      <div className="photo" data-testid="profile-photo">
        <img src={user?.profileSrc} alt="profile" />
      </div>
      <span data-testid="profile-name">{user?.name}</span>
    </div>
  );
}
