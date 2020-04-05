import React from 'react';
import {
  render,
  screen,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import axios from 'axios';
import QuestionContainer from '../questionContainer';
import { GET_QUESTION } from '../../config/endpoint';

jest.mock('axios');

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

  it('should call the questions get endpoint', async () => {
    render(<QuestionContainer />);

    expect(axios.get).toHaveBeenCalledTimes(1);
    expect(axios.get).toHaveBeenCalledWith(GET_QUESTION);
  });

  it('should render a table', async () => {
    const { getByText } = render(<QuestionContainer />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    expect(screen.getByTestId('question-table')).toBeDefined();
  });

  it('should render the table question header', async () => {
    const { getByText } = render(<QuestionContainer />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    expect(screen.getByTestId('question-header-statement')).toHaveTextContent(
      'Statement'
    );
    expect(screen.getByTestId('question-header-subject')).toHaveTextContent(
      'Subject'
    );
    expect(screen.getByTestId('question-header-type')).toHaveTextContent(
      'Type'
    );
  });

  it('should render questions when endpoint return them', async () => {
    const { container, getByText } = render(<QuestionContainer />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    expect(
      container.querySelectorAll('[data-testid^="question-statement-"]')
    ).toHaveLength(2);
  });

  it('should render a loading while calling endpoint', () => {
    const { getByText } = render(<QuestionContainer />);
    expect(getByText('Loading')).toBeDefined();
  });

  it('should render a creat button', async () => {
    const { getByText } = render(<QuestionContainer />);

    await waitForElementToBeRemoved(() => getByText('Loading'));

    const button = screen.getByTestId('question-create-button');
    expect(button).toBeDefined();
    expect(button).toHaveTextContent('Create Question');
  });
});
