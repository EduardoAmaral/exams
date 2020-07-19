import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import Comments, { Comment } from '../comments';

describe('<Comments />', () => {
  it('should render a input to write comments', () => {
    const { getByTestId } = render(<Comments onSend={() => {}} />);

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
        creationDate: '',
      },
      {
        id: 2,
        message: 'Comment 2',
        author: '7777',
        creationDate: '',
      },
    ];

    const { container, getByTestId } = render(
      <Comments onSend={() => {}} comments={comments} />
    );

    expect(container.querySelectorAll('.comment')).toHaveLength(2);
    expect(getByTestId('comment-1-author')).toHaveTextContent('1234');
    expect(getByTestId('comment-1-message')).toHaveTextContent('Comment 1');
    expect(getByTestId('comment-2-author')).toHaveTextContent('7777');
    expect(getByTestId('comment-2-message')).toHaveTextContent('Comment 2');
  });

  it('should clean input once submit comment', () => {
    const { getByTestId } = render(<Comments onSend={() => {}} />);

    fireEvent.change(getByTestId('comment-input'), {
      target: { value: 'My Comment' },
    });

    fireEvent.click(getByTestId('send-comment-button'));

    expect(getByTestId('comment-input')).toBeEmptyDOMElement();
  });

  describe('<Comments />', () => {
    it('should render a comment', () => {
      const comment = {
        id: 1,
        message: 'Message',
        author: 'Author',
        creationDate: '2011-12-03T10:15:30+01:00',
      };
      const { getByTestId } = render(<Comment comment={comment} />);

      expect(getByTestId('comment-1-message')).toHaveTextContent('Message');
      expect(getByTestId('comment-1-author')).toHaveTextContent('Author');
      expect(getByTestId('comment-1-creation-date')).toHaveTextContent(
        '12/3/2011'
      );
    });

    it('should render creationDate as TIME when date it is today', () => {
      const date = new Date();
      const comment = {
        id: 1,
        message: 'Message',
        author: 'Author',
        creationDate: date.toISOString(),
      };
      const { getByTestId } = render(<Comment comment={comment} />);

      expect(getByTestId('comment-1-creation-date')).toHaveTextContent(
        date.toLocaleTimeString()
      );
    });

    it('should render creationDate as YESTERDAY when it is yesterday', () => {
      const date = new Date();
      date.setDate(date.getDate() - 1);
      const comment = {
        id: 1,
        message: '',
        author: '',
        creationDate: date.toISOString(),
      };
      const { getByTestId } = render(<Comment comment={comment} />);

      expect(getByTestId('comment-1-creation-date')).toHaveTextContent(
        'Yesterday'
      );
    });

    it('should render creationDate as Day, Month, Year when it is before yesterday', () => {
      const comment = {
        id: 1,
        message: '',
        author: '',
        creationDate: '2011-12-03T10:15:30+01:00',
      };
      const { getByTestId } = render(<Comment comment={comment} />);

      expect(getByTestId('comment-1-creation-date')).toHaveTextContent(
        '12/3/2011'
      );
    });
  });
});
