import {
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from '@testing-library/react';
import Axios from 'axios';
import React from 'react';
import { SUBJECT } from '../../../config/endpoint';
import NewSubjectButton from '../newSubjectButton';

jest.mock('axios');
const axiosMocked = Axios as jest.Mocked<typeof Axios>;

describe('<NewSubjectButton />', () => {
  it('should show subject modal once clicked on the add subject button', () => {
    const { getByRole, queryByText } = render(
      <NewSubjectButton onSave={jest.fn()} />
    );

    expect(queryByText('Subject')).toBeNull();

    fireEvent.click(
      getByRole('button', {
        name: 'Add new subject',
      })
    );

    expect(queryByText('Subject')).toBeVisible();
  });

  beforeEach(() => {
    axiosMocked.post.mockResolvedValueOnce({
      status: 201,
    });
  });

  it('should save a new subject once click on the save button', async () => {
    const { getByLabelText, getByRole, queryByText } = render(
      <NewSubjectButton onSave={jest.fn()} />
    );

    fireEvent.click(
      getByRole('button', {
        name: 'Add new subject',
      })
    );

    fireEvent.change(getByLabelText('Subject', { selector: 'input' }), {
      target: { value: 'Games' },
    });

    fireEvent.click(
      getByRole('button', {
        name: 'Save',
      })
    );

    expect(Axios.post).toHaveBeenCalledWith(SUBJECT, {
      description: 'Games',
    });

    await waitForElementToBeRemoved(() => queryByText('Subject'));
  });

  it('should close the modal once click on cancel button', () => {
    const { queryByText, getByRole } = render(
      <NewSubjectButton onSave={jest.fn()} />
    );

    fireEvent.click(
      getByRole('button', {
        name: 'Add new subject',
      })
    );

    fireEvent.click(
      getByRole('button', {
        name: 'Cancel',
      })
    );

    expect(queryByText('Subject')).toBeNull();
  });
});
