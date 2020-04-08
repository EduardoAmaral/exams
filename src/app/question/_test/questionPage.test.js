import React from 'react';
import {
  render,
  fireEvent,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import axios from 'axios';
import QuestionPage from '../questionPage';
import { DELETE_QUESTION, QUESTION } from '../../config/endpoint';
import history from '../../config/history';

jest.mock('axios');
jest.mock('../../config/history');

describe('Question Page', () => {
  const questions = [
    {
      id: 1,
      statement: 'Question?',
      type: 'True Or False',
      subject: {
        id: 1,
        description: 'English',
      },
    },
    {
      id: 2,
      statement: 'Question 2?',
      type: 'Multiple Choice',
      subject: {
        id: 1,
        description: 'English',
      },
    },
  ];

  beforeEach(() => {
    axios.get.mockResolvedValueOnce({
      data: questions,
    });

    axios.delete.mockResolvedValueOnce({
      status: 204,
    });
  });

  afterEach(() => axios.get.mockRestore());

  it('should call the questions get endpoint', async () => {
    render(<QuestionPage />);

    expect(axios.get).toHaveBeenCalledTimes(1);
    expect(axios.get).toHaveBeenCalledWith(QUESTION);
  });

  it('should render the question page', async () => {
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-page')).toBeDefined();
  });

  it('should render the table question header', async () => {
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-header-statement')).toHaveTextContent(
      'Statement'
    );
    expect(getByTestId('question-header-subject')).toHaveTextContent('Subject');
    expect(getByTestId('question-header-type')).toHaveTextContent('Type');
    expect(getByTestId('question-header-actions')).toHaveTextContent('Actions');
  });

  it('should render questions when endpoint return them', async () => {
    const { container, getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(
      container.querySelectorAll('[data-testid^="question-statement-"]')
    ).toHaveLength(2);
  });

  it('should render a loading while calling endpoint', () => {
    const { getByTestId } = render(<QuestionPage />);
    expect(getByTestId('loading')).toBeDefined();
  });

  it('should render a create question button', async () => {
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    const button = getByTestId('question-create-button');
    expect(button).toBeDefined();
    expect(button).toHaveTextContent('Create Question');
  });

  it('should redirect to create question page', async () => {
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('question-create-button'));

    expect(history.push).toBeCalledTimes(1);
    expect(history.push).toBeCalledWith('/question/create');
  });

  it('should call delete question endpoint when click on delete button', async () => {
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('question-delete-button-1'));

    expect(axios.delete).toBeCalledTimes(1);
    expect(axios.delete).toBeCalledWith(DELETE_QUESTION.replace(':id', 1));
  });

  it('should not show the question after delete it', async () => {
    const { getByTestId, queryByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-1')).toBeDefined();

    fireEvent.click(getByTestId('question-delete-button-1'));

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(queryByTestId('question-1')).toBeNull();
  });
});
