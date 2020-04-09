import React from 'react';
import { Route, Switch } from 'react-router';
import QuestionPage from './app/question/questionPage';
import QuestionCreatePage from './app/question/questionCreatePage';
import QuestionEditPage from './app/question/questionEditPage';

export default function Routes() {
  return (
    <Switch>
      <Route path="/" component={QuestionPage} exact />
      <Route path="/question/create" component={QuestionCreatePage} />
      <Route path="/question/edit/:id" component={QuestionEditPage} />
    </Switch>
  );
}
