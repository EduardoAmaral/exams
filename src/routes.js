import React from 'react';
import { Route, Switch } from 'react-router';
import QuestionPage from './app/question/questionPage';
import QuestionCreatePage from './app/question/questionCreatePage';
import QuestionEditPage from './app/question/questionEditPage';
import QuestionDetailPage from './app/question/questionDetailPage';

export default function Routes() {
  return (
    <Switch>
      <Route path="/" component={QuestionPage} exact />
      <Route path="/question/create" component={QuestionCreatePage} />
      <Route path="/question/edit/:id" component={QuestionEditPage} />
      <Route path="/question/detail/:id" component={QuestionDetailPage} />
    </Switch>
  );
}
