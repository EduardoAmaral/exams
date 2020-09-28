import React from 'react';
import {
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import CreateExamPage from '../createExamPage';
import { EXAM } from '../../config/endpoint';
import Axios from 'axios';
import history from '../../config/history';

jest.mock('axios');
jest.mock('../../config/history');

const axios = Axios as jest.Mocked<typeof Axios>;

describe('<CreateExamPage />', () => {
  const questions = [
    {
      id: 1,
      statement: 'Question 1',
      type: 'True Or False',
      subject: {
        description: 'English',
      },
      authorName: 'Unknown',
    },
  ];

  beforeEach(() => {
    axios.get.mockResolvedValueOnce({
      data: questions,
    });
  });

  afterEach(() => {
    (history as any).goBack.mockRestore();
  });

  it('should display a form to create an exam', () => {
    const { getByRole, getByLabelText } = render(<CreateExamPage />);

    expect(getByRole('heading')).toHaveTextContent('Create Exam');
    expect(
      getByRole('textbox', {
        name: 'Title',
      })
    ).toBeDefined();
    expect(getByLabelText('Starts at', { selector: 'input' })).toBeDefined();
    expect(getByLabelText('Ends at', { selector: 'input' })).toBeDefined();
  });

  it('should display a table with the questions available to create an exam', async () => {
    const { getByText, getByTestId, getAllByRole } = render(<CreateExamPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByText('Statement')).toBeDefined();
    expect(getByText('Question 1')).toBeDefined();

    expect(getByText('Subject')).toBeDefined();
    expect(getByText('English')).toBeDefined();

    expect(getByText('Type')).toBeDefined();
    expect(getByText('True Or False')).toBeDefined();

    expect(getByText('Author')).toBeDefined();
    expect(getByText('Unknown')).toBeDefined();

    expect(getAllByRole('checkbox')).toHaveLength(1);
  });

  it('should save an exam when click on save button with the required fields filled', async () => {
    axios.post.mockResolvedValueOnce({
      response: {
        status: 201,
      },
    });
    const { getByLabelText, getByRole, getByTestId } = render(
      <CreateExamPage />
    );

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.change(getByLabelText('Title', { selector: 'input' }), {
      target: {
        value: 'Title should be this',
      },
    });

    fireEvent.change(getByLabelText('Starts at', { selector: 'input' }), {
      target: {
        value: '2020-09-27T18:02',
      },
    });

    fireEvent.change(getByLabelText('Ends at', { selector: 'input' }), {
      target: {
        value: '2020-09-27T22:02',
      },
    });

    fireEvent.click(getByTestId(`question-selected-${questions[0].id}-input`));

    fireEvent.click(
      getByRole('button', {
        name: 'Save',
      })
    );

    expect(axios.post).toHaveBeenCalledWith(EXAM, {
      title: 'Title should be this',
      startDateTime: new Date('2020-09-27T18:02').toISOString(),
      endDateTime: new Date('2020-09-27T22:02').toISOString(),
      questions: questions.map((q) => ({ ...q, selected: true })),
    });

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(history.goBack).toHaveBeenCalledTimes(1);
  });

  it('should go back when click on cancel button', () => {
    const { getByRole } = render(<CreateExamPage />);

    fireEvent.click(
      getByRole('button', {
        name: 'Cancel',
      })
    );

    expect(history.goBack).toHaveBeenCalledTimes(1);
  });
});
