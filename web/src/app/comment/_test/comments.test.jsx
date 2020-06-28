import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import Comments from '../comments';

describe('<Comments />', () => {
  it('should render a input to write comments', () => {
    const { getByTestId } = render(<Comments />);

    expect(getByTestId('comment-input')).toBeDefined();
    expect(getByTestId('send-comment-button')).toBeDefined();
  });

  it('should create a comment once clicked on the send button', () => {
    const onSend = jest.fn();

    const { getByTestId } = render(<Comments onSend={onSend} />);

    fireEvent.change(getByTestId('comment-input'), {
      target: { value: 'My Comment' },
    });

    fireEvent.click(getByTestId('send-comment-button'));

    expect(onSend).toHaveBeenCalledWith({ message: 'My Comment' });
  });

  it('should render comments when they exist', () => {
    const comments = [
      {
        id: 1,
        message: 'Comment 1',
        author: '1234',
      },
      {
        id: 2,
        message: 'Comment 2',
        author: '7777',
      },
    ];

    const { container, getByTestId } = render(<Comments comments={comments} />);

    expect(container.querySelectorAll('.comment')).toHaveLength(2);
    expect(getByTestId('comment-1-author')).toHaveTextContent('1234');
    expect(getByTestId('comment-1-message')).toHaveTextContent('Comment 1');
    expect(getByTestId('comment-2-author')).toHaveTextContent('7777');
    expect(getByTestId('comment-2-message')).toHaveTextContent('Comment 2');
  });

  it('should clean input once submit comment', () => {
    const { getByTestId } = render(<Comments />);

    fireEvent.change(getByTestId('comment-input'), {
      target: { value: 'My Comment' },
    });

    fireEvent.click(getByTestId('send-comment-button'));

    expect(getByTestId('comment-input')).toBeEmptyDOMElement();
  });
});
