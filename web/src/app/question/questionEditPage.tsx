import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { QUESTION, QUESTION_BY_ID } from '../config/endpoint';
import history from '../config/history';
import Loading from '../loading/loading';
import Question from '../types/Question';
import QuestionForm from './components/questionForm';

interface QueryParams {
  readonly id: string;
}

export default function QuestionEditPage(): JSX.Element {
  const [isLoading, setLoading] = useState<boolean>(false);
  const [question, setQuestion] = useState<Question>();
  const [errors, setErrors] = useState({});

  const { id } = useParams<QueryParams>();

  useEffect(() => {
    setLoading(true);

    axios
      .get(QUESTION_BY_ID.replace(':id', id))
      .then((response) => {
        setLoading(false);
        setQuestion(response.data);
      })
      .catch(() => {
        setLoading(false);
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
      .catch(({ response }) => {
        setLoading(false);
        setErrors(response.data.errors);
      });
  };

  return (
    <>
      <Loading isLoading={isLoading} />
      <section>
        <h2>Edit Question</h2>
        <QuestionForm
          questionData={question}
          onSubmit={onSubmit}
          errors={errors}
        />
      </section>
    </>
  );
}
