import { render } from '@testing-library/react';
import React from 'react';

import Profile from '../profile';

describe('<Profile />', () => {
  it('should render user profile', () => {
    const { getByTestId } = render(<Profile />);

    expect(getByTestId('header-profile')).toBeDefined();
    expect(getByTestId('profile-photo')).toBeDefined();
    expect(getByTestId('profile-name')).toBeDefined();
  });
});
