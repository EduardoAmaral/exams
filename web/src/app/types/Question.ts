import Subject from './Subject';
import Alternative from './Alternative';

export default interface Question {
  id?: number;
  statement: string;
  type: string;
  subject: Subject;
  solution?: string;
  topic?: string;
  sharable?: boolean;
  alternatives?: Alternative[];
  correctAnswer?: string;
}
