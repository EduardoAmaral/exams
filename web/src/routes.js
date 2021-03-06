import React from 'react';
import { Route, Switch } from 'react-router';
import QuestionPage from './app/question/questionPage';
import QuestionCreatePage from './app/question/questionCreatePage';
import QuestionEditPage from './app/question/questionEditPage';
import QuestionDetailPage from './app/question/questionDetailPage';
import ExamPage from './app/exam/examPage';
import CreateExamPage from './app/exam/createExamPage';

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
export default function Routes() {
  return (
    <Switch>
      <Route path="/" exact component={QuestionPage} />
      <Route path="/question" exact component={QuestionPage} />
      <Route path="/question/create" component={QuestionCreatePage} />
      <Route path="/question/edit/:id" component={QuestionEditPage} />
      <Route path="/question/detail/:id" component={QuestionDetailPage} />
      <Route path="/exam" exact component={ExamPage} />
      <Route path="/exam/create" component={CreateExamPage} />
    </Switch>
  );
}
