import Axios from 'axios';
import React, { useState } from 'react';
import { SUBJECT } from '../../config/endpoint';
import Subject from '../../types/Subject';

interface Props {
  readonly onSave: (subject: Subject) => void;
}

const SubjectButton = ({ onSave }: Props) => {
  const [visible, setVisible] = useState<boolean>(false);
  const [description, setDescription] = useState<string>('');

  const openModal = () => {
    setDescription('');
    setVisible(true);
  };

  const save = (event: React.MouseEvent) => {
    event.preventDefault();

    Axios.post(SUBJECT, {
      description: description,
    }).then((response) => {
      onSave(response.data);
      setVisible(false);
    });
  };

  const renderModal = () => {
    return (
      <div>
        <label>
          Subject
          <input
            type="text"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </label>
        <button type="button" onClick={() => setVisible(false)}>
          Cancel
        </button>
        <button
          type="button"
          className="primary"
          onClick={save}
          data-testid="subject-save-button"
        >
          Save
        </button>
      </div>
    );
  };

  return (
    <div className="subject-button-container">
      <button
        type="button"
        className="icon"
        onClick={openModal}
        title="Add new subject"
      >
        <i className="ri-add-circle-line" />
      </button>
      {visible ? renderModal() : null}
    </div>
  );
};

export default SubjectButton;
