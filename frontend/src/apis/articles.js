import axios from './axios';

export const getArticles = async (articleId) => {
  const { data } = await axios.get(`articles/${articleId}`);
  return data;
};
export const addArticle = async (
  { description, articleHashtagSet, disclosureType, imageType },
  file,
) => {
  const formData = new FormData();

  const request = new Blob(
    [
      JSON.stringify({
        description,
        articleHashtagSet,
        disclosureType,
        imageType,
        title: description,
      }),
    ],
    { type: 'application/json' },
  );

  formData.append('request', request);
  formData.append('imageFile', file);

  const result = await axios.post('articles/no-constellation', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return result;
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
  // console.log("axios에서 getFeeds의 data: ", data);
  return { resultCode : data.resultCode, data : data.result.content }
}

export const getProfileImage = async (nickName) => {
  const { data } = await axios.get(`${nickName}/request-profile`);
  return { resultCode : data.resultCode, data : data.result }
}

export const getLikeCount = async (articleId) => {
  const { data } = await axios.get(`articles/${articleId}/likeCount`);
  return { resultCode : data.resultCode, data : data.result } 
}

export const postLike = async (articleId) => {
  const data = await axios.post(`articles/${articleId}/likes`);
  return data;
}

export const getLike = async (articleId) => {
  const {data} = await axios.get(`articles/${articleId}/likes`);
  return { resultCode : data.resultCode, data : data.result }
}