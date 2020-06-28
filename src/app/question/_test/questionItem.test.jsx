import React from 'react';
import { fireEvent, render } from '@testing-library/react';
import QuestionItem from '../questionItem';

describe('<QuestionItem />', () => {
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
    const { getByTestId } = render(<QuestionItem question={question} />);

    const testId = `question-statement-${question.id}`;

    expect(getByTestId(testId)).toHaveTextContent('Question?');
  });

  it('should have a subject', () => {
    const { getByTestId } = render(<QuestionItem question={question} />);

    const testId = `question-subject-${question.id}`;

    expect(getByTestId(testId)).toHaveTextContent('English');
  });

  it('should have a type', () => {
    const { getByTestId } = render(<QuestionItem question={question} />);

    const testId = `question-type-${question.id}`;

    expect(getByTestId(testId)).toHaveTextContent('True Or False');
  });

  it('should render a delete button', () => {
    const { getByTestId } = render(<QuestionItem question={question} />);

    const testId = `question-delete-button-${question.id}`;

    expect(getByTestId(testId)).toBeDefined();
  });

  it('should have called onDelete function when click on the delete button', () => {
    const onDelete = jest.fn();
    const { getByTestId } = render(
      <QuestionItem question={question} onDelete={onDelete} />
    );

    const testId = `question-delete-button-${question.id}`;

    fireEvent.click(getByTestId(testId));

    expect(onDelete).toBeCalledTimes(1);
    expect(onDelete).toBeCalledWith(question.id);
  });

  it('should render a edit button', () => {
    const { getByTestId } = render(<QuestionItem question={question} />);

    const testId = `question-edit-button-${question.id}`;

    expect(getByTestId(testId)).toBeDefined();
  });

  it('should have called onEdit function when click on the edit button', () => {
    const onEdit = jest.fn();
    const { getByTestId } = render(
      <QuestionItem question={question} onEdit={onEdit} />
    );

    const testId = `question-edit-button-${question.id}`;

    fireEvent.click(getByTestId(testId));

    expect(onEdit).toBeCalledTimes(1);
    expect(onEdit).toBeCalledWith(question.id);
  });

  it('should render an info button', () => {
    const { getByTestId } = render(<QuestionItem question={question} />);

    const testId = `question-detail-button-${question.id}`;

    expect(getByTestId(testId)).toBeDefined();
  });

  it('should have called onDetail function when click on the detail button', () => {
    const onDetail = jest.fn();
    const { getByTestId } = render(
      <QuestionItem question={question} onDetail={onDetail} />
    );

    const testId = `question-detail-button-${question.id}`;

    fireEvent.click(getByTestId(testId));

    expect(onDetail).toBeCalledTimes(1);
    expect(onDetail).toBeCalledWith(question.id);
  });
});
