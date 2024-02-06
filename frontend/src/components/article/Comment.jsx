import React from 'react';

export default function Comment({ id, nickName, content, createAt, parentId, childrenComments }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <div>
        <div>{id}</div>
        <div>{nickName}</div>
        <div>{content}</div>
        <div>{createAt}</div>
        <button>수정</button>
        <button>삭제</button>
      </div>
      <div>
        {childrenComments.map((comment) => (
          <ChildrenComment
            id={comment.id}
            nickName={comment.nickName}
            content={comment.content}
            createAt={comment.createAt}
            parentId={id}
          />
        ))}
      </div>
    </div>
  );
}
