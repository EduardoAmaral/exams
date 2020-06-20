import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { QUESTION, SUBJECT } from '../config/endpoint';
import Loading from '../loading/loading';
import QuestionForm from './questionForm';
import history from '../config/history';
import './scss/questionCreatePage.scss';

export default function QuestionCreatePage() {
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    axios
      .get(SUBJECT)
      .then((response) => {
        setLoading(false);
        setSubjects(response.data);
      })
      .catch((err) => {
        console.log(err);
        setLoading(false);
      });
  }, []);

  const onSubmit = (question) => {
    setLoading(true);
    axios
      .post(QUESTION, question)
      .then((response) => {
        if (response.status === 201) {
          setLoading(false);
          history.goBack();
        }
      })
      .catch((err) => {
        setLoading(false);
        console.log(err);
      });
  };

  if (loading) {
    return <Loading />;
  }

  return (
    <div data-testid="question-create-page">
      <h1>Create Question</h1>
      <QuestionForm subjects={subjects} onSubmit={onSubmit} />
    </div>
  );
}
