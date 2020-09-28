import React, { useEffect, useState } from 'react';
import Axios from 'axios';
import { EXAM } from '../config/endpoint';
import Loading from '../loading/loading';
import Exam from '../types/Exam';
import { formatDateTime } from '../util/dateUtil';
import history from '../config/history';

export default function ExamPage() {
  const [isLoading, setLoading] = useState<boolean>(false);
  const [exams, setExams] = useState<Exam[]>([]);

  useEffect(() => {
    setLoading(true);
    Axios.get(EXAM)
      .then((response) => {
        setLoading(false);
        setExams(response.data);
      })
      .catch(() => {
        setLoading(false);
      });
  }, []);

  return (
    <>
      <Loading isLoading={isLoading} />
      <section>
        <h2>My exams</h2>
        <table className="table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Starts at</th>
              <th>Ends at</th>
            </tr>
          </thead>
          <tbody>
            {exams.map((exam) => (
              <tr key={exam.id}>
                <td>{exam.title}</td>
                <td>{formatDateTime(exam.startDateTime)}</td>
                <td>{formatDateTime(exam.endDateTime)}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <div className="right">
          <button
            className="positive ui button"
            type="button"
            onClick={() => history.push('/exam/create')}
          >
            Create Exam
          </button>
        </div>
      </section>
    </>
  );
}
