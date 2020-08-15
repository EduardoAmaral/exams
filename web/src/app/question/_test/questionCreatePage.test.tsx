import React from 'react';
import {
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import axios from 'axios';
import { QUESTION, SUBJECT } from '../../config/endpoint';
import QuestionCreatePage from '../questionCreatePage';

jest.mock('axios');
jest.mock('../../config/history');

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
    (axios.get as any).mockResolvedValueOnce({
      data: subjects,
    });

    (axios.post as any).mockResolvedValueOnce({
      status: 201,
    });
  });

  afterEach(() => (axios.get as any).mockRestore());

  it('should render the question creation page', async () => {
    const { getByTestId } = render(<QuestionCreatePage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-create-page')).toBeDefined();
  });

  it('should call the subject get endpoint', async () => {
    render(<QuestionCreatePage />);

    expect(axios.get).toHaveBeenCalledTimes(1);
    expect(axios.get).toHaveBeenCalledWith(SUBJECT);
  });

  it('should render a loading while calling an endpoint', () => {
    const { getByTestId } = render(<QuestionCreatePage />);
    expect(getByTestId('loading')).toBeDefined();
  });

  it('should render a form', async () => {
    const { getByTestId, getByRole } = render(<QuestionCreatePage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByRole('form')).toBeDefined();
  });

  it('should call the question save endpoint when save a form', async () => {
    const { getByTestId, getByText } = render(<QuestionCreatePage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByText('Save'));

    expect(axios.post).toHaveBeenCalledTimes(1);
    expect(axios.post).toHaveBeenCalledWith(QUESTION, expect.any(Object));
  });
});
