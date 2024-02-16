import { useNavigate } from 'react-router-dom';

export default function UserView({ data }) {
  const navigate = useNavigate();
  const ImgUrl = data.imageUrl != '' ? data.imageUrl : 'default_profile_image.png';

  return (
    <div
      className="user-view-container"
      onClick={() => {
        navigate(`universe/${data.nickname}`);
      }}
    >
      <div className="user-view">
        <div className="user-view-img-container">
          <img className="user-view-img" src={ImgUrl} alt="사용자의 프로필 사진" />
        </div>
        <p className="user-view-name">{data.nickname}</p>
      </div>
    </div>
  );
}
