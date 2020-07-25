import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router';
import { QUESTION, QUESTION_BY_ID, SUBJECT } from '../config/endpoint';
import Loading from '../loading/loading';
import QuestionForm from './components/questionForm';
import history from '../config/history';
import Question from '../types/Question';
import Subject from '../types/Subject';

export default function QuestionEditPage() {
  const [loading, setLoading] = useState<boolean>(false);
  const [question, setQuestion] = useState<Question>();
  const [subjects, setSubjects] = useState<Subject[]>([]);

  const { id } = useParams();

  useEffect(() => {
    setLoading(true);

    axios
      .get(QUESTION_BY_ID.replace(':id', id))
      .then((response) => {
        setLoading(false);
        setQuestion(response.data);
      })
      .catch((err) => {
        console.log(err);
        setLoading(false);
      });

    axios
      .get(SUBJECT)
      .then((response) => {
        setSubjects(response.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [id]);

  const onSubmit = (updatedQuestion: Partial<Question>) => {
    setLoading(true);
    axios
      .put(QUESTION, updatedQuestion)
      .then((response) => {
        if (response.status === 200) {
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
    <div data-testid="question-edit-page">
      <h2>Edit Question</h2>
      <QuestionForm
        questionData={question}
        subjects={subjects}
        onSubmit={onSubmit}
      />
    </div>
  );
}
