import { AUTH_AUTHENTICATED, AUTH_LOGOUT } from './action';

export default function cart(state = false, action) {
  switch (action.type) {
    case AUTH_AUTHENTICATED.type:
      return true;
    case AUTH_LOGOUT.type:
      return false;
    default:
      return state;
  }
}
