import Question from './Question';

export default interface Exam {
  id?: number;
  title: string;
  startDateTime: string;
  endDateTime: string;
  mockTest: boolean;
  questions?: Question[];
}
