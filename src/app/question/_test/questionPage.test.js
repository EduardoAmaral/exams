import React from 'react';
import {
  render,
  fireEvent,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import axios from 'axios';
import QuestionPage from '../questionPage';
import { QUESTION } from '../../config/endpoint';
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
  });

  afterEach(() => axios.get.mockRestore());

  it('should call the questions get endpoint', async () => {
    render(<QuestionPage />);

    expect(axios.get).toHaveBeenCalledTimes(1);
    expect(axios.get).toHaveBeenCalledWith(QUESTION);
  });

  it('should render the question page', async () => {
    const { getByText, getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    expect(getByTestId('question-page')).toBeDefined();
  });

  it('should render the table question header', async () => {
    const { getByText, getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    expect(getByTestId('question-header-statement')).toHaveTextContent(
      'Statement'
    );
    expect(getByTestId('question-header-subject')).toHaveTextContent('Subject');
    expect(getByTestId('question-header-type')).toHaveTextContent('Type');
  });

  it('should render questions when endpoint return them', async () => {
    const { container, getByText } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    expect(
      container.querySelectorAll('[data-testid^="question-statement-"]')
    ).toHaveLength(2);
  });

  it('should render a loading while calling endpoint', () => {
    const { getByText } = render(<QuestionPage />);
    expect(getByText('Loading')).toBeDefined();
  });

  it('should render a create question button', async () => {
    const { getByText, getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    const button = getByTestId('question-create-button');
    expect(button).toBeDefined();
    expect(button).toHaveTextContent('Create Question');
  });

  it('should redirect to create question page', async () => {
    const { getByText } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    fireEvent.click(getByText('Create Question'));

    expect(history.push).toBeCalledTimes(1);
    expect(history.push).toBeCalledWith('/question/create');
  });
});
