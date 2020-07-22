export default interface Question {
  id: number;
  statement: string;
  type: string;
  subject: any;
  solution?: string;
  topic?: string;
  sharable?: boolean;
  alternatives?: any;
  correctAnswer?: string;
}
