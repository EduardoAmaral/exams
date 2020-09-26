import { formatDateTime } from '../dateUtil';

describe('DateUtil', () => {
  it('should return empty if param is null', () => {
    expect(formatDateTime(undefined)).toBe('');
  });

  it('should return date with short month and time when param is a valid datetime', () => {
    expect(formatDateTime('2020-09-27T17:22:30-00:00')).toBe(
      new Date('2020-09-27T17:22:30-00:00').toLocaleDateString(
        navigator.language,
        {
          day: 'numeric',
          month: 'short',
          year: 'numeric',
          hour: 'numeric',
          minute: 'numeric',
        }
      )
    );
  });
});
