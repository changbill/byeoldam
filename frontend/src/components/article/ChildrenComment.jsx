import React from 'react';

export default function ChildrenComment({ id, parentId, nickName, content, createAt }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'row' }}>
      <div>{id}</div>
      <div>{parentId}</div>
      <div>{nickName}</div>
      <div>{content}</div>
      <div>{createAt}</div>
      <button>수정</button>
      <button>삭제</button>
    </div>
  );
}
