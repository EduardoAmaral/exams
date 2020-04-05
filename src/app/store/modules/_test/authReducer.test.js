import store from '../../store';
import { AUTH_AUTHENTICATED, AUTH_LOGOUT } from '../auth/action';

describe('Auth Reducer', () => {
  it('should return false has initial value', () => {
    const { auth } = store.getState();
    expect(auth).toBeFalsy();
  });

  it('authenticated action should return true', () => {
    store.dispatch(AUTH_AUTHENTICATED);

    const { auth } = store.getState();

    expect(auth).toBeTruthy();
  });

  it('logout action should return false', () => {
    store.dispatch(AUTH_LOGOUT);

    const { auth } = store.getState();

    expect(auth).toBeFalsy();
  });
});
