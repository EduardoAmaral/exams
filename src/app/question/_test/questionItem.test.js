import React from 'react';
import { render, screen } from '@testing-library/react';
import QuestionItem from '../questionItem';

describe('Question Item', () => {
  const question = {
    id: 1,
    statement: 'Question?',
    type: 'True Or False',
    subject: {
      id: 1,
      description: 'English',
    },
  };

  it('should have a statement', () => {
    render(<QuestionItem question={question} />);

    const testId = `question-statement-${question.id}`;

    expect(screen.getByTestId(testId)).toHaveTextContent('Question?');
  });

  it('should have a subject', () => {
    render(<QuestionItem question={question} />);

    const testId = `question-subject-${question.id}`;

    expect(screen.getByTestId(testId)).toHaveTextContent('English');
  });

  it('should have a type', () => {
    render(<QuestionItem question={question} />);

    const testId = `question-type-${question.id}`;

    expect(screen.getByTestId(testId)).toHaveTextContent('True Or False');
  });
});
