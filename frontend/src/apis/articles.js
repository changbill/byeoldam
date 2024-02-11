import axios from './axios';

export const getArticles = async (articleId) => {
  const { data } = await axios.get(`articles/${articleId}`);
  return { resultCode: data.resultCode, data: data.articles };
};

export const addArticle = async ({ nickname, articleName, tagName, disclosure, image }) => {
  const formData = new FormData();

  formData.append(
    'requests',
    JSON.stringify({
      nickname,
      articleName,
      tagName,
      disclosure,
    }),
  );

  formData.append('image', image);

  const { data } = await axios.post('articles', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return { resultCode: data.resultCode };
};

export const deleteArticle = async (articleId) => {
  const { data } = await axios.delete(`articles/${articleId}`);
  return { resultCode: data.resultCode };
};

export const editArticle = async ({ articleId, title, disclosure, tag, description }) => {
  const { data } = await axios.put(`articles/${articleId}`, {
    title,
    disclosure,
    tag,
    description,
  });

  return { resultCode: data.resultCode };
};

// feed 받아오는 axios
// async 안에 인자에 뭐가 들어가야 할지 잘 모르겠음
export const getFeeds = async () => {
  const { data } = await axios.get(`articles/follow`);
  //return pageable
  //jpa pagination axios 검색
}