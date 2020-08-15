import {
  fireEvent,
  render,
  waitFor,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import Axios from 'axios';
import React from 'react';
import router from 'react-router';
import { QUESTION, QUESTION_BY_ID, SUBJECT } from '../../config/endpoint';
import QuestionEditPage from '../questionEditPage';

jest.mock('axios');
jest.mock('../../config/history');
jest.spyOn(router, 'useParams').mockReturnValue({ id: '2' });

const axiosMocked = Axios as jest.Mocked<typeof Axios>;

describe('<QuestionEditPage />', () => {
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
    author: '1',
  };

  beforeEach(() => {
    axiosMocked.get
      .mockResolvedValueOnce({ data: subjects })
      .mockResolvedValueOnce({ data: question });

    axiosMocked.put.mockResolvedValueOnce({
      status: 200,
    });
  });

  afterEach(() => axiosMocked.get.mockRestore());

  it('should render the question edit page', async () => {
    const { getByTestId, getByLabelText } = render(<QuestionEditPage />);

    await waitFor(() => getByLabelText('Subject'));

    expect(getByTestId('question-edit-page')).toBeDefined();
  });

  it('should call the questionById and subject endpoint', async () => {
    render(<QuestionEditPage />);

    expect(Axios.get).toHaveBeenCalledTimes(2);
    expect(Axios.get).toHaveBeenNthCalledWith(1, SUBJECT);
    expect(Axios.get).toHaveBeenNthCalledWith(
      2,
      QUESTION_BY_ID.replace(':id', '2')
    );
  });

  it('should render a loading while calling an endpoint', () => {
    const { getByTestId } = render(<QuestionEditPage />);
    expect(getByTestId('loading')).toBeDefined();
  });

  it('should render a form', async () => {
    const { getByLabelText, getByRole, getByTestId } = render(
      <QuestionEditPage />
    );

    await waitForElementToBeRemoved(getByTestId('loading'));

    await waitFor(() => getByLabelText('Subject'));

    expect(getByRole('form')).toBeDefined();
  });

  it('should call the question update endpoint when save a form', async () => {
    const { getByText, getByLabelText, getByTestId } = render(
      <QuestionEditPage />
    );

    await waitForElementToBeRemoved(getByTestId('loading'));

    await waitFor(() => getByLabelText('Subject'));

    fireEvent.click(getByText('Save'));

    expect(Axios.put).toHaveBeenCalledTimes(1);
    expect(Axios.put).toHaveBeenCalledWith(QUESTION, expect.any(Object));
  });
});
