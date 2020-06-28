import { render } from '@testing-library/react';
import React from 'react';
import Loading from '../loading';

describe('Loading', () => {
  it('should render a loading while calling endpoint', () => {
    const { getByText } = render(<Loading />);
    expect(getByText('Loading')).toBeDefined();
  });
});
