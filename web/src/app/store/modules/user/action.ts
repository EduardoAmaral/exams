import axios from 'axios';
import { AUTH } from '../../../config/endpoint';
import { Dispatch } from 'redux';

export const AUTH_AUTHENTICATED = {
  type: '@auth/authenticated',
};

export const AUTH_LOGOUT = {
  type: '@auth/logout',
};

export const login = () => async (dispatch: Dispatch) => {
  try {
    const response = await axios.get(AUTH);
    dispatch({
      type: '@auth/authenticated',
      payload: response.data,
    });
  } catch (err) {
    window.location.href = 'http://localhost:8081/oauth2/authorization/google';
  }
};

export const authenticate = () => async (dispatch: any) => {
  await dispatch(login());
};
