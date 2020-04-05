import React, { useReducer } from 'react';
import axios from 'axios';
import { Provider } from 'react-redux';
import { AUTH } from './app/config/endpoint';
import store from './app/store/store';
import reducer from './app/store/modules/auth/reducer';
import { AUTH_AUTHENTICATED } from './app/store/modules/auth/action';

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
      <div className="ui container" data-testid="app">
        {state ? <span>Authenticated</span> : getAuthentication(dispatch)}
      </div>
    </Provider>
  );
}

export default App;
