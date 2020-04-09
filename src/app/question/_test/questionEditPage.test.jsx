import React from 'react';
import {
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import axios from 'axios';
import router from 'react-router';
import { QUESTION, QUESTION_BY_ID, SUBJECT } from '../../config/endpoint';
import QuestionEditPage from '../questionEditPage';

jest.mock('axios');
jest.mock('../../config/history');
jest.spyOn(router, 'useParams').mockReturnValue({ id: 2 });

describe('Question Edit Page', () => {
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

  const question = {
    id: 2,
    statement: 'Question 1?',
    type: 'True Or False',
    solution: 'Solution 1',
    shared: false,
    alternatives: [
      { id: 1, description: 'True' },
      { id: 2, description: 'False' },
    ],
    correctAnswer: 'True',
    topic: 'Topic',
    subject: { id: 1, description: 'English' },
    author: '107859231324466082693',
  };

  beforeEach(() => {
    axios.get = jest
      .fn()
      .mockResolvedValueOnce({ data: question })
      .mockResolvedValueOnce({ data: subjects });

    axios.put.mockResolvedValueOnce({
      status: 200,
    });
  });

  afterEach(() => axios.get.mockRestore());

  it('should render the question creation page', async () => {
    const { getByTestId } = render(<QuestionEditPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-create-page')).toBeDefined();
  });

  it('should call the questionById and subject endpoint', async () => {
    render(<QuestionEditPage />);

    expect(axios.get).toHaveBeenCalledTimes(2);
    expect(axios.get).toHaveBeenNthCalledWith(
      1,
      QUESTION_BY_ID.replace(':id', '2')
    );
    expect(axios.get).toHaveBeenNthCalledWith(2, SUBJECT);
  });

  it('should render a loading while calling an endpoint', () => {
    const { getByTestId } = render(<QuestionEditPage />);
    expect(getByTestId('loading')).toBeDefined();
  });

  it('should render a form', async () => {
    const { getByTestId } = render(<QuestionEditPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-form')).toBeDefined();
  });

  it('should call the question update endpoint when save a form', async () => {
    const { getByTestId } = render(<QuestionEditPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('question-form-save-button'));

    expect(axios.put).toHaveBeenCalledTimes(1);
    expect(axios.put).toHaveBeenCalledWith(QUESTION, expect.any(Object));
  });
});
