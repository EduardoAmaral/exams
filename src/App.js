import React, { useReducer } from 'react';
import axios from 'axios';
import { Provider } from 'react-redux';
import { Router } from 'react-router';
import { AUTH } from './app/config/endpoint';
import store from './app/store/store';
import reducer from './app/store/modules/auth/reducer';
import { AUTH_AUTHENTICATED } from './app/store/modules/auth/action';
import Routes from './routes';
import history from './app/config/history';
import './App.scss';
import HeaderBar from './app/header/headerBar';

const getAuthentication = (dispatch) => {
  axios.get(AUTH).then((response) => {
    if (response.data) {
      dispatch(AUTH_AUTHENTICATED);
    } else {
      window.location = 'http://localhost:8081/oauth2/authorization/google';
    }
  });
};

function App() {
  const [state, dispatch] = useReducer(reducer, false);

  return (
    <Provider store={store}>
      <div data-testid="app" className="app">
        <HeaderBar />
        <div className="content">
          <Router history={history}>
            {state ? <Routes /> : getAuthentication(dispatch)}
          </Router>
        </div>
      </div>
    </Provider>
  );
}

export default App;
