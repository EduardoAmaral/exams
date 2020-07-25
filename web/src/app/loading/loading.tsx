import React from 'react';
import './loading.scss';

interface Props {
  isLoading: boolean;
}

export default function Loading({ isLoading }: Props) {
  if (isLoading) {
    return (
      <div className="loading-container">
        <div className="loading" data-testid="loading">
          <div></div>
          <div></div>
          <div></div>
          <div></div>
        </div>
      </div>
    );
  }

  return <></>;
}
