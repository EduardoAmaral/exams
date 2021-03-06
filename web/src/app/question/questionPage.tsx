import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { DELETE_QUESTION, QUESTION } from '../config/endpoint';
import Loading from '../loading/loading';
import history from '../config/history';
import Question from '../types/Question';
import { IconButton } from '@material-ui/core';
import { InfoOutlined, EditOutlined, DeleteOutlined } from '@material-ui/icons';

export default function QuestionPage(): JSX.Element {
  const [isLoading, setLoading] = useState<boolean>(false);
  const [questions, setQuestions] = useState<Question[]>([]);

  useEffect(() => {
    setLoading(true);
    axios
      .get(QUESTION)
      .then((response) => {
        setLoading(false);
        setQuestions(response.data);
      })
      .catch(() => {
        setLoading(false);
      });
  }, []);

  const redirectToCreate = () => {
    history.push('/question/create');
  };

  const redirectToEdit = (id: number) => {
    history.push(`/question/edit/${id}`);
  };

  const redirectToDetail = (id: number) => {
    history.push(`/question/detail/${id}`);
  };

  const handleDelete = (id: number) => {
    const confirmed = window.confirm(
      `Are you sure you want to delete the question ${id}?`
    );

    if (confirmed) {
      setLoading(true);
      axios.delete(DELETE_QUESTION.replace(':id', id.toString())).then(() => {
        setLoading(false);
        setQuestions(questions.filter((q) => q.id !== id));
      });
    }
  };

  return (
    <>
      <Loading isLoading={isLoading} />
      <section>
        <h2>My questions</h2>
        <table className="table">
          <thead>
            <tr>
              <th>Statement</th>
              <th>Subject</th>
              <th>Type</th>
              <th />
            </tr>
          </thead>

          <tbody>
            {questions.map((question) => (
              <QuestionItem
                question={question}
                key={question.id}
                onEdit={redirectToEdit}
                onDelete={handleDelete}
                onDetail={redirectToDetail}
              />
            ))}
          </tbody>
        </table>
        <div className="right">
          <button
            className="positive ui button"
            type="button"
            data-testid="question-create-button"
            onClick={redirectToCreate}
          >
            Create Question
          </button>
        </div>
      </section>
    </>
  );
}

interface QuestionItemProps {
  question: Question;
  onEdit: (id: number) => void;
  onDelete: (id: number) => void;
  onDetail: (id: number) => void;
}

export function QuestionItem({
  question,
  onEdit,
  onDelete,
  onDetail,
}: QuestionItemProps): JSX.Element {
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
          <IconButton
            aria-label="detail"
            type="button"
            className="icon"
            data-testid={`question-detail-button-${id}`}
            onClick={() => onDetail(id)}
          >
            <InfoOutlined fontSize="small" />
          </IconButton>
          <IconButton
            aria-label="edit"
            type="button"
            className="icon"
            data-testid={`question-edit-button-${id}`}
            onClick={() => onEdit(id)}
          >
            <EditOutlined fontSize="small" />
          </IconButton>
          <IconButton
            aria-label="delete"
            type="button"
            className="icon"
            data-testid={`question-delete-button-${id}`}
            onClick={() => onDelete(id)}
          >
            <DeleteOutlined fontSize="small" />
          </IconButton>
        </div>
      </td>
    </tr>
  );
}
