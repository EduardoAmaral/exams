import React from 'react';
import './loading.scss';

export default function Loading() {
  return (
    <div className="ui segment">
      <div className="ui active dimmer">
        <div className="ui medium text loader" data-testid="loading">
          Loading
        </div>
      </div>
      <p />
    </div>
  );
}
