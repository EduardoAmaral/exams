import Axios from 'axios';
import React, { FormEvent, useEffect, useState } from 'react';
import { EXAM, QUESTION } from '../config/endpoint';
import Exam from '../types/Exam';
import history from '../config/history';
import style from './createExamPage.module.scss';
import Question from '../types/Question';
import Loading from '../loading/loading';

export default function CreateExamPage() {
  const [exam, setExam] = useState<Partial<Exam>>({
    title: '',
    startDateTime: '',
    endDateTime: '',
  });

  const [isLoading, setLoading] = useState<boolean>(false);
  const [questions, setQuestions] = useState<Question[]>([]);

  useEffect(() => {
    setLoading(true);
    Axios.get(QUESTION)
      .then((response) => {
        setLoading(false);
        setQuestions(response.data);
      })
      .catch(() => {
        setLoading(false);
      });
  }, []);

  const saveExam = (event: FormEvent) => {
    event.preventDefault();

    const startDateTime = exam.startDateTime
      ? new Date(exam.startDateTime).toISOString()
      : null;

    const endDateTime = exam.endDateTime
      ? new Date(exam.endDateTime).toISOString()
      : null;

    setLoading(true);
    Axios.post(EXAM, {
      ...exam,
      startDateTime,
      endDateTime,
      questions: questions.filter((question) => question.selected),
    })
      .then(() => {
        history.goBack();
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const renderTitle = () => {
    return (
      <>
        <label htmlFor="title">Title</label>
        <input
          id="title"
          name="title"
          type="text"
          value={exam.title}
          onChange={(e) => setExam({ ...exam, title: e.target.value })}
        />
      </>
    );
  };

  const renderStartsAt = () => {
    return (
      <>
        <label htmlFor="startsAt">Starts at</label>
        <input
          id="startsAt"
          name="startdatetime"
          type="datetime-local"
          value={exam.startDateTime}
          onChange={(e) => setExam({ ...exam, startDateTime: e.target.value })}
        />
      </>
    );
  };

  const renderEndsAt = () => {
    return (
      <>
        <label htmlFor="endsAt">Ends at</label>
        <input
          id="endsAt"
          name="enddatetime"
          type="datetime-local"
          value={exam.endDateTime}
          onChange={(e) => setExam({ ...exam, endDateTime: e.target.value })}
        />
      </>
    );
  };

  const renderQuestions = () => {
    return (
      <>
        <table>
          <thead>
            <tr>
              <th />
              <th>Statement</th>
              <th>Subject</th>
              <th>Type</th>
              <th>Author</th>
            </tr>
          </thead>
          <tbody>
            {questions.map((question) => (
              <tr key={question.id}>
                <td>
                  <input
                    type="checkbox"
                    data-testid={`question-selected-${question.id}-input`}
                    checked={question.selected === true}
                    onClick={() =>
                      setQuestions((previous) =>
                        previous.map((q) => {
                          if (q.id === question.id) {
                            return { ...q, selected: !q.selected };
                          }
                          return q;
                        })
                      )
                    }
                  />
                </td>
                <td>{question.statement}</td>
                <td>{question.subject.description}</td>
                <td>{question.type}</td>
                <td>{question.authorName}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </>
    );
  };

  return (
    <>
      <Loading isLoading={isLoading} />
      <section>
        <h2>Create Exam</h2>
        <form onSubmit={saveExam}>
          <div className={style.container}>
            <div className={style.title}>{renderTitle()}</div>
            <div>{renderStartsAt()}</div>
            <div>{renderEndsAt()}</div>
            <div className={style.questions}>{renderQuestions()}</div>
          </div>
          <div className="right">
            <button type="button" onClick={() => history.goBack()}>
              Cancel
            </button>
            <button className="primary" type="submit">
              Save
            </button>
          </div>
        </form>
      </section>
    </>
  );
}
