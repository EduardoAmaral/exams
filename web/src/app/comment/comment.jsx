import React from 'react';

export default function Comment({ comment }) {
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
