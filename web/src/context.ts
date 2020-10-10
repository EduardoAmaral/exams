import { createContext } from 'react';
import User from './app/types/User';

interface AuthContextProps {
  user: User;
  login: (user: User) => void;
  logout: () => void;
}

export const AuthContext = createContext<Partial<AuthContextProps>>({});
