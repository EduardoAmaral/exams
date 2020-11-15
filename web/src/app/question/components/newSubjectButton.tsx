import Axios from 'axios';
import React, { useState } from 'react';
import { SUBJECT } from '../../config/endpoint';
import Subject from '../../types/Subject';
import style from './newSubjectButton.module.scss';

interface Props {
  readonly onSave: (subject: Subject) => void;
}

const NewSubjectButton = ({ onSave }: Props): JSX.Element => {
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

  const renderInput = () => {
    return (
      <div className={style.inputContainer}>
        <label htmlFor="new-subject">Subject</label>
        <input
          id="new-subject"
          type="text"
          placeholder="Add new subject"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
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
    <span className={`${style.container} ${visible ? style.inputSpace : ''}`}>
      <button
        type="button"
        className="icon"
        onClick={openModal}
        title="Add new subject"
      >
        <i className="ri-add-circle-line" />
      </button>
      {visible ? renderInput() : null}
    </span>
  );
};

export default NewSubjectButton;
