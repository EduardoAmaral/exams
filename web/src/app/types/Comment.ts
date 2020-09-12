export default interface Comment {
  id: number;
  authorId: string;
  authorName?: string;
  message: string;
  creationDate: any;
}
