import React, { useState } from 'react';
import Comment from './comment';
import './comments.scss';

export default function Comments({ comments = [], onSend = () => {} }) {
  const [newComment, setNewComment] = useState({});

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
              row="1"
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
