import { useEffect, useState } from 'react';
import { getComments } from '../../../apis/comments';
import Comment from '../../article/Comment';

export default function ArticleModal({ articleId, description, hits, createdAt }) {
  const [comments, setComments] = useState();

  useEffect(() => {
    getComments(articleId).then(({ resultCode, result }) => {
      console.log(resultCode);
      setComments(result);
    });
  }, [articleId]);

  return (
    <div>
      <div>내용 : {description}</div>
      <div>조회수 : {hits}</div>
      <div>작성일 : {createdAt}</div>
      <div>
        {comments.map((comment) => (
          <Comment
            id={comment.id}
            nickName={comment.nickName}
            createAt={comment.createAt}
            parentId={comment.parentId}
            childrenComments={comment.childrenComments}
          />
        ))}
      </div>
    </div>
  );
}
