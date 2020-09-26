import React from 'react';
import style from './loading.module.scss';

interface Props {
  isLoading: boolean;
}

export default function Loading({ isLoading }: Props) {
  if (isLoading) {
    return (
      <div className={style.container}>
        <div className={style.loading} data-testid="loading">
          <div></div>
          <div></div>
          <div></div>
          <div></div>
        </div>
      </div>
    );
  }

  return <></>;
}
