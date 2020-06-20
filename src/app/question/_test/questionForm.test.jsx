import React from 'react';
import { fireEvent, render } from '@testing-library/react';
import QuestionForm from '../questionForm';
import history from '../../config/history';

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
    topic: 'Topic',
    subject: { id: 1, description: 'English' },
    author: '107859231324466082693',
  };

  it('should render a form', () => {
    const { getByTestId } = render(<QuestionForm />);

    expect(getByTestId('question-form')).toBeDefined();
  });

  it('should render a text input and label to question statement', () => {
    const { getByTestId } = render(<QuestionForm />);

    expect(getByTestId('question-form-statement-label')).toHaveTextContent(
      'Statement'
    );
    expect(getByTestId('question-form-statement-input')).toBeDefined();
  });

  it('should render a text input and label to question type', () => {
    const { getByTestId } = render(<QuestionForm />);

    expect(getByTestId('question-form-type-label')).toHaveTextContent('Type');
    expect(getByTestId('question-form-type-input')).toBeDefined();
  });

  it('should render types', () => {
    const { container } = render(<QuestionForm subjects={subjects} />);
    expect(
      container.querySelectorAll(
        '[data-testid="question-form-type-input"] option'
      )
    ).toHaveLength(3);
  });

  it('should render a text input and label to question subject', () => {
    const { getByTestId } = render(<QuestionForm subjects={subjects} />);

    expect(getByTestId('question-form-subject-label')).toHaveTextContent(
      'Subject'
    );
    expect(getByTestId('question-form-subject-input')).toBeDefined();
  });

  it('should render all subjects', () => {
    const { container } = render(<QuestionForm subjects={subjects} />);
    expect(
      container.querySelectorAll(
        '[data-testid="question-form-subject-input"] option'
      )
    ).toHaveLength(subjects.length + 1);
  });

  it('should render a text input and label to question solution', () => {
    const { getByTestId } = render(<QuestionForm />);

    expect(getByTestId('question-form-solution-label')).toHaveTextContent(
      'Solution'
    );
    expect(getByTestId('question-form-solution-input')).toBeDefined();
  });

  it('should render a text input and label to question topic', () => {
    const { getByTestId } = render(<QuestionForm />);

    expect(getByTestId('question-form-topic-label')).toHaveTextContent(
      'Topics'
    );
    expect(getByTestId('question-form-topic-input')).toBeDefined();
  });

  it('should render alternatives true and false when True Or False type is selected', () => {
    const { getByTestId } = render(<QuestionForm />);

    fireEvent.change(getByTestId('question-form-type-input'), {
      target: { value: 'True Or False' },
    });

    expect(getByTestId('question-form-alternative-True')).toHaveTextContent(
      'True'
    );
    expect(getByTestId('question-form-alternative-False')).toHaveTextContent(
      'False'
    );
  });

  it('should render a save button', () => {
    const { getByTestId } = render(<QuestionForm />);

    expect(getByTestId('question-form-save-button')).toHaveTextContent('Save');
  });

  it('should render a save button', () => {
    const { getByTestId } = render(<QuestionForm />);

    expect(getByTestId('cancel-button')).toHaveTextContent('Cancel');
  });

  it('should save a question when click on save', () => {
    const onSubmit = jest.fn();

    const savedQuestion = {
      statement: 'Statement',
      type: 'True Or False',
      solution: 'Solution',
      topic: 'Topic 1; Topic 2;',
      subject: {
        id: '1',
      },
      correctAnswer: 'True',
      alternatives: [{ description: 'True' }, { description: 'False' }],
    };

    const { getByTestId } = render(
      <QuestionForm subjects={subjects} onSubmit={onSubmit} />
    );

    fireEvent.change(getByTestId('question-form-statement-input'), {
      target: { value: 'Statement' },
    });

    fireEvent.change(getByTestId('question-form-type-input'), {
      target: { value: 'True Or False' },
    });

    fireEvent.change(getByTestId('question-form-subject-input'), {
      target: { value: 1 },
    });

    fireEvent.change(getByTestId('question-form-solution-input'), {
      target: { value: 'Solution' },
    });

    fireEvent.change(getByTestId('question-form-topic-input'), {
      target: { value: 'Topic 1; Topic 2;' },
    });

    fireEvent.click(getByTestId('question-form-alternative-True'));

    fireEvent.click(getByTestId('question-form-save-button'));

    expect(onSubmit).toBeCalledTimes(1);

    expect(onSubmit).toBeCalledWith(savedQuestion);
  });

  it('should return to the previous page when click on cancel', () => {
    history.goBack = jest.fn();
    const { getByTestId } = render(<QuestionForm />);

    fireEvent.click(getByTestId('cancel-button'));

    expect(history.goBack).toBeCalledTimes(1);
  });

  it('should load the fields if question param is defined', async () => {
    const { getByTestId } = render(
      <QuestionForm questionData={questionData} subjects={subjects} />
    );

    expect(getByTestId('question-form-statement-input')).toHaveTextContent(
      questionData.statement
    );

    expect(getByTestId('question-form-type-input')).toHaveProperty(
      'value',
      questionData.type
    );

    expect(getByTestId('question-form-subject-input')).toHaveProperty(
      'value',
      questionData.subject.id.toString()
    );

    expect(getByTestId('question-form-solution-input')).toHaveTextContent(
      questionData.solution
    );

    expect(getByTestId('question-form-topic-input')).toHaveProperty(
      'value',
      questionData.topic
    );

    expect(getByTestId('question-form-alternative-True-input')).toHaveProperty(
      'checked'
    );
  });

  it('should not disable the type dropdown when question does not have an id', () => {
    const { getByTestId } = render(<QuestionForm />);

    expect(getByTestId('question-form-type-input')).toHaveProperty(
      'disabled',
      false
    );
  });

  it('should disable the type dropdown when question data has an id - edit mode', () => {
    const { getByTestId } = render(
      <QuestionForm questionData={questionData} />
    );

    expect(getByTestId('question-form-type-input')).toHaveProperty(
      'disabled',
      true
    );
  });
});
