import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { GET_SUBJECT } from '../config/endpoint';
import Loading from '../loading/loading';
import QuestionForm from './questionForm';

export default function QuestionCreatePage() {
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(false);
  const newQuestion = {};

  useEffect(() => {
    setLoading(true);
    axios
      .get(GET_SUBJECT)
      .then((response) => {
        setLoading(false);
        setSubjects(response.data);
      })
      .catch((err) => {
        console.log(err);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <Loading />;
  }

  return (
    <div data-testid="question-create-page" className="ui container">
      <h1>Create Question</h1>
      <QuestionForm question={newQuestion} subjects={subjects} />
    </div>
  );
}
