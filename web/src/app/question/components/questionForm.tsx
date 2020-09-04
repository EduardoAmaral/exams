import Axios from 'axios';
import React, { FormEvent, useEffect, useState } from 'react';
import { SUBJECT } from '../../config/endpoint';
import history from '../../config/history';
import Question, { QuestionErrors } from '../../types/Question';
import Subject from '../../types/Subject';
import './questionForm.scss';
import SubjectButton from './subjectButton';

interface Props {
  questionData?: Question;
  errors?: QuestionErrors;
  onSubmit: (question: Partial<Question>) => void;
}

export default function QuestionForm({
  questionData,
  errors = {},
  onSubmit,
}: Props) {
  const [question, setQuestion] = useState<Partial<Question>>({
    topic: '',
    solution: '',
  });
  const [subjects, setSubjects] = useState<Subject[]>([]);
  const [selectedSubject, setSelectedSubject] = useState<number>();
  const [loadingSubjects, setLoadingSubjects] = useState<boolean>();

  const TRUE_OR_FALSE = 'True Or False';
  const MULTIPLE_CHOICES = 'Multiple Choices';

  const types = [
    {
      value: TRUE_OR_FALSE,
    },
    {
      value: MULTIPLE_CHOICES,
    },
  ];

  const trueOrFalseAlternatives = [
    { position: 1, description: 'True' },
    { position: 2, description: 'False' },
  ];

  const multipleChoicesAlternatives = [
    { position: 1, description: '' },
    { position: 2, description: '' },
    { position: 3, description: '' },
    { position: 4, description: '' },
    { position: 5, description: '' },
  ];

  useEffect(() => {
    setLoadingSubjects(true);
    Axios.get(SUBJECT)
      .then((response) => {
        setLoadingSubjects(false);
        setSubjects(response.data);
      })
      .catch(() => {
        setLoadingSubjects(false);
      });
  }, []);

  useEffect(() => {
    if (questionData !== undefined) {
      let alternativePosition = 0;
      setQuestion({
        ...questionData,
        alternatives: questionData?.alternatives?.map((q) => ({
          ...q,
          // eslint-disable-next-line no-plusplus
          position: ++alternativePosition,
        })),
      });
      setSelectedSubject(questionData?.subject?.id);
    }
  }, [questionData]);

  const onSubmitForm = (event: FormEvent) => {
    event.preventDefault();

    onSubmit(question);
  };

  const onCancelClick = () => {
    history.goBack();
  };

  const renderStatementInput = () => {
    return (
      <>
        <label htmlFor="statement">Statement</label>
        <textarea
          id="statement"
          name="statement"
          rows={4}
          className={`${errors.statement ? 'error' : ''}`}
          value={question.statement}
          onChange={(event) => {
            setQuestion({ ...question, statement: event.target.value });
          }}
        />
        {errors.statement ? (
          <div className="validation-error">{errors.statement}</div>
        ) : null}
      </>
    );
  };

  const renderTypeSelect = () => {
    return (
      <>
        <label htmlFor="type">Type</label>
        <select
          id="type"
          name="type"
          className={`${errors.type ? 'error' : ''}`}
          value={question.type}
          disabled={question.id !== undefined}
          onChange={(event) => {
            switch (event.target.value) {
              case TRUE_OR_FALSE:
                setQuestion({
                  ...question,
                  type: event.target.value,
                  alternatives: trueOrFalseAlternatives,
                });
                break;
              case MULTIPLE_CHOICES:
                setQuestion({
                  ...question,
                  type: event.target.value,
                  alternatives: multipleChoicesAlternatives,
                });
                break;
              default:
                setQuestion({
                  ...question,
                  type: event.target.value,
                  alternatives: [],
                });
                break;
            }
          }}
        >
          <option value="" label="Select an option" />
          {types.map((type) => (
            <option key={type.value} value={type.value} label={type.value} />
          ))}
        </select>
        {errors.type ? (
          <div className="validation-error">{errors.type}</div>
        ) : null}
      </>
    );
  };

  const renderSubjectSelect = () => {
    return (
      !loadingSubjects && (
        <>
          <label htmlFor="subject">Subject</label>
          <div className="subjectContainer">
            <select
              id="subject"
              name="subject"
              className={`${errors.subject ? 'error' : ''}`}
              value={selectedSubject}
              onChange={(event) => {
                setQuestion({
                  ...question,
                  subject: {
                    id: Number.parseInt(event.target.value),
                  },
                });
                setSelectedSubject(Number.parseInt(event.target.value));
              }}
            >
              <option value="" label="Select an option" />
              {subjects?.map((subject) => (
                <option
                  value={subject.id}
                  key={subject.id}
                  label={subject.description}
                />
              ))}
            </select>
            <SubjectButton
              onSave={(subject: Subject) => {
                console.log('Here: ', subject);
                setSubjects((s) => [...s, subject]);
                setSelectedSubject(subject.id);
              }}
            />
          </div>
          {errors.subject ? (
            <div className="validation-error">{errors.subject}</div>
          ) : null}
        </>
      )
    );
  };

  const renderSolutionInput = () => {
    return (
      <>
        <label htmlFor="solution">Solution</label>
        <textarea
          id="solution"
          name="solution"
          className={`${errors.solution ? 'error' : ''}`}
          rows={3}
          value={question.solution}
          onChange={(event) => {
            setQuestion({ ...question, solution: event.target.value });
          }}
        />
        {errors.solution ? (
          <div className="validation-error">{errors.solution}</div>
        ) : null}
      </>
    );
  };

  const renderTopicsInput = () => {
    return (
      <>
        <label htmlFor="topic">Topic</label>
        <input
          id="topic"
          name="topic"
          className={`${errors.topic ? 'error' : ''}`}
          type="text"
          value={question.topic}
          onChange={(event) => {
            setQuestion({ ...question, topic: event.target.value });
          }}
        />
        {errors.topic ? (
          <div className="validation-error">{errors.topic}</div>
        ) : null}
      </>
    );
  };

  const renderAlternatives = () => {
    if (question.alternatives) {
      return (
        <div className="alternatives">
          <span>Alternatives:</span>
          {errors.alternatives ? (
            <div className="validation-error">{errors.alternatives}</div>
          ) : null}
          {errors.correctAnswer ? (
            <div className="validation-error">{errors.correctAnswer}</div>
          ) : null}
          {question.alternatives.map((alternative) => (
            <div key={alternative.position}>
              <label
                data-testid={`question-form-alternative-${alternative.position}`}
              >
                <input
                  type="radio"
                  name="correctAnswer"
                  value={alternative.description}
                  data-testid={`question-form-alternative-${alternative.position}-radio`}
                  checked={alternative.description === question.correctAnswer}
                  onChange={(event) => {
                    setQuestion({
                      ...question,
                      correctAnswer: event.target.value,
                    });
                  }}
                />
                {question.type === TRUE_OR_FALSE ? (
                  alternative.description
                ) : (
                  <input
                    type="input"
                    name={`alternative-${alternative.position}`}
                    data-testid={`question-form-alternative-${alternative.position}-input`}
                    className="form-control"
                    value={alternative.description}
                    onChange={(event) => {
                      const newAlternatives = question.alternatives?.map(
                        (a) => {
                          return a.position === alternative.position
                            ? {
                                ...alternative,
                                description: event.target.value,
                              }
                            : a;
                        }
                      );
                      setQuestion({
                        ...question,
                        alternatives: newAlternatives,
                      });
                    }}
                  />
                )}
              </label>
            </div>
          ))}
        </div>
      );
    }
    return null;
  };

  return (
    <form className="form" onSubmit={onSubmitForm} aria-label="form">
      <div className="container">
        <div className="statement">{renderStatementInput()}</div>
        <div className="type">{renderTypeSelect()}</div>
        <div className="subject">{renderSubjectSelect()}</div>
        <div className="solution">{renderSolutionInput()}</div>
        <div className="topic">{renderTopicsInput()}</div>
        <div className="alternatives">{renderAlternatives()}</div>
      </div>
      <div className="right">
        <button type="button" onClick={onCancelClick}>
          Cancel
        </button>
        <button className="primary" type="submit">
          Save
        </button>
      </div>
    </form>
  );
}
