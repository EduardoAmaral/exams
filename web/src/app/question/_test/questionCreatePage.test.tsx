import { fireEvent, render, waitFor } from '@testing-library/react';
import Axios from 'axios';
import React from 'react';
import { QUESTION, SUBJECT } from '../../config/endpoint';
import QuestionCreatePage from '../questionCreatePage';

jest.mock('axios');
jest.mock('../../config/history');

const axios = Axios as jest.Mocked<typeof Axios>;

describe('<QuestionCreatePage />', () => {
  const subjects = [
    {
      id: 1,
      description: 'English',
    },
    {
      id: 2,
      description: 'Spanish',
    },
  ];

  beforeEach(() => {
    axios.get.mockResolvedValueOnce({
      data: subjects,
    });

    axios.post.mockResolvedValueOnce({
      status: 201,
    });
  });

  afterEach(() => axios.get.mockRestore());

  it('should render the question creation page', async () => {
    const { getByTestId, getByLabelText } = render(<QuestionCreatePage />);

    await waitFor(() => getByLabelText('Subject'));

    expect(getByTestId('question-create-page')).toBeDefined();
  });

  it('should call the subject get endpoint', async () => {
    render(<QuestionCreatePage />);

    expect(Axios.get).toHaveBeenCalledTimes(1);
    expect(Axios.get).toHaveBeenCalledWith(SUBJECT);
  });

  it('should render a form', async () => {
    const { getByLabelText, getByRole } = render(<QuestionCreatePage />);

    await waitFor(() => getByLabelText('Subject'));

    expect(getByRole('form')).toBeDefined();
  });

  it('should call the question save endpoint when save a form', async () => {
    const { getByLabelText, getByText } = render(<QuestionCreatePage />);

    await waitFor(() => getByLabelText('Subject'));

    fireEvent.click(getByText('Save'));

    expect(Axios.post).toHaveBeenCalledTimes(1);
    expect(Axios.post).toHaveBeenCalledWith(QUESTION, expect.any(Object));
  });
});
