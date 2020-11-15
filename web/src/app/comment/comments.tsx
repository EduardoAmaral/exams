import React, { useState } from 'react';
import CommentModel from '../types/Comment';
import style from './comments.module.scss';

interface Props {
  comments?: CommentModel[];
  onSend: (comment: Partial<CommentModel>) => void;
}

export default function Comments({
  comments = [],
  onSend,
}: Props): JSX.Element {
  const [newComment, setNewComment] = useState<Partial<CommentModel>>({});

  const sendComment = () => {
    onSend(newComment);
    setNewComment({ message: '' });
  };

  return (
    <div className={style.section} data-testid="comments-section">
      <div>
        <label htmlFor="comment-input">
          <i className="ri-chat-1-line" />
          Comments
        </label>
        <div className={style.input}>
          <textarea
            id="comment-input"
            value={newComment.message}
            data-testid="comment-input"
            placeholder="Write your comment..."
            rows={4}
            onChange={(e) =>
              setNewComment({ ...newComment, message: e.target.value })
            }
          />
          <button
            type="button"
            className="icon"
            data-testid="send-comment-button"
            onClick={sendComment}
          >
            <i className="ri-send-plane-2-line" />
          </button>
        </div>
      </div>
      <div className="comments-container">
        {comments.map((comment) => (
          <Comment key={comment.id} comment={comment} />
        ))}
      </div>
    </div>
  );
}

interface CommentProps {
  comment: CommentModel;
}

export function Comment({ comment }: CommentProps): JSX.Element {
  const printDate = () => {
    const creationDate = new Date(comment.creationDate);
    const currentDate = new Date();

    if (currentDate.toDateString() === creationDate.toDateString()) {
      return creationDate.toLocaleTimeString();
    }

    currentDate.setDate(currentDate.getDate() - 1);
    if (currentDate.toDateString() === creationDate.toDateString()) {
      return 'Yesterday';
    }

    return creationDate.toLocaleDateString(navigator.language, {
      day: 'numeric',
      month: 'short',
      year: 'numeric',
    });
  };

  return (
    <div className={style.comment} data-testid={`comment-${comment.id}`}>
      <span
        className={style.author}
        data-testid={`comment-${comment.id}-author`}
      >
        {comment.authorName}
      </span>
      <div
        className={style.message}
        data-testid={`comment-${comment.id}-message`}
      >
        {comment.message}
      </div>
      <span
        className={style.date}
        data-testid={`comment-${comment.id}-creation-date`}
      >
        {printDate()}
      </span>
    </div>
  );
}
