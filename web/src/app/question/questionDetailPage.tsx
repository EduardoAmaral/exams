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
  readonly data: Comment[];
}

interface QueryParams {
  readonly id: string;
}

export default function QuestionDetailPage(): JSX.Element {
  const [isLoading, setLoading] = useState(false);
  const [question, setQuestion] = useState<Partial<Question>>({});
  const [comments, setComments] = useState<Comment[]>([]);

  const { id } = useParams<QueryParams>();

  const onCommentReceive = (message: CommentMessage) => {
    switch (message.type) {
      case 'FETCH_ALL_COMMENTS':
        setComments(message.data);
        break;
      case 'NEW_COMMENT':
        setComments((c) => lodash.uniqBy([message.data, ...c], 'id'));
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

  const onSendComment = (comment: Partial<Comment>) => {
    axios
      .post(QUESTION_COMMENT, { ...comment, questionId: question.id })
      .then((response) => {
        setComments((c) => [response.data, ...c]);
      });
  };

  return (
    <>
      <Loading isLoading={isLoading} />
      <section>
        <h2>Question</h2>
        <div className="field">
          <span>
            Statement: <span>{question?.statement}</span>
          </span>
        </div>
        <div className="field">
          <span>
            Type: <span>{question?.type}</span>
          </span>
        </div>
        <div className="field">
          <span>
            Subject: <span>{question.subject?.description}</span>
          </span>
        </div>
        <div className="field">
          <span>
            Solution: <span>{question?.solution}</span>
          </span>
        </div>
        <div className="field">
          <span>
            Keywords: <span>{question?.keywords}</span>
          </span>
        </div>
        <Comments comments={comments} onSend={onSendComment} />
        <div>
          <button className="ui button" type="button" onClick={onCancelClick}>
            Cancel
          </button>
        </div>
      </section>
    </>
  );
}
