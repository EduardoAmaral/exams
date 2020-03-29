import React from 'react';
import {mount} from 'enzyme';
import App from './App';

describe('App test', () => {

    it('should render the App component', () => {
        const wrapper = mount(<App/>);

        let text = wrapper.find("div").text();

        expect(text).toEqual('Hello Exams!');
    });

});