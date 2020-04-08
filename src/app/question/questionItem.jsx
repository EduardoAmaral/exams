import React from 'react';

export default function QuestionItem({ question, onDelete = () => {} }) {
  const { id } = question;

  return (
    <tr data-testid={`question-${id}`}>
      <td data-label-name="Statement" data-testid={`question-statement-${id}`}>
        {question.statement}
      </td>
      <td data-label-name="Subject" data-testid={`question-subject-${id}`}>
        {question.subject.description}
      </td>
      <td data-label-name="Type" data-testid={`question-type-${id}`}>
        {question.type}
      </td>
      <td>
        <button
          className="ui button"
          type="button"
          data-testid={`question-delete-button-${id}`}
          onClick={() => onDelete(id)}
        >
          <i className="trash icon" title="delete" />
        </button>
      </td>
    </tr>
  );
}
