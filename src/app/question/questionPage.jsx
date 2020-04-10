import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { DELETE_QUESTION, QUESTION } from '../config/endpoint';
import QuestionItem from './questionItem';
import Loading from '../loading/loading';
import history from '../config/history';

export default function QuestionPage() {
  const [loading, setLoading] = useState(false);
  const [questions, setQuestions] = useState([]);

  useEffect(() => {
    setLoading(true);
    axios
      .get(QUESTION)
      .then((response) => {
        setLoading(false);
        setQuestions(response.data);
      })
      .catch((err) => {
        console.log(err);
        setLoading(false);
      });
  }, []);

  const redirectToCreate = () => {
    history.push('/question/create');
  };

  const redirectToEdit = (id) => {
    history.push(`/question/edit/${id}`);
  };

  const redirectToDetail = (id) => {
    history.push(`/question/detail/${id}`);
  };

  const onDelete = (id) => {
    const confirmed = window.confirm(
      `Are you sure you want to delete the question ${id}?`
    );

    if (confirmed) {
      setLoading(true);
      axios.delete(DELETE_QUESTION.replace(':id', id)).then(() => {
        setLoading(false);
        setQuestions(questions.filter((q) => q.id !== id));
      });
    }
  };

  if (loading) {
    return <Loading />;
  }

  return (
    <div data-testid="question-page" className="ui container">
      <h1>Questions</h1>
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
              onDelete={onDelete}
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
  );
}
