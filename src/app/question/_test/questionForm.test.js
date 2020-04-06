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

  const question = {};

  it('should render a form', () => {
    const { getByTestId } = render(<QuestionForm question={question} />);

    expect(getByTestId('question-form')).toBeDefined();
  });

  it('should render a text input and label to question statement', () => {
    const { getByTestId } = render(<QuestionForm question={question} />);

    expect(getByTestId('question-form-statement-label')).toHaveTextContent(
      'Statement'
    );
    expect(getByTestId('question-form-statement-input')).toBeDefined();
  });

  it('should render a text input and label to question type', () => {
    const { getByTestId } = render(<QuestionForm question={question} />);

    expect(getByTestId('question-form-type-label')).toHaveTextContent('Type');
    expect(getByTestId('question-form-type-input')).toBeDefined();
  });

  it('should render types', () => {
    const { container } = render(
      <QuestionForm question={question} subjects={subjects} />
    );
    expect(
      container.querySelectorAll(
        '[data-testid="question-form-type-input"] option'
      )
    ).toHaveLength(2);
  });

  it('should render a text input and label to question subject', () => {
    const { getByTestId } = render(
      <QuestionForm question={question} subjects={subjects} />
    );

    expect(getByTestId('question-form-subject-label')).toHaveTextContent(
      'Subject'
    );
    expect(getByTestId('question-form-subject-input')).toBeDefined();
  });

  it('should render all subjects', () => {
    const { container } = render(
      <QuestionForm question={question} subjects={subjects} />
    );
    expect(
      container.querySelectorAll(
        '[data-testid="question-form-subject-input"] option'
      )
    ).toHaveLength(2);
  });

  it('should render a text input and label to question solution', () => {
    const { getByTestId } = render(<QuestionForm question={question} />);

    expect(getByTestId('question-form-solution-label')).toHaveTextContent(
      'Solution'
    );
    expect(getByTestId('question-form-solution-input')).toBeDefined();
  });

  it('should render a text input and label to question topic', () => {
    const { getByTestId } = render(<QuestionForm question={question} />);

    expect(getByTestId('question-form-topic-label')).toHaveTextContent(
      'Topics'
    );
    expect(getByTestId('question-form-topic-input')).toBeDefined();
  });

  it('should render alternatives true and false when True Or False type is selected', () => {
    const { getByTestId } = render(<QuestionForm question={question} />);

    fireEvent.change(getByTestId('question-form-type-input'), {
      target: { value: 'TRUE_OR_FALSE' },
    });

    expect(getByTestId('question-form-alternative-true')).toHaveTextContent(
      'True'
    );
    expect(getByTestId('question-form-alternative-false')).toHaveTextContent(
      'False'
    );
  });
});
