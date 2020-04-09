import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

describe('App test', () => {
  it('should render the App component', () => {
    render(<App />);

    expect(screen.getByTestId('app')).toBeDefined();
  });
});
