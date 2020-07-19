export default interface Question {
  id: number;
  statement: string;
  correctAnswer: string;
  solution: string;
  topic: string;
  type: string;
  subject: any;
  active: boolean;
  sharable: boolean;
  alternatives: any;
}
