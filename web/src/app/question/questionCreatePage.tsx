import axios from 'axios';
import React, { useState } from 'react';
import { QUESTION } from '../config/endpoint';
import history from '../config/history';
import Loading from '../loading/loading';
import Question from '../types/Question';
import QuestionForm from './components/questionForm';

export default function QuestionCreatePage(): JSX.Element {
  const [isLoading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});

  const handleSubmit = (question: Partial<Question>) => {
    setLoading(true);
    axios
      .post(QUESTION, question)
      .then(() => {
        history.goBack();
      })
      .catch(({ response }) => {
        setLoading(false);
        setErrors(response.data.errors);
      });
  };

  return (
    <>
      <Loading isLoading={isLoading} />
      <section>
        <h2>Create Question</h2>
        <QuestionForm onSubmit={handleSubmit} errors={errors} />
      </section>
    </>
  );
}
