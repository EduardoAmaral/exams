import { render } from '@testing-library/react';
import React from 'react';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import App from './App';
import rootReducer from './app/store/modules/rootReducer';

describe('<App />', () => {
  it('should render Login when not authenticated', () => {
    const { getByTestId } = render(
      <Provider store={createStore(rootReducer, applyMiddleware(thunk))}>
        <App />
      </Provider>
    );

    expect(getByTestId('login')).toBeDefined();
  });

  it('should render App when Authenticated', () => {
    const store = createStore(
      () => ({ user: { id: '1' } }),
      applyMiddleware(thunk)
    );

    const { getByTestId } = render(
      <Provider store={store}>
        <App />
      </Provider>
    );

    expect(getByTestId('app')).toBeDefined();
  });
});
