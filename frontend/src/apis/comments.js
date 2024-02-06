import axios from './axios';

export const getComments = async (articleId) => {
  const { data } = await axios.get(`/articles/${articleId}/comments`);

  return { resultCode: data.resultCode, data: data.result.content };
};

export const addComments = async ({ parentCommentId, articleId, content }) => {
  const { data } = axios.post(`comments/articles/${articleId}`, {
    parentCommentId,
    content,
  });

  return { resultCode: data.resultCode };
};

export const editComments = async ({ articleId, commentId, content }) => {
  const { data } = axios.put(`comments/${commentId}`, {
    articleId,
    content,
  });

  return { resultCode: data.resultCode };
};

export const deleteComments = async ({ commentId }) => {
  const { data } = axios.delete(`comments/${commentId}`);

  return { resultCode: data.resultCode };
};
