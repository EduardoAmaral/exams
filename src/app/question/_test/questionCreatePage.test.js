import React from 'react';
import { render, waitForElementToBeRemoved } from '@testing-library/react';
import axios from 'axios';
import { GET_SUBJECT } from '../../config/endpoint';
import QuestionCreatePage from '../questionCreatePage';

jest.mock('axios');
jest.mock('../../config/history');

describe('Question Create Page', () => {
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

  afterEach(() => axios.get.mockRestore());

  it('should render the question creation page', async () => {
    const { getByText, getByTestId } = render(<QuestionCreatePage />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    expect(getByTestId('question-create-page')).toBeDefined();
  });

  it('should call the subject get endpoint', async () => {
    render(<QuestionCreatePage />);

    expect(axios.get).toHaveBeenCalledTimes(1);
    expect(axios.get).toHaveBeenCalledWith(GET_SUBJECT);
  });

  it('should render a loading while calling an endpoint', () => {
    const { getByText } = render(<QuestionCreatePage />);
    expect(getByText('Loading')).toBeDefined();
  });

  it('should render a form', async () => {
    const { getByTestId, getByText } = render(<QuestionCreatePage />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    expect(getByTestId('question-form')).toBeDefined();
  });
});
