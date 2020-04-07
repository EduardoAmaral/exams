import React from 'react';
import { fireEvent, render } from '@testing-library/react';
import QuestionForm from '../questionForm';

describe('Question Form', () => {
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
    ).toHaveLength(2);
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
    ).toHaveLength(2);
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
      target: { value: 'TRUE_OR_FALSE' },
    });

    expect(getByTestId('question-form-alternative-True')).toHaveTextContent(
      'True'
    );
    expect(getByTestId('question-form-alternative-False')).toHaveTextContent(
      'False'
    );
  });

  it('should save a question when click on save', () => {
    const onSubmit = jest.fn();

    const savedQuestion = {
      statement: 'Statement',
      type: 'TRUE_OR_FALSE',
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
      target: { value: 'TRUE_OR_FALSE' },
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

    expect(onSubmit).toBeCalled();

    expect(onSubmit).toBeCalledWith(savedQuestion);
  });
});
