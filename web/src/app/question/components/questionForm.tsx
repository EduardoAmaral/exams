import Axios from 'axios';
import React, { FormEvent, useEffect, useState } from 'react';
import { SUBJECT } from '../../config/endpoint';
import history from '../../config/history';
import Question, { QuestionErrors } from '../../types/Question';
import Subject from '../../types/Subject';
import NewSubjectButton from './newSubjectButton';
import {
  TextField,
  InputLabel,
  Select,
  FormControl,
  FormHelperText,
} from '@material-ui/core';

import style from './questionForm.module.scss';

interface Props {
  questionData?: Question;
  errors?: QuestionErrors;
  onSubmit: (question: Partial<Question>) => void;
}

export default function QuestionForm({
  questionData,
  errors = {},
  onSubmit,
}: Props): JSX.Element {
  const [question, setQuestion] = useState<Partial<Question>>({
    keywords: '',
    solution: '',
  });
  const [subjects, setSubjects] = useState<Subject[]>([]);
  const [selectedSubject, setSelectedSubject] = useState<number>();
  const [loadingSubjects, setLoadingSubjects] = useState<boolean>();
  const [correctAnswerPosition, setCorrectAnswerPosition] = useState<number>();

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
      questionData.alternatives &&
        setCorrectAnswerPosition(
          questionData.alternatives.findIndex(
            (q) => q.description === questionData.correctAnswer
          ) + 1
        );
    }
  }, [questionData]);

  const onSubmitForm = (event: FormEvent) => {
    event.preventDefault();

    onSubmit({
      ...question,
      correctAnswer:
        correctAnswerPosition && question.alternatives
          ? question.alternatives[correctAnswerPosition - 1].description
          : '',
    });
  };

  const onCancelClick = () => {
    history.goBack();
  };

  const renderStatementInput = () => {
    return (
      !loadingSubjects && (
        <TextField
          label="Statement"
          id="statement"
          name="statement"
          variant="outlined"
          error={errors.statement ? true : false}
          helperText={errors.statement}
          type="text"
          value={question.statement}
          onChange={(event) => {
            setQuestion({ ...question, statement: event.target.value });
          }}
          multiline={true}
          rows={3}
          data-testid="question-statement"
        />
      )
    );
  };

  const selectType = (type: string) => {
    switch (type) {
      case TRUE_OR_FALSE:
        setQuestion({
          ...question,
          type: type,
          alternatives: trueOrFalseAlternatives,
        });
        break;
      case MULTIPLE_CHOICES:
        setQuestion({
          ...question,
          type: type,
          alternatives: multipleChoicesAlternatives,
        });
        break;
      default:
        setQuestion({
          ...question,
          type: type,
          alternatives: [],
        });
        break;
    }
  };

  const renderTypeSelect = () => {
    return (
      !loadingSubjects && (
        <FormControl
          variant="outlined"
          disabled={question.id !== undefined}
          error={errors.type ? true : false}
        >
          <InputLabel htmlFor="type">Type</InputLabel>
          <Select
            native
            id="type"
            name="type"
            label="Type"
            value={question.type}
            onChange={(event) => selectType(event.target.value as string)}
            data-testid="question-type"
            inputProps={{
              name: 'type',
              id: 'type',
            }}
          >
            <option aria-label="None" value="" />
            {types.map((type) => (
              <option key={type.value} value={type.value} label={type.value} />
            ))}
          </Select>
          <FormHelperText>{errors.type}</FormHelperText>
        </FormControl>
      )
    );
  };

  const selectSubject = (newSubject: number) => {
    setQuestion({
      ...question,
      subject: {
        id: newSubject,
      },
    });
    setSelectedSubject(newSubject);
  };

  const renderSubjectSelect = () => {
    return (
      !loadingSubjects && (
        <>
          <FormControl variant="outlined" error={errors.subject ? true : false}>
            <InputLabel htmlFor="subject">Subject</InputLabel>
            <Select
              native
              id="subject"
              name="subject"
              label="Subject"
              value={selectedSubject}
              onChange={(event) => {
                selectSubject(Number.parseInt(event.target.value as string));
              }}
              inputProps={{
                name: 'subject',
                id: 'subject',
              }}
            >
              <option aria-label="None" value="" />
              {subjects?.map((subject) => (
                <option
                  value={subject.id}
                  key={subject.id}
                  label={subject.description}
                />
              ))}
            </Select>
            <FormHelperText>{errors.subject}</FormHelperText>
          </FormControl>
          <NewSubjectButton
            onSave={(subject: Subject) => {
              setSubjects((s) => [...s, subject]);
              setSelectedSubject(subject.id);
            }}
          />
        </>
      )
    );
  };

  const renderSolutionInput = () => {
    return (
      <TextField
        label="Solution"
        id="solution"
        name="solution"
        variant="outlined"
        error={errors.solution ? true : false}
        helperText={errors.solution}
        type="text"
        value={question.solution}
        onChange={(event) => {
          setQuestion({ ...question, solution: event.target.value });
        }}
        multiline={true}
        rows={2}
      />
    );
  };

  const renderKeywordsInput = () => {
    return (
      <TextField
        label="Keywords"
        id="keywords"
        name="keywords"
        variant="outlined"
        error={errors.keywords ? true : false}
        helperText={errors.keywords}
        type="text"
        value={question.keywords}
        onChange={(event) => {
          setQuestion({ ...question, keywords: event.target.value });
        }}
      />
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
                  value={alternative.position}
                  data-testid={`question-form-alternative-${alternative.position}-radio`}
                  checked={alternative.position == correctAnswerPosition}
                  onChange={(event) => {
                    setCorrectAnswerPosition(
                      Number.parseInt(event.target.value as string)
                    );
                  }}
                />
                {question.type === TRUE_OR_FALSE ? (
                  alternative.description
                ) : (
                  <TextField
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
      <div className={style.container}>
        <div className={style.statement}>{renderStatementInput()}</div>
        <div className={style.type}>{renderTypeSelect()}</div>
        <div className={style.subject}>{renderSubjectSelect()}</div>
        <div className={style.solution}>{renderSolutionInput()}</div>
        <div className={style.keywords}>{renderKeywordsInput()}</div>
        <div className={style.alternatives}>{renderAlternatives()}</div>
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
