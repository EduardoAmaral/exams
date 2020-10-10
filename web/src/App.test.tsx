import { render, waitFor } from '@testing-library/react';
import Axios from 'axios';
import React from 'react';
import App from './App';

jest.mock('axios');
jest.mock('./app/config/history');

const axios = Axios as jest.Mocked<typeof Axios>;

afterEach(() => {
  axios.get.mockRestore();
});

describe('<App />', () => {
  it('should call Login when not authenticated', async () => {
    axios.get.mockRejectedValueOnce({ status: 401 });

    window.location.replace = jest.fn();

    render(<App />);

    setTimeout(function () {
      expect(window.location.replace).toHaveBeenCalledTimes(1);
    }, 1000);
  });

  it('should render App when Authenticated', async () => {
    axios.get.mockResolvedValueOnce({
      data: { name: 'Someone' },
    });
    axios.get.mockResolvedValueOnce({ data: [] });

    const { getByTestId } = render(<App />);

    await waitFor(() => getByTestId('app'));

    expect(getByTestId('app')).toBeDefined();
  });
});
