import React, { useState } from 'react';
import CommentModel from '../types/Comment';
import './comments.scss';

interface Props {
  comments?: Array<CommentModel>;
  onSend: (comment: any) => void;
}

export default function Comments({ comments = [], onSend} : Props) {
  const [newComment, setNewComment] = useState<Partial<CommentModel>>({});

  const sendComment = () => {
    onSend(newComment);
    setNewComment({ message: '' });
  };

  return (
    <div className="comments-section" data-testid="comments-section">
      <div className="row">
        <div className="comment-input-container">
          <label htmlFor="comment-input">
            <i className="ri-chat-1-line" />
            Comments
          </label>
          <div className="comment-input">
            <textarea
              id="comment-input"
              value={newComment.message}
              data-testid="comment-input"
              placeholder="Write your comment..."
              rows={2}
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
      </div>
      <div className="comments-container">
        {comments.map((comment) => (
          <Comment comment={comment} />
        ))}
      </div>
    </div>
  );
}

interface CommentProps {
  comment: CommentModel;
}

export function Comment({ comment }: CommentProps) {
  const printDate = () => {
    const date = new Date(comment.creationDate);
    const today = new Date();

    if (today.toDateString() === date.toDateString()) {
      return date.toLocaleTimeString();
    }

    today.setDate(today.getDate() - 1);
    if (today.toDateString() === date.toDateString()) {
      return 'Yesterday';
    }

    return date.toLocaleDateString();
  };

  return (
    <div
      key={comment.id}
      className="comment"
      data-testid={`comment-${comment.id}`}
    >
      <span className="author" data-testid={`comment-${comment.id}-author`}>
        {comment.author}
      </span>
      <div className="message" data-testid={`comment-${comment.id}-message`}>
        {comment.message}
      </div>
      <span
        className="date"
        data-testid={`comment-${comment.id}-creation-date`}
      >
        {printDate()}
      </span>
    </div>
  );
}
