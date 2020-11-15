import Axios from 'axios';
import React, { useContext, useState } from 'react';
import { Router } from 'react-router';
import './App.scss';
import { AUTH } from './app/config/endpoint';
import history from './app/config/history';
import HeaderBar from './app/header/headerBar';
import User from './app/types/User';
import { AuthContext } from './context';
import Routes from './routes';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';

const Authenticated = () => {
  const context = useContext(AuthContext);

  const prefersDarkMode = useMediaQuery('(prefers-color-scheme: dark)');

  const theme = React.useMemo(
    () =>
      createMuiTheme({
        palette: {
          type: 'dark',
        },
      }),
    [prefersDarkMode]
  );

  if (context.user) {
    return (
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <div data-testid="app" className="app">
          <HeaderBar />
          <div className="content">
            <Router history={history}>
              <Routes />
            </Router>
          </div>
        </div>
      </ThemeProvider>
    );
  }

  return <></>;
};

const Authentication = () => {
  const context = useContext(AuthContext);

  if (!context.user) {
    Axios.get(AUTH)
      .then((response) => {
        context.login && context.login(response.data);
      })
      .catch(() => {
        window.location.replace(
          'http://localhost:8081/oauth2/authorization/google'
        );
      });
  }

  return <></>;
};

export default function App(): JSX.Element {
  const [user, setUser] = useState<User>();

  return (
    <AuthContext.Provider
      value={{
        user: user,
        login: (user: User) => setUser(user),
      }}
    >
      <Authentication />
      <Authenticated />
    </AuthContext.Provider>
  );
}
