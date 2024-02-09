import { Link } from 'react-router-dom';
import ExtendedBarIcon from './base/ExtendedBarIcon';
import { useState } from 'react';

export default function ExtendedBar({ changeBar, barId, changeExNav, exNav, goHome }) {
  const [isClose, setIsClosed] = useState(false);

  const closeExtendedBar = () => {
    setIsClosed(true);

    setTimeout(() => {
      changeBar(0);
      changeExNav(0);
    }, 400);
  };
  const openSearchBar = () => {
    changeBar(1);
    changeExNav(1);
  };
  const openNotificationBar = () => {
    changeBar(3);
    changeExNav(1);
  };
  const openSettingsPage = () => {
    changeBar(4);
    changeExNav(1);
  };

  return (
    <div className={isClose ? 'extended-bar-closed' : 'extended-bar-big-container'}>
      <div className="nav-bar-logo" onClick={goHome}>
        <img src="/src/assets/images/temporary-logo.png" alt="logo" />
      </div>
      <div className={isClose ? 'extended-bar-container-closed' : 'extended-bar-container'}>
        <div className="extended-bar-contents">
          {barId === 1 ? (
            <input type="text" placeholder="검색" className="search-bar-input" />
          ) : null}

          <div className="search-bar-bottom-contents">
            <div className="extended-bar-side-menu-box">
              <div
                className={
                  barId === 1 ? 'search-bar-filter-box' : 'search-bar-filter-box opacity-0'
                }
              >
                <ExtendedBarIcon
                  src={'/src/assets/images/search-bar-filter-icons/user.png'}
                  alt={'user'}
                />
                <ExtendedBarIcon
                  src={'/src/assets/images/search-bar-filter-icons/star.png'}
                  alt={'star'}
                />
                <ExtendedBarIcon
                  src={'/src/assets/images/search-bar-filter-icons/constellation.png'}
                  alt={'constellation'}
                />
              </div>

              <div className="extended-bar-menu-box">
                <ExtendedBarIcon
                  src={
                    barId === 1
                      ? '/src/assets/images/nav-bar-menu-icons/search_activated.png'
                      : '/src/assets/images/nav-bar-menu-icons/search.png'
                  }
                  alt={'search'}
                  onClick={openSearchBar}
                />
                <ExtendedBarIcon
                  src={'/src/assets/images/nav-bar-menu-icons/feed.png'}
                  alt={'feed'}
                  className={'extended-bar-menu-icon'}
                />
                <ExtendedBarIcon
                  src={
                    barId === 3
                      ? '/src/assets/images/nav-bar-menu-icons/notifications_activated.png'
                      : '/src/assets/images/nav-bar-menu-icons/notifications.png'
                  }
                  alt={'notifications'}
                  onClick={openNotificationBar}
                />
                <Link to={'/settings'} onClick={openSettingsPage}>
                  <ExtendedBarIcon
                    src={
                      barId === 4
                        ? '/src/assets/images/nav-bar-menu-icons/settings_activated.png'
                        : '/src/assets/images/nav-bar-menu-icons/settings.png'
                    }
                    alt={'notifications'}
                  />
                </Link>
                <ExtendedBarIcon
                  src={'/src/assets/images/nav-bar-menu-icons/logout.png'}
                  alt={'logout'}
                />
              </div>
            </div>

            <div className="extended-bar-main-contents-box">
              <p className="title nickname">texts</p>
            </div>
          </div>
        </div>
      </div>
      <div className="nav-bar-toggle-container" onClick={closeExtendedBar}>
        <div className="nav-bar-menu-icon-box">
          <img
            src="/src/assets/images/nav-bar-toggle-button/close.png"
            alt="toggle_button"
            className="toggle-button"
          />
        </div>
      </div>
    </div>
  );
}
