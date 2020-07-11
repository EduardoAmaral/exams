import React from 'react';
import { render } from '@testing-library/react';
import Comment from '../comment';

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
      creationDate: '2011-12-03T10:15:30+01:00',
    };
    const { getByTestId } = render(<Comment comment={comment} />);

    expect(getByTestId('comment-1-creation-date')).toHaveTextContent(
      '12/3/2011'
    );
  });
});
