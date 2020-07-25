import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { DELETE_QUESTION, QUESTION } from '../config/endpoint';
import QuestionItem from './components/questionItem';
import Loading from '../loading/loading';
import history from '../config/history';
import Question from '../types/Question';

export default function QuestionPage() {
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
      <div data-testid="question-page">
        <h2>Questions</h2>
        <table className="ui celled table" data-testid="question-table">
          <thead>
            <tr>
              <th data-testid="question-header-statement">Statement</th>
              <th data-testid="question-header-subject">Subject</th>
              <th data-testid="question-header-type">Type</th>
              <th data-testid="question-header-actions">Actions</th>
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
      </div>
    </>
  );
}
