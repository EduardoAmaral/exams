import { render, getByTestId } from '@testing-library/react';
import React from 'react';
import Loading from '../loading';

describe('Loading', () => {
  it('should render a loading while calling endpoint', () => {
    const { getByTestId } = render(<Loading isLoading={true} />);
    expect(getByTestId('loading')).toBeDefined();
  });
});
