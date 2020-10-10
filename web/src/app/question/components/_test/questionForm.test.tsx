import {
  fireEvent,
  render,
  waitFor,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import Axios from 'axios';
import React from 'react';
import { SUBJECT } from '../../../config/endpoint';
import history from '../../../config/history';
import Question from '../../../types/Question';
import QuestionForm from '../questionForm';

jest.mock('axios');

const axios = Axios as jest.Mocked<typeof Axios>;

describe('<QuestionForm />', () => {
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

  const questionData = {
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
    author: '107859231324466082693',
  };

  beforeEach(() => {
    axios.get.mockResolvedValueOnce({
      data: subjects,
    });
  });

  it('should render a form', () => {
    const { getByRole } = render(<QuestionForm onSubmit={jest.fn()} />);

    expect(getByRole('form')).toBeVisible();
  });

  it('should render alternatives true and false when True Or False type is selected', () => {
    const { getByTestId, getByLabelText } = render(
      <QuestionForm onSubmit={jest.fn()} />
    );

    fireEvent.change(getByLabelText('Type', { selector: 'select' }), {
      target: { value: 'True Or False' },
    });

    expect(getByTestId('question-form-alternative-1')).toHaveTextContent(
      'True'
    );
    expect(getByTestId('question-form-alternative-2')).toHaveTextContent(
      'False'
    );
  });

  it('should render five alternatives when Multiple Choices type is selected', () => {
    const { getByLabelText, getAllByRole } = render(
      <QuestionForm onSubmit={jest.fn()} />
    );

    fireEvent.change(getByLabelText('Type', { selector: 'select' }), {
      target: { value: 'Multiple Choices' },
    });

    expect(getAllByRole('radio')).toHaveLength(5);
  });

  it('should save a question when click on save', async () => {
    const onSubmit = jest.fn();

    const savedQuestion = {
      statement: 'Statement',
      type: 'True Or False',
      solution: 'Solution',
      keywords: 'Key 1; Key 2;',
      subject: {
        id: 1,
      },
      correctAnswer: 'True',
      alternatives: [
        { position: 1, description: 'True' },
        { position: 2, description: 'False' },
      ],
    };

    const { getByTestId, getByLabelText, getByText } = render(
      <QuestionForm onSubmit={onSubmit} />
    );

    await waitFor(() => getByLabelText('Subject'));

    fireEvent.change(getByLabelText('Statement', { selector: 'textarea' }), {
      target: { value: 'Statement' },
    });

    fireEvent.change(getByLabelText('Type', { selector: 'select' }), {
      target: { value: 'True Or False' },
    });

    fireEvent.change(getByLabelText('Subject', { selector: 'select' }), {
      target: { value: 1 },
    });

    fireEvent.change(getByLabelText('Solution', { selector: 'textarea' }), {
      target: { value: 'Solution' },
    });

    fireEvent.change(getByLabelText('Keywords', { selector: 'input' }), {
      target: { value: 'Key 1; Key 2;' },
    });

    fireEvent.click(getByTestId('question-form-alternative-1-radio'));

    fireEvent.click(getByText('Save'));

    expect(onSubmit).toBeCalledWith(savedQuestion);
  });

  it('should update a question when click on save', () => {
    const onSubmit = jest.fn();

    const savedQuestion: Question = {
      statement: 'Statement',
      type: 'Multiple Choices',
      solution: 'Solution',
      keywords: 'Key 1; Key 2;',
      subject: {
        id: 1,
      },
      correctAnswer: 'D',
      alternatives: [
        { position: 1, description: 'A' },
        { position: 2, description: 'B' },
        { position: 3, description: 'C' },
        { position: 4, description: 'D' },
        { position: 5, description: 'E' },
      ],
    };

    const { getByTestId, getByText } = render(
      <QuestionForm onSubmit={onSubmit} questionData={savedQuestion} />
    );

    fireEvent.click(getByTestId('question-form-alternative-2'));

    fireEvent.click(getByText('Save'));

    expect(onSubmit).toBeCalledWith({ ...savedQuestion, correctAnswer: 'B' });
  });

  it('should return to the previous page when click on cancel', () => {
    history.goBack = jest.fn();
    const { getByText } = render(<QuestionForm onSubmit={jest.fn()} />);

    fireEvent.click(getByText('Cancel'));

    expect(history.goBack).toBeCalledTimes(1);
  });

  it('should load the fields if question data is defined', async () => {
    const { getByTestId, getByLabelText } = render(
      <QuestionForm onSubmit={jest.fn()} questionData={questionData} />
    );

    await waitFor(() => getByLabelText('Subject'));

    expect(
      getByLabelText('Statement', { selector: 'textarea' })
    ).toHaveTextContent(questionData.statement);

    expect(getByLabelText('Type', { selector: 'select' })).toHaveProperty(
      'value',
      questionData.type
    );

    expect(getByLabelText('Subject', { selector: 'select' })).toHaveProperty(
      'value',
      questionData.subject.id.toString()
    );

    expect(
      getByLabelText('Solution', { selector: 'textarea' })
    ).toHaveTextContent(questionData.solution);

    expect(
      getByLabelText('Keywords', { selector: 'input' })
    ).toHaveDisplayValue(questionData.keywords);

    expect(getByTestId('question-form-alternative-1-radio')).toHaveProperty(
      'checked'
    );
  });

  it('should not disable the type dropdown when question data is not provided', () => {
    const { getByLabelText } = render(<QuestionForm onSubmit={jest.fn()} />);

    expect(getByLabelText('Type', { selector: 'select' })).toHaveProperty(
      'disabled',
      false
    );
  });

  it('should disable type options when question data is provided', () => {
    const { getByLabelText } = render(
      <QuestionForm onSubmit={jest.fn()} questionData={questionData} />
    );

    expect(getByLabelText('Type', { selector: 'select' })).toHaveProperty(
      'disabled',
      true
    );
  });

  describe('Validation handling', () => {
    it('should not should errors when does not have any', () => {
      const { queryByText } = render(<QuestionForm onSubmit={jest.fn()} />);

      expect(queryByText('required')).toBeNull();
    });

    it('should show statement validation message when have a statement error', () => {
      const { getByText } = render(
        <QuestionForm
          onSubmit={jest.fn()}
          errors={{ statement: 'Statement is required' }}
        />
      );

      expect(getByText('Statement is required')).toBeVisible();
    });

    it('should show type validation message when have a type error', () => {
      const { getByText } = render(
        <QuestionForm
          onSubmit={jest.fn()}
          errors={{ type: 'Type is required' }}
        />
      );

      expect(getByText('Type is required')).toBeVisible();
    });

    it('should show subject validation message when have a subject error', async () => {
      const { getByText, getByLabelText } = render(
        <QuestionForm
          onSubmit={jest.fn()}
          errors={{ subject: 'Subject is required' }}
        />
      );

      await waitFor(() => getByLabelText('Subject'));

      expect(getByText('Subject is required')).toBeVisible();
    });

    it('should show alternatives validation message when have an alternatives errors', () => {
      const { getByLabelText, getByText } = render(
        <QuestionForm
          onSubmit={jest.fn()}
          errors={{ alternatives: 'Alternatives are required' }}
        />
      );

      fireEvent.change(getByLabelText('Type', { selector: 'select' }), {
        target: { value: 'True Or False' },
      });

      expect(getByText('Alternatives are required')).toBeVisible();
    });

    it('should show correct answer validation message when have a correct answer error', () => {
      const { getByLabelText, getByText } = render(
        <QuestionForm
          onSubmit={jest.fn()}
          errors={{ correctAnswer: 'Correct answer is required' }}
        />
      );

      fireEvent.change(getByLabelText('Type', { selector: 'select' }), {
        target: { value: 'True Or False' },
      });

      expect(getByText('Correct answer is required')).toBeVisible();
    });

    it('should show solution validation message when have a solution error', () => {
      const { getByText } = render(
        <QuestionForm
          onSubmit={jest.fn()}
          errors={{
            solution: 'Solution should have a maximum of 3000 characters',
          }}
        />
      );

      expect(
        getByText('Solution should have a maximum of 3000 characters')
      ).toBeVisible();
    });

    it('should show keywords validation message if have an error', () => {
      const { getByText } = render(
        <QuestionForm
          onSubmit={jest.fn()}
          errors={{
            keywords: 'Keywords should have a maximum of 255 characters',
          }}
        />
      );

      expect(
        getByText('Keywords should have a maximum of 255 characters')
      ).toBeVisible();
    });
  });

  describe('Subject', () => {
    it('should select the subject added after create it', async () => {
      axios.post.mockResolvedValueOnce({
        data: {
          id: 3,
          description: 'New Subject',
        },
      });

      const { getByTitle, getByLabelText, getByTestId } = render(
        <QuestionForm onSubmit={jest.fn()} />
      );

      await waitFor(() => getByLabelText('Subject'));

      fireEvent.click(getByTitle('Add new subject'));

      fireEvent.change(getByLabelText('Subject', { selector: 'input' }), {
        target: {
          value: 'New Subject',
        },
      });

      fireEvent.click(getByTestId('subject-save-button'));

      expect(axios.post).toHaveBeenCalledWith(SUBJECT, {
        description: 'New Subject',
      });

      await waitForElementToBeRemoved(getByTestId('subject-save-button'));

      expect(getByLabelText('Subject', { selector: 'select' })).toHaveProperty(
        'value',
        '3'
      );
    });
  });
});
