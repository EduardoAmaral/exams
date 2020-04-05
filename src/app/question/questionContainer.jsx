import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { GET_QUESTION } from '../config/endpoint';
import QuestionItem from './questionItem';
import Loading from '../loading/loading';

export default function QuestionContainer() {
  const [loading, setLoading] = useState(false);
  const [questions, setQuestions] = useState([]);

  useEffect(() => {
    setLoading(true);
    axios.get(GET_QUESTION).then((response) => {
      setLoading(false);
      setQuestions(response.data);
    });
  }, []);

  if (loading) {
    return <Loading />;
  }

  return (
    <>
      <table className="ui celled table" data-testid="question-table">
        <thead>
          <tr>
            <th data-testid="question-header-statement">Statement</th>
            <th data-testid="question-header-subject">Subject</th>
            <th data-testid="question-header-type">Type</th>
          </tr>
        </thead>

        <tbody>
          {questions.map((question) => (
            <QuestionItem question={question} key={question.id} />
          ))}
        </tbody>
      </table>
    </>
  );
}
