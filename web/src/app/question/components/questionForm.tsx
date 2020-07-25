import React, { useEffect, useState, FormEvent } from 'react';
import history from '../../config/history';
import './questionForm.scss';
import Question, { QuestionErrors } from '../../types/Question';
import Subject from '../../types/Subject';

interface Props {
  questionData?: Question;
  subjects?: Subject[];
  errors?: QuestionErrors;
  onSubmit: (question: Partial<Question>) => void;
}

export default function QuestionForm({
  questionData,
  subjects = [],
  errors = {},
  onSubmit,
}: Props) {
  const [question, setQuestion] = useState<Partial<Question>>({
    topic: '',
    solution: '',
  });
  const [selectedSubject, setSelectedSubject] = useState<number>();

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
        <label data-testid="question-form-statement-label">
          Statement
          <textarea
            rows={4}
            className="form-control"
            data-testid="question-form-statement-input"
            value={question.statement}
            onChange={(event) => {
              setQuestion({ ...question, statement: event.target.value });
            }}
          />
        </label>
        {errors.statement ? (
          <div
            className="validation-error"
            data-testid="question-form-statement-error"
          >
            {errors.statement}
          </div>
        ) : null}
      </>
    );
  };

  const renderTypeSelect = () => {
    return (
      <>
        <label data-testid="question-form-type-label">
          Type
          <select
            className="form-control"
            data-testid="question-form-type-input"
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
              <option
                key={type.value}
                value={type.value}
                label={type.value}
                data-testid={type.value}
              />
            ))}
          </select>
        </label>
        {errors.type ? (
          <div
            className="validation-error"
            data-testid="question-form-type-error"
          >
            {errors.type}
          </div>
        ) : null}
      </>
    );
  };

  const renderSubjectSelect = () => {
    return (
      <>
        <label data-testid="question-form-subject-label">
          Subject
          <select
            className="form-control"
            data-testid="question-form-subject-input"
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
            {subjects.map((subject) => (
              <option
                value={subject.id}
                key={subject.id}
                label={subject.description}
              />
            ))}
          </select>
        </label>
        {errors.subject ? (
          <div
            className="validation-error"
            data-testid="question-form-subject-error"
          >
            {errors.subject}
          </div>
        ) : null}
      </>
    );
  };

  const renderSolutionInput = () => {
    return (
      <>
        <label data-testid="question-form-solution-label">
          Solution
          <textarea
            className="form-control"
            rows={3}
            data-testid="question-form-solution-input"
            placeholder="Tell your students why the answer is that"
            value={question.solution}
            onChange={(event) => {
              setQuestion({ ...question, solution: event.target.value });
            }}
          />
        </label>
        {errors.solution ? (
          <div
            className="validation-error"
            data-testid="question-form-solution-error"
          >
            {errors.solution}
          </div>
        ) : null}
      </>
    );
  };

  const renderTopicsInput = () => {
    return (
      <>
        <label data-testid="question-form-topic-label">
          Topic
          <input
            className="form-control"
            data-testid="question-form-topic-input"
            type="text"
            value={question.topic}
            onChange={(event) => {
              setQuestion({ ...question, topic: event.target.value });
            }}
          />
        </label>
        {errors.topic ? (
          <div
            className="validation-error"
            data-testid="question-form-topic-error"
          >
            {errors.topic}
          </div>
        ) : null}
      </>
    );
  };

  const renderAlternatives = () => {
    if (question.alternatives) {
      return (
        <div className="alternatives">
          Alternatives:
          {errors.alternatives ? (
            <div
              className="validation-error"
              data-testid="question-form-alternatives-error"
            >
              {errors.alternatives}
            </div>
          ) : null}
          {errors.correctAnswer ? (
            <div
              className="validation-error"
              data-testid="question-form-correct-answer-error"
            >
              {errors.correctAnswer}
            </div>
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
    <form
      data-testid="question-form"
      className="ui form"
      onSubmit={onSubmitForm}
    >
      <div className="row field">
        <div className="col-md-12">
          <div className="form-group">{renderStatementInput()}</div>
        </div>
      </div>
      <div className="row field">
        <div className="col-md-4">{renderTypeSelect()}</div>
        <div className="col-md-4 offset-col-2">{renderSubjectSelect()}</div>
      </div>
      <div className="row field">
        <div className="col-md-12">{renderSolutionInput()}</div>
      </div>
      <div className="row field">
        <div className="col-md-12">{renderTopicsInput()}</div>
      </div>
      <div className="row field">
        <div className="col-md-6">{renderAlternatives()}</div>
      </div>
      <div className="row">
        <div className="col-md-12 right">
          <button
            type="button"
            data-testid="cancel-button"
            onClick={onCancelClick}
          >
            Cancel
          </button>
          <button
            className="primary"
            type="submit"
            data-testid="question-form-save-button"
          >
            Save
          </button>
        </div>
      </div>
    </form>
  );
}
