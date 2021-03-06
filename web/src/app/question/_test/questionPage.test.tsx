import React from 'react';
import {
  render,
  fireEvent,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import Axios from 'axios';
import QuestionPage from '../questionPage';
import { DELETE_QUESTION, QUESTION } from '../../config/endpoint';
import history from '../../config/history';

jest.mock('axios');
jest.mock('../../config/history');

const axios = Axios as jest.Mocked<typeof Axios>;

describe('<QuestionPage />', () => {
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

  afterEach(() => {
    axios.get.mockRestore();
    (history.push as any).mockRestore();
  });

  it('should call the questions get endpoint', async () => {
    render(<QuestionPage />);

    expect(Axios.get).toHaveBeenCalledTimes(1);
    expect(Axios.get).toHaveBeenCalledWith(QUESTION);
  });

  it('should render question page title', async () => {
    const { getByTestId, getByText } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByText('My questions')).toBeDefined();
  });

  it('should render user questions on a table', async () => {
    const { getByTestId, getByText, getAllByText } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByText('Statement')).toBeDefined();
    expect(getByText('Subject')).toBeDefined();
    expect(getByText('Type')).toBeDefined();

    expect(getByText('Question?')).toBeDefined();
    expect(getByText('True Or False')).toBeDefined();

    expect(getByText('Question 2?')).toBeDefined();
    expect(getByText('Multiple Choice')).toBeDefined();

    expect(getAllByText('English')).toHaveLength(2);
  });

  it('should render a loading while calling an endpoint', () => {
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

  it('should redirect to create question page when click on create question button', async () => {
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('question-create-button'));

    expect(history.push).toBeCalledTimes(1);
    expect(history.push).toBeCalledWith('/question/create');
  });

  it('should display a confirm dialog when click on the delete button', async () => {
    window.confirm = jest.fn();

    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('question-delete-button-1'));

    expect(window.confirm).toBeCalledTimes(1);
    expect(window.confirm).toBeCalledWith(
      'Are you sure you want to delete the question 1?'
    );
  });

  it('should call delete question endpoint when click on the delete button', async () => {
    window.confirm = jest.fn().mockImplementation(() => true);
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('question-delete-button-1'));

    expect(Axios.delete).toBeCalledTimes(1);
    expect(Axios.delete).toBeCalledWith(DELETE_QUESTION.replace(':id', '1'));
  });

  it('should not show the question after delete it', async () => {
    window.confirm = jest.fn().mockImplementation(() => true);
    const { getByTestId, queryByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByTestId('question-1')).toBeDefined();

    fireEvent.click(getByTestId('question-delete-button-1'));

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(queryByTestId('question-1')).toBeNull();
  });

  it('should redirect to edit question page when click on the edit button', async () => {
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('question-edit-button-1'));

    expect(history.push).toBeCalledTimes(1);
    expect(history.push).toBeCalledWith('/question/edit/1');
  });

  it('should redirect to detail question page when click on the detail button', async () => {
    const { getByTestId } = render(<QuestionPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(getByTestId('question-detail-button-1'));

    expect(history.push).toBeCalledTimes(1);
    expect(history.push).toBeCalledWith('/question/detail/1');
  });
});
