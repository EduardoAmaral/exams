import React from 'react';
import {
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import ExamPage from '../examPage';
import Axios from 'axios';
import { formatDateTime } from '../../util/dateUtil';
import history from '../../config/history';

jest.mock('axios');
jest.mock('../../config/history');

const axios = Axios as jest.Mocked<typeof Axios>;

describe('<ExamPage />', () => {
  const exams = [
    {
      title: 'Title 1',
      startDateTime: '2020-09-27T09:30:00-05:00',
      endDateTime: '2020-09-27T12:30:00-05:00',
    },
    {
      title: 'Title 2',
      startDateTime: '2020-09-14T13:45:00-05:00',
      endDateTime: '2020-09-14T15:30:00-05:00',
    },
  ];

  beforeEach(() => {
    axios.get.mockResolvedValueOnce({
      data: exams,
    });
  });

  it('should display a grid for my exams', () => {
    const { getByRole, getByText } = render(<ExamPage />);

    expect(getByRole('table')).toBeDefined();

    expect(getByText('Title')).toBeDefined();
    expect(getByText('Starts at')).toBeDefined();
    expect(getByText('Ends at')).toBeDefined();
  });

  it('should display user exams', async () => {
    const { getByText, getByTestId } = render(<ExamPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByText('Title 1')).toBeDefined();
    expect(
      getByText(formatDateTime('2020-09-27T09:30:00-05:00'))
    ).toBeDefined();
    expect(
      getByText(formatDateTime('2020-09-27T12:30:00-05:00'))
    ).toBeDefined();

    expect(getByText('Title 2')).toBeDefined();
    expect(
      getByText(formatDateTime('2020-09-14T13:45:00-05:00'))
    ).toBeDefined();
    expect(
      getByText(formatDateTime('2020-09-14T15:30:00-05:00'))
    ).toBeDefined();
  });

  it('should redirect to create exam page when click on create button', async () => {
    const { getByRole, getByTestId } = render(<ExamPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(
      getByRole('button', {
        name: 'Create Exam',
      })
    );

    expect(history.push).toBeCalledTimes(1);
    expect(history.push).toBeCalledWith('/exam/create');
  });
});
