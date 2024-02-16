import axios from './axios';

export const checkFollow = async (nickname) => {
  const { data } = await axios.get(`/${nickname}/follow`);

  return data;
};

export const followNickname = async (nickname) => {
  const { data } = await axios.post(`/${nickname}/follow`);

  return data;
};
