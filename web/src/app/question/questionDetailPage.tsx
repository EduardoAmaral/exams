import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router';
import lodash from 'lodash';
import { QUESTION_BY_ID, QUESTION_COMMENT } from '../config/endpoint';
import Loading from '../loading/loading';
import history from '../config/history';
import Comments from '../comment/comments';
import { questionCommentsSubscription } from '../config/socket';
import Question from '../types/Question';
import Comment from '../types/Comment';

interface CommentMessage {
  readonly type: string;
  readonly data: any;
}

export default function QuestionDetailPage() {
  const [isLoading, setLoading] = useState(false);
  const [question, setQuestion] = useState<Partial<Question>>({});
  const [comments, setComments] = useState<Comment[]>([]);

  const { id } = useParams();

  const onCommentReceive = (message: CommentMessage) => {
    switch (message.type) {
      case 'FETCH_ALL_COMMENTS':
        setComments(message.data);
        break;
      case 'NEW_COMMENT':
        setComments((c) => lodash.uniqBy([message.data, ...c], 'id'));
        break;
      default:
        break;
    }
  };

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

    const commentsSubscription = questionCommentsSubscription(
      id,
      onCommentReceive
    );

    return () => {
      if (commentsSubscription) commentsSubscription.unsubscribe();
    };
  }, [id]);

  const onCancelClick = () => {
    history.goBack();
  };

  const onSendComment = (comment: Comment) => {
    axios
      .post(QUESTION_COMMENT, { ...comment, questionId: question.id })
      .then((response) => {
        setComments((c) => [response.data, ...c]);
      });
  };

  return (
    <>
      <Loading isLoading={isLoading} />
      <div data-testid="question-detail-page">
        <h2>Question {id}</h2>
        <div className="field">
          <span data-testid="question-detail-statement-title">Statement:</span>{' '}
          <span data-testid="question-detail-statement-value">
            {question.statement}
          </span>
        </div>
        <div className="field">
          <span data-testid="question-detail-type-title">Type:</span>{' '}
          <span data-testid="question-detail-type-value">{question?.type}</span>
        </div>
        <div className="field">
          <span data-testid="question-detail-subject-title">Subject:</span>{' '}
          <span data-testid="question-detail-subject-value">
            {question.subject?.description}
          </span>
        </div>
        <div className="field">
          <span data-testid="question-detail-solution-title">Solution:</span>{' '}
          <span data-testid="question-detail-solution-value">
            {question.solution}
          </span>
        </div>
        <div className="field">
          <span data-testid="question-detail-topic-title">Topics:</span>{' '}
          <span data-testid="question-detail-topic-value">
            {question.topic}
          </span>
        </div>
        <Comments comments={comments} onSend={onSendComment} />
        <div>
          <button
            className="ui button"
            type="button"
            data-testid="cancel-button"
            onClick={onCancelClick}
          >
            Cancel
          </button>
        </div>
      </div>
    </>
  );
}
