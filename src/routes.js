import React from 'react';
import { Route, Switch } from 'react-router';
import QuestionContainer from './app/question/questionContainer';

export default function Routes() {
  return (
    <Switch>
      <Route path="/" component={QuestionContainer} exact />
    </Switch>
  );
}
