import { useState } from 'react';
import ExtendedBar from './ExtendedBar';
import ProfileInfo from './base/ProfileInfo';
import NavBarMenu from './base/NavBarMenu';
import { Link } from 'react-router-dom';

export default function NavBar() {
  const [isNavBarVisible, setIsNavBarVisible] = useState(true);
  const [barId, setBarId] = useState(0);

  const closeNavBar = () => {
    setIsNavBarVisible(!isNavBarVisible);
  };
  const openSearchBar = () => {
    setBarId(1);
  };
  const openNotificationBar = () => {
    setBarId(3);
  };
  const changeBar = (newId) => {
    setBarId(newId);
  };

  const openSettings = () => {
    setBarId(4);
  };
  console.log('barId : ', barId);

  return (
    <div>
      {barId ? (
        <ExtendedBar changeBar={changeBar} barId={barId} />
      ) : (
        <div className={`nav-bar-big-container ${isNavBarVisible ? '' : 'nav-bar-hidden'}`}>
          <div className="nav-bar-container">
            <Link to={'/home'}>
              <div className="nav-bar-logo">
                <img src="/src/assets/images/temporary-logo.png" alt="logo" />
              </div>
            </Link>

            <div className="nav-bar-contents">
              <div className="nav-bar-profile-box">
                <div className="profile-image-box">
                  <div className="profile-image-container">
                    <img
                      className="profile-image"
                      src="/src/assets/images/space.png"
                      alt="profile_image"
                    />
                  </div>
                </div>
                <div className="nickname title">
                  <p>상당히느긋한사람</p>
                </div>
                <div className="profile-info-box">
                  <ProfileInfo text={'별'} num={26} />
                  <ProfileInfo text={'별자리'} num={4} />
                  <ProfileInfo text={'팔로워'} num={144} />
                  <ProfileInfo text={'팔로잉'} num={132} />
                </div>
              </div>

              <div className="nav-bar-divide-line-box">
                <hr className="nav-bar-divide-line" />
              </div>

              <div className="nav-bar-menu-box">
                <NavBarMenu
                  text={'검색'}
                  src={'/src/assets/images/nav-bar-menu-icons/search.png'}
                  alt={'search'}
                  onClick={openSearchBar}
                />
                <NavBarMenu
                  text={'피드'}
                  src={'/src/assets/images/nav-bar-menu-icons/feed.png'}
                  alt={'feed'}
                />
                <NavBarMenu
                  text={'알림'}
                  src={'/src/assets/images/nav-bar-menu-icons/notifications.png'}
                  alt={'notifications'}
                  onClick={openNotificationBar}
                />
                <Link to={'/settings'} className="link-setting">
                  <NavBarMenu
                    text={'설정'}
                    src={'/src/assets/images/nav-bar-menu-icons/settings.png'}
                    alt={'settings'}
                    onClick={openSettings}
                  />
                </Link>
                <NavBarMenu
                  text={'로그아웃'}
                  src={'/src/assets/images/nav-bar-menu-icons/logout.png'}
                  alt={'logout'}
                />
              </div>
            </div>

            <div className="nav-bar-toggle-container" onClick={closeNavBar}>
              <div className="nav-bar-menu-icon-box">
                <img
                  src={
                    isNavBarVisible
                      ? '/src/assets/images/nav-bar-toggle-button/close.png'
                      : '/src/assets/images/nav-bar-toggle-button/open.png'
                  }
                  alt="toggle_button"
                  className="toggle-button"
                />
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
