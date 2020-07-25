import React from 'react';
import Question from '../../types/Question';

interface Props {
  question: Question;
  onEdit: (id: number) => void;
  onDelete: (id: number) => void;
  onDetail: (id: number) => void;
}

export default function QuestionItem({
  question,
  onEdit,
  onDelete,
  onDetail,
}: Props) {
  const id = question.id || 0;

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
        <div className="action-buttons">
          <button
            className="icon"
            type="button"
            data-testid={`question-detail-button-${id}`}
            onClick={() => onDetail(id)}
          >
            <i className="ri-information-line" title="Info" />
          </button>
          <button
            className="icon"
            type="button"
            data-testid={`question-edit-button-${id}`}
            onClick={() => onEdit(id)}
          >
            <i className="ri-pencil-line" title="Edit" />
          </button>
          <button
            className="icon"
            type="button"
            data-testid={`question-delete-button-${id}`}
            onClick={() => onDelete(id)}
          >
            <i className="ri-delete-bin-line" title="Delete" />
          </button>
        </div>
      </td>
    </tr>
  );
}
