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
  });

  afterEach(() => {
    axios.get.mockRestore();
    axios.post.mockRestore();
  });

  it('should render the question creation page', async () => {
    const { getByRole, getByLabelText } = render(<QuestionCreatePage />);

    await waitFor(() => getByLabelText('Subject'));

    expect(getByRole('heading')).toHaveTextContent('Create Question');
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
    axios.post.mockResolvedValueOnce({
      status: 201,
    });

    const { getByLabelText, getByRole } = render(<QuestionCreatePage />);

    await waitFor(() => getByLabelText('Subject'));

    fireEvent.click(
      getByRole('button', {
        name: 'Save',
      })
    );

    expect(Axios.post).toHaveBeenCalledTimes(1);
    expect(Axios.post).toHaveBeenCalledWith(QUESTION, expect.any(Object));
  });

  it('should show errors on fields when save with invalid fields', async () => {
    axios.post.mockRejectedValueOnce({
      response: {
        status: 400,
        data: {
          errors: {
            type: 'Type is required',
            statement: 'Statement is required',
          },
        },
      },
    });

    const { getByLabelText, getByText, getByRole } = render(
      <QuestionCreatePage />
    );

    await waitFor(() => getByLabelText('Subject'));

    fireEvent.click(
      getByRole('button', {
        name: 'Save',
      })
    );

    await waitFor(() => getByText('Type is required'));

    expect(Axios.post).toHaveBeenCalledTimes(1);
    expect(getByText('Type is required')).toBeDefined();
    expect(getByText('Statement is required')).toBeDefined();
  });
});
