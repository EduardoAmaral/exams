import { render } from '@testing-library/react';
import React from 'react';
import { createStore } from 'redux';
import { Provider } from 'react-redux';

import Profile from '../profile';

describe('<Profile />', () => {
  it('should render user profile', () => {
    const store = createStore(() => ({
      user: { id: '1', name: 'Name', profileSrc: 'source' },
    }));

    const { getByTestId } = render(
      <Provider store={store}>
        <Profile />
      </Provider>
    );

    expect(getByTestId('header-profile')).toBeDefined();
    expect(getByTestId('profile-photo')).toBeDefined();
    expect(getByTestId('profile-name')).toHaveTextContent('Name');
  });
});
