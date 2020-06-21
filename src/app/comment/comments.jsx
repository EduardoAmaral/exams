import React, { useState } from 'react';
import './comments.scss';

export default function Comments({ comments = [], onSend = () => {} }) {
  const [newComment, setComment] = useState({});

  const printComment = (comment) => {
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
      </div>
    );
  };

  const sendComment = () => {
    onSend(newComment);
    setComment({});
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
              row="1"
              onChange={(e) =>
                setComment({ ...newComment, message: e.target.value })
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
        {comments.map((comment) => printComment(comment))}
      </div>
    </div>
  );
}
