import Subject from './Subject';
import Alternative from './Alternative';

export default interface Question {
  id?: number;
  statement: string;
  type: string;
  subject: Subject;
  solution?: string;
  keywords?: string;
  sharable?: boolean;
  alternatives?: Alternative[];
  correctAnswer?: string;
  authorName?: string;
  selected?: boolean;
}

export interface QuestionErrors {
  statement?: string;
  type?: string;
  subject?: string;
  solution?: string;
  keywords?: string;
  alternatives?: string;
  correctAnswer?: string;
}
