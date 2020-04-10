import React, { useEffect, useState } from 'react';
import history from '../config/history';

export default function QuestionForm({
  questionData,
  subjects = [],
  onSubmit = () => {},
}) {
  const [question, setQuestion] = useState({ subject: {} });

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
    { description: 'True' },
    { description: 'False' },
  ];

  useEffect(() => {
    if (questionData !== undefined && questionData.subject !== undefined) {
      setQuestion(questionData);
    }
  }, [questionData]);

  const onSubmitForm = (event) => {
    event.preventDefault();

    onSubmit(question);
  };

  const onCancelClick = () => {
    history.goBack();
  };

  return (
    <form
      data-testid="question-form"
      className="ui form"
      onSubmit={onSubmitForm}
    >
      <div className="field">
        <label data-testid="question-form-statement-label">
          Statement
          <textarea
            rows="2"
            data-testid="question-form-statement-input"
            value={question.statement}
            onChange={(event) => {
              setQuestion({ ...question, statement: event.target.value });
            }}
          />
        </label>
      </div>
      <div className="two fields">
        <div className="field">
          <label data-testid="question-form-type-label">
            Type
            <select
              className="ui fluid dropdown"
              data-testid="question-form-type-input"
              value={question.type}
              disabled={question.id !== undefined}
              onChange={(event) => {
                if (event.target.value === TRUE_OR_FALSE) {
                  setQuestion({
                    ...question,
                    type: event.target.value,
                    alternatives: trueOrFalseAlternatives,
                  });
                } else {
                  setQuestion({
                    ...question,
                    type: event.target.value,
                    alternatives: [],
                  });
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
        </div>
        <div className="field">
          <label data-testid="question-form-subject-label">
            Subject
            <select
              className="ui fluid dropdown"
              data-testid="question-form-subject-input"
              value={question.subject.id}
              onChange={(event) => {
                setQuestion({
                  ...question,
                  subject: {
                    id: event.target.value,
                  },
                });
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
        </div>
      </div>
      <div className="field">
        <label data-testid="question-form-solution-label">
          Solution
          <textarea
            rows="2"
            data-testid="question-form-solution-input"
            value={question.solution}
            onChange={(event) => {
              setQuestion({ ...question, solution: event.target.value });
            }}
          />
        </label>
      </div>
      <div className="field">
        <label data-testid="question-form-topic-label">
          Topics
          <input
            data-testid="question-form-topic-input"
            type="text"
            value={question.topic}
            onChange={(event) => {
              setQuestion({ ...question, topic: event.target.value });
            }}
          />
        </label>
      </div>
      <div className="grouped fields">
        <label>
          Alternatives:
          {question.type === TRUE_OR_FALSE
            ? question.alternatives.map((alternative) => (
                <div className="inline field" key={alternative.description}>
                  <label
                    data-testid={`question-form-alternative-${alternative.description}`}
                  >
                    <input
                      className="ui radio"
                      type="radio"
                      name="alternative"
                      value={alternative.description}
                      data-testid={`question-form-alternative-${alternative.description}-input`}
                      checked={
                        alternative.description === question.correctAnswer
                      }
                      onChange={(event) => {
                        setQuestion({
                          ...question,
                          correctAnswer: event.target.value,
                        });
                      }}
                    />
                    {alternative.description}
                  </label>
                </div>
              ))
            : null}
        </label>
      </div>
      <div className="ui buttons">
        <button
          className="ui primary button"
          type="submit"
          data-testid="question-form-save-button"
        >
          Save
        </button>
        <button
          className="ui button"
          type="button"
          data-testid="cancel-button"
          onClick={onCancelClick}
        >
          Cancel
        </button>
      </div>
    </form>
  );
}
