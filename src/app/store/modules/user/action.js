import axios from 'axios';
import { AUTH } from '../../../config/endpoint';

export const AUTH_AUTHENTICATED = {
  type: '@auth/authenticated',
};

export const AUTH_LOGOUT = {
  type: '@auth/logout',
};

export const login = () => async (dispatch) => {
  try {
    const response = await axios.get(AUTH);
    dispatch({
      type: '@auth/authenticated',
      payload: response.data,
    });
  } catch (err) {
    window.location = 'http://localhost:8081/oauth2/authorization/google';
  }
};

export const authenticate = () => async (dispatch) => {
  await dispatch(login());
};
