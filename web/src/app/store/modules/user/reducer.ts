import { AUTH_AUTHENTICATED, AUTH_LOGOUT } from './action';

export default function userReducer(state = null, action: any) {
  switch (action.type) {
    case AUTH_AUTHENTICATED.type:
      return action.payload;
    case AUTH_LOGOUT.type:
      return null;
    default:
      return state;
  }
}
