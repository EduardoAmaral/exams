import { render } from '@testing-library/react';
import React from 'react';

import { Provider } from 'react-redux';
import HeaderBar from '../headerBar';
import store from '../../store/store';

describe('<HeaderBar />', () => {
  it('should render the header bar', () => {
    const { getByRole } = render(
      <Provider store={store}>
        <HeaderBar />
      </Provider>
    );

    expect(getByRole('banner')).toBeDefined();
  });

  it('should render the application name', () => {
    const { getByRole } = render(
      <Provider store={store}>
        <HeaderBar />
      </Provider>
    );

    expect(getByRole('heading')).toHaveTextContent('Exams');
  });

  it('should render user profile picture', () => {
    const { getByRole } = render(
      <Provider store={store}>
        <HeaderBar />
      </Provider>
    );

    expect(getByRole('img')).toBeDefined();
  });
});
