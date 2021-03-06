import {
  fireEvent,
  render,
  waitFor,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import Axios from 'axios';
import React from 'react';
import { MemoryRouter, Route } from 'react-router';
import { QUESTION, QUESTION_BY_ID, SUBJECT } from '../../config/endpoint';
import QuestionEditPage from '../questionEditPage';

jest.mock('axios');
jest.mock('../../config/history');

const axios = Axios as jest.Mocked<typeof Axios>;

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
    keywords: 'Key',
    subject: { id: 1, description: 'English' },
    author: '1',
  };

  beforeEach(() => {
    axios.get
      .mockResolvedValueOnce({ data: subjects })
      .mockResolvedValueOnce({ data: question });
  });

  afterEach(() => axios.get.mockRestore());

  it('should render the question edit page', async () => {
    const { getByRole, getByLabelText } = renderPage();

    await waitFor(() => getByLabelText('Subject'));

    expect(getByRole('heading')).toHaveTextContent('Edit Question');
  });

  it('should call the questionById and subject endpoint', async () => {
    renderPage();

    expect(Axios.get).toHaveBeenCalledTimes(2);
    expect(Axios.get).toHaveBeenNthCalledWith(1, SUBJECT);
    expect(Axios.get).toHaveBeenNthCalledWith(
      2,
      QUESTION_BY_ID.replace(':id', '2')
    );
  });

  it('should render a loading while calling an endpoint', () => {
    const { getByTestId } = renderPage();
    expect(getByTestId('loading')).toBeDefined();
  });

  it('should render a form', async () => {
    const { getByLabelText, getByRole, getByTestId } = renderPage();

    await waitForElementToBeRemoved(getByTestId('loading'));

    await waitFor(() => getByLabelText('Subject'));

    expect(getByRole('form')).toBeDefined();
  });

  it('should call the question update endpoint when save a form', async () => {
    axios.put.mockResolvedValueOnce({
      status: 200,
    });

    const { getByText, getByLabelText, getByTestId } = renderPage();

    await waitForElementToBeRemoved(getByTestId('loading'));

    await waitFor(() => getByLabelText('Subject'));

    fireEvent.click(getByText('Save'));

    expect(Axios.put).toHaveBeenCalledTimes(1);
    expect(Axios.put).toHaveBeenCalledWith(QUESTION, expect.any(Object));
  });

  it('should show errors on fields when save with invalid fields', async () => {
    axios.put.mockRejectedValueOnce({
      response: {
        status: 400,
        data: {
          errors: {
            statement: 'Statement is required',
          },
        },
      },
    });

    const { getByLabelText, getByText, getByRole } = renderPage();

    await waitFor(() => getByLabelText('Subject'));

    fireEvent.change(getByLabelText('Statement', { selector: 'textarea' }), {
      target: { value: '' },
    });

    fireEvent.click(
      getByRole('button', {
        name: 'Save',
      })
    );

    await waitFor(() => getByText('Statement is required'));

    expect(Axios.put).toHaveBeenCalledTimes(1);
    expect(getByText('Statement is required')).toBeDefined();
  });

  const renderPage = () => {
    return render(
      <MemoryRouter initialEntries={['/question/edit/2']}>
        <Route path="/question/edit/:id">
          <QuestionEditPage />
        </Route>
      </MemoryRouter>
    );
  };
});
