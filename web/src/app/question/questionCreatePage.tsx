import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { QUESTION, SUBJECT } from '../config/endpoint';
import Loading from '../loading/loading';
import QuestionForm from './questionForm';
import history from '../config/history';
import Question from '../types/Question';
import Subject from '../types/Subject';

export default function QuestionCreatePage() {
  const [subjects, setSubjects] = useState<Subject[]>([]);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});

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

  const handleSubmit = (question: Partial<Question>) => {
    setLoading(true);
    axios
      .post(QUESTION, question)
      .then((response) => {
        if (response.status === 201) {
          history.goBack();
        }
      })
      .catch(({ response }) => {
        setLoading(false);
        setErrors(response.data.errors);
      });
  };

  if (loading) {
    return <Loading />;
  }

  return (
    <div data-testid="question-create-page">
      <h2>Create Question</h2>
      <QuestionForm
        subjects={subjects}
        onSubmit={handleSubmit}
        errors={errors}
      />
    </div>
  );
}
