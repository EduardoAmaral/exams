import { render } from '@testing-library/react';
import React from 'react';

import { AuthContext } from '../../../context';
import HeaderBar from '../headerBar';

describe('<HeaderBar />', () => {
  it('should render the header bar', () => {
    const { getByRole } = render(
      <AuthContext.Provider
        value={{
          user: { name: 'Eduardo Amaral' },
        }}
      >
        <HeaderBar />
      </AuthContext.Provider>
    );

    expect(getByRole('banner')).toBeDefined();
  });

  it('should render the application name', () => {
    const { getByRole } = render(
      <AuthContext.Provider
        value={{
          user: { name: 'Eduardo Amaral' },
        }}
      >
        <HeaderBar />
      </AuthContext.Provider>
    );

    expect(getByRole('heading')).toHaveTextContent('Exams');
  });

  it('should render user profile picture', () => {
    const { getByRole } = render(
      <AuthContext.Provider
        value={{
          user: { name: 'Eduardo Amaral' },
        }}
      >
        <HeaderBar />
      </AuthContext.Provider>
    );

    expect(getByRole('img')).toBeDefined();
  });
});
