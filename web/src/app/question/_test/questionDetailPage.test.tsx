import React from 'react';
import {
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import Axios from 'axios';
import router from 'react-router';
import { QUESTION_BY_ID, QUESTION_COMMENT } from '../../config/endpoint';
import history from '../../config/history';
import QuestionDetailPage from '../questionDetailPage';
import { questionCommentsSubscription } from '../../config/socket';

jest.mock('axios');
jest.mock('../../config/history');
jest.spyOn(router, 'useParams').mockReturnValue({ id: '2' });
jest.mock('../../config/socket');

const axiosMocked = Axios as jest.Mocked<typeof Axios>;

describe('<QuestionDetailPage />', () => {
  const question = {
    id: 2,
    statement: 'Question 1?',
    type: 'True Or False',
    solution: 'Some comments about how to resolve the question 1',
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
    axiosMocked.get
      .mockResolvedValueOnce({
        data: question,
      })
      .mockResolvedValueOnce({
        data: [],
      });

    axiosMocked.post.mockResolvedValueOnce({
      data: {
        id: 1,
        message: 'My Comment',
        auhot: '1',
      },
    });
  });

  afterEach(() => {
    axiosMocked.get.mockRestore();
    axiosMocked.post.mockRestore();
    (history.push as any).mockRestore();
    (questionCommentsSubscription as any).mockRestore();
  });

  it('should call the get question by id endpoint', async () => {
    render(<QuestionDetailPage />);

    expect(Axios.get).toHaveBeenCalledWith(
      QUESTION_BY_ID.replace(':id', question.id.toString())
    );
  });

  it('should render a loading while calling endpoint', () => {
    const { getByTestId } = render(<QuestionDetailPage />);
    expect(getByTestId('loading')).toBeDefined();
  });

  it('should render the question detail page', async () => {
    const { getByTestId, getByRole } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByRole('heading')).toHaveTextContent(`Question ${question.id}`);
  });

  it('should render the question details', async () => {
    const { getByText, getByTestId } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    expect(getByText(/Type/)).toBeDefined();
    expect(getByText(/Statement/)).toBeDefined();
    expect(getByText(/Subject/)).toBeDefined();
    expect(getByText(/Solution/)).toBeDefined();
    expect(getByText(/Keywords/)).toBeDefined();

    expect(getByText(/Statement/, { selector: 'span' })).toHaveTextContent(
      question.statement
    );
    expect(getByText(/Type/, { selector: 'span' })).toHaveTextContent(
      question.type
    );
    expect(getByText(/Subject/, { selector: 'span' })).toHaveTextContent(
      question.subject.description
    );
    expect(getByText(/Solution/, { selector: 'span' })).toHaveTextContent(
      question.solution
    );
    expect(getByText(/Keywords/, { selector: 'span' })).toHaveTextContent(
      question.keywords
    );
  });

  it('should return to the previous page after click on the cancel button', async () => {
    history.goBack = jest.fn();
    const { getByTestId, getByRole } = render(<QuestionDetailPage />);

    await waitForElementToBeRemoved(() => getByTestId('loading'));

    fireEvent.click(
      getByRole('button', {
        name: 'Cancel',
      })
    );

    expect(history.goBack).toBeCalledTimes(1);
  });

  describe('Comments Section', () => {
    it('should render a comment section', async () => {
      const { getByTestId } = render(<QuestionDetailPage />);

      await waitForElementToBeRemoved(() => getByTestId('loading'));

      expect(getByTestId('comments-section')).toBeDefined();
    });

    it('should call create comment endpoint when clicked on send button', async () => {
      const { container, getByTestId } = render(<QuestionDetailPage />);

      await waitForElementToBeRemoved(() => getByTestId('loading'));

      expect(container.querySelectorAll('.comment')).toHaveLength(0);

      fireEvent.change(getByTestId('comment-input'), {
        target: { value: 'My Comment' },
      });

      fireEvent.click(getByTestId('send-comment-button'));

      expect(Axios.post).toHaveBeenCalledWith(QUESTION_COMMENT, {
        message: 'My Comment',
        questionId: question.id,
      });
    });

    it('should subsribe on comments channel', async () => {
      const { getByTestId } = render(<QuestionDetailPage />);

      await waitForElementToBeRemoved(() => getByTestId('loading'));

      expect(questionCommentsSubscription).toHaveBeenCalledWith(
        question.id.toString(),
        expect.anything()
      );
    });
  });
});
