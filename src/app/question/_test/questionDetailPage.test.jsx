import React from 'react';
import {
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import axios from 'axios';
import router from 'react-router';
import { QUESTION_BY_ID } from '../../config/endpoint';
import history from '../../config/history';
import QuestionDetailPage from '../questionDetailPage';

jest.mock('axios');
jest.mock('../../config/history');
jest.spyOn(router, 'useParams').mockReturnValue({ id: 2 });

describe('Question Detail Page', () => {
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
    author: '1',
  };

  beforeEach(() => {
    axios.get.mockResolvedValueOnce({
      data: question,
    });
  });

  afterEach(() => {
    axios.get.mockRestore();
    history.push.mockRestore();
  });

  it('should call the get question by id endpoint', async () => {
    render(<QuestionDetailPage />);

    expect(axios.get).toHaveBeenCalledTimes(1);
    expect(axios.get).toHaveBeenCalledWith(
      QUESTION_BY_ID.replace(':id', question.id)
    );
  });

  it('should render a loading while calling endpoint', () => {
    const { getByTestId } = render(<QuestionDetailPage />);
    expect(getByTestId('loading')).toBeDefined();
  });

  it('should render the question detail page', async () => {
    const { getByTestId } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-detail-page')).toBeDefined();
  });

  it('should render the question statement', async () => {
    const { getByTestId } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-detail-statement-title')).toHaveTextContent(
      'Statement:'
    );
    expect(getByTestId('question-detail-statement-value')).toHaveTextContent(
      question.statement
    );
  });

  it('should render the question type', async () => {
    const { getByTestId } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-detail-type-title')).toHaveTextContent(
      'Type:'
    );
    expect(getByTestId('question-detail-type-value')).toHaveTextContent(
      question.type
    );
  });

  it('should render the question subject', async () => {
    const { getByTestId } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-detail-subject-title')).toHaveTextContent(
      'Subject:'
    );
    expect(getByTestId('question-detail-subject-value')).toHaveTextContent(
      question.subject.description
    );
  });

  it('should render the question solution', async () => {
    const { getByTestId } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-detail-solution-title')).toHaveTextContent(
      'Solution:'
    );
    expect(getByTestId('question-detail-solution-value')).toHaveTextContent(
      question.solution
    );
  });

  it('should render the question topic', async () => {
    const { getByTestId } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-detail-topic-title')).toHaveTextContent(
      'Topics:'
    );
    expect(getByTestId('question-detail-topic-value')).toHaveTextContent(
      question.topic
    );
  });

  it('should return to the previous after click on the cancel button', async () => {
    history.goBack = jest.fn();
    const { getByTestId } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('cancel-button'));

    expect(history.goBack).toBeCalledTimes(1);
  });
});
