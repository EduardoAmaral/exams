import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './app/config/axiosConfig';
import './app/style/global-style.scss';

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);
