import { render } from '@testing-library/react';
import React from 'react';

import HeaderBar from '../headerBar';

describe('<HeaderBar />', () => {
  it('should render the header bar', () => {
    const { getByTestId } = render(<HeaderBar />);

    expect(getByTestId('header-bar')).toBeDefined();
  });

  it('should render the application name', () => {
    const { getByTestId } = render(<HeaderBar />);

    expect(getByTestId('header-bar')).toHaveTextContent('Exams');
  });

  it('should render user profile', () => {
    const { getByTestId } = render(<HeaderBar />);

    expect(getByTestId('header-profile')).toBeDefined();
    expect(getByTestId('profile-photo')).toBeDefined();
    expect(getByTestId('profile-name')).toBeDefined();
  });
});
