import React from 'react';
import { render } from '@testing-library/react';
import axios from 'axios';
import App from './App';
import { AUTH } from './app/config/endpoint';

jest.mock('axios');

describe('App test', () => {
  beforeEach(() => {
    axios.get
      .mockResolvedValueOnce({
        data: true,
      })
      .mockResolvedValueOnce({ data: [] });
  });

  afterEach(() => {
    axios.get.mockRestore();
  });

  it('should render the App component', () => {
    const { getByTestId } = render(<App />);

    expect(getByTestId('app')).toBeDefined();
  });

  it('should call auth endpoint onload', () => {
    render(<App />);

    expect(axios.get).toBeCalledTimes(1);
    expect(axios.get).toBeCalledWith(AUTH);
  });

  it('should render header bar', () => {
    const { getByTestId } = render(<App />);

    expect(getByTestId('header-bar')).toBeDefined();
  });
});
