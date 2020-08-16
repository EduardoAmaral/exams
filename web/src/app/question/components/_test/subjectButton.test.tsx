import {
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import Axios from 'axios';
import React from 'react';
import { SUBJECT } from '../../../config/endpoint';
import SubjectButton from '../subjectButton';

jest.mock('axios');
const axiosMocked = Axios as jest.Mocked<typeof Axios>;

describe('<SubjectButton />', () => {
  it('should show subject modal once clicked on the add subject button', () => {
    const { getByTitle, queryByText } = render(
      <SubjectButton onSave={jest.fn()} />
    );

    expect(queryByText('Subject')).toBeNull();

    fireEvent.click(getByTitle('Add new subject'));

    expect(queryByText('Subject')).toBeVisible();
  });

  beforeEach(() => {
    axiosMocked.post.mockResolvedValueOnce({
      status: 201,
    });
  });

  it('should save a new subject once click on the save button', async () => {
    const { getByTitle, getByLabelText, queryByText } = render(
      <SubjectButton onSave={jest.fn()} />
    );

    fireEvent.click(getByTitle('Add new subject'));

    fireEvent.change(getByLabelText('Subject', { selector: 'input' }), {
      target: { value: 'Games' },
    });

    fireEvent.click(queryByText('Save'));

    expect(Axios.post).toHaveBeenCalledWith(SUBJECT, {
      description: 'Games',
    });

    await waitForElementToBeRemoved(() => queryByText('Subject'));
  });

  it('should close the modal once click on cancel button', () => {
    const { getByTitle, queryByText } = render(
      <SubjectButton onSave={jest.fn()} />
    );

    fireEvent.click(getByTitle('Add new subject'));

    fireEvent.click(queryByText('Cancel'));

    expect(queryByText('Subject')).toBeNull();
  });
});
