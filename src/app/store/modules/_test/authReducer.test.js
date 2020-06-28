import store from '../../store';
import { AUTH_LOGOUT, AUTH_AUTHENTICATED } from '../user/action';

describe('Auth Reducer', () => {
  it('should return false has initial value', () => {
    const { user } = store.getState();
    expect(user).toBeNull();
  });

  it('authenticated action should return true', () => {
    store.dispatch({
      type: AUTH_AUTHENTICATED,
      payload: {},
    });

    const { user } = store.getState();

    expect(user).toBeDefined();
  });

  it('logout action should return false', () => {
    store.dispatch(AUTH_LOGOUT);

    const { user } = store.getState();

    expect(user).toBeNull();
  });
});
