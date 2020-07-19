import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Router } from 'react-router';
import './App.scss';
import history from './app/config/history';
import HeaderBar from './app/header/headerBar';
import { authenticate } from './app/store/modules/user/action';
import Routes from './routes';

const Authenticated = () => {
  return (
    <div data-testid="app" className="app">
      <HeaderBar />
      <div className="content">
        <Router history={history}>
          <Routes />
        </Router>
      </div>
    </div>
  );
};

const Login = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(authenticate());
  }, [dispatch]);

  return <div data-testid="login">Login</div>;
};

export default function App() {
  const user = useSelector((state: any) => state.user);

  return user ? <Authenticated /> : <Login />;
}
