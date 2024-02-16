import { Link, useNavigate } from 'react-router-dom';
import ExtendedBarIcon from './base/ExtendedBarIcon';
import { useEffect, useState, useRef } from 'react';
import axios from '../apis/axios';
import UserView from './search/UserView';

export default function ExtendedBar({ changeBar, barId, changeExNav, exNav, goHome }) {
  const [isClose, setIsClosed] = useState(false);
  const [filterId, setFilterId] = useState(0);
  const [inputValue, setInputValue] = useState('');
  const [users, setUsers] = useState([]);
  const inputRef = useRef();

  const search = (event) => {
    if (event.key === 'Enter') {
      setInputValue(event.target.value);
    }
  };

  const resetSearch = () => {
    inputRef.current.value = '';
    setInputValue('');
  };

  const changeFilter = (id) => {
    setFilterId(id);
  };

  const navigate = useNavigate();

  const closeExtendedBar = () => {
    setIsClosed(true);

    setTimeout(() => {
      changeBar(0);
      changeExNav(0);
      // navigate('/home');
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
  const openFeedPage = () => {
    changeBar(2);
    changeExNav(1);
  };

  const searchUser = async (keyword) => {
    const { data: userResponse } = await axios.get(`/search/user?keyword=${keyword}`);
    setUsers(userResponse.result);
  };

  useEffect(() => {
    if (inputValue.trim() !== '') {
      if (filterId === 0) {
        searchUser(inputValue);
      } else if (filterId === 1) {
        navigate('/search/star', { state: { keyword: inputValue.trim(), filterId: filterId } });
      } else if (filterId === 2) {
        navigate('/search/constellation', {
          state: { keyword: inputValue.trim(), filterId: filterId },
        });
      }
    }
  }, [inputValue, filterId]);

  return (
    <div className={isClose ? 'extended-bar-closed' : 'extended-bar-big-container'}>
      <div className="nav-bar-logo" onClick={goHome}>
        <img src="/images/temporary-logo.png" alt="logo" />
      </div>
      <div className={isClose ? 'extended-bar-container-closed' : 'extended-bar-container'}>
        <div className="extended-bar-contents">
          <div className="extended-bar-search">
            {barId === 1 ? (
              <input
                ref={inputRef}
                type="text"
                placeholder="검색"
                className="search-bar-input"
                onKeyUp={search}
              />
            ) : null}
            <img
              src="/images/base/close-button.png"
              alt="검색어 초기화 X 버튼"
              onClick={resetSearch}
            />
          </div>

          <div className="search-bar-bottom-contents">
            <div className="extended-bar-side-menu-box">
              <div
                className={
                  barId === 1 ? 'search-bar-filter-box' : 'search-bar-filter-box opacity-0'
                }
              >
                <ExtendedBarIcon
                  src={
                    filterId === 0
                      ? '/images/search-bar-filter-icons/user_activated.png'
                      : '/images/search-bar-filter-icons/user.png'
                  }
                  alt={'user'}
                  onClick={() => changeFilter(0)}
                />
                <ExtendedBarIcon
                  src={
                    filterId === 1
                      ? '/images/search-bar-filter-icons/star_activated.png'
                      : '/images/search-bar-filter-icons/star.png'
                  }
                  alt={'star'}
                  onClick={() => changeFilter(1)}
                />
                <ExtendedBarIcon
                  src={
                    filterId === 2
                      ? '/images/search-bar-filter-icons/constellation_activated.png'
                      : '/images/search-bar-filter-icons/constellation.png'
                  }
                  alt={'constellation'}
                  onClick={() => changeFilter(2)}
                />
              </div>

              <div className="extended-bar-menu-box">
                <ExtendedBarIcon
                  src={
                    barId === 1
                      ? '/images/nav-bar-menu-icons/search_activated.png'
                      : '/images/nav-bar-menu-icons/search.png'
                  }
                  alt={'search'}
                  onClick={openSearchBar}
                />
                <Link to={'/feed'} onClick={openFeedPage}>
                  <ExtendedBarIcon
                    src={
                      barId === 2
                        ? '/images/nav-bar-menu-icons/feed_activated.png'
                        : '/images/nav-bar-menu-icons/feed.png'
                    }
                    alt={'feed'}
                    className={'extended-bar-menu-icon'}
                  />
                </Link>
                <ExtendedBarIcon
                  src={
                    barId === 3
                      ? '/images/nav-bar-menu-icons/notifications_activated.png'
                      : '/images/nav-bar-menu-icons/notifications.png'
                  }
                  alt={'notifications'}
                  onClick={openNotificationBar}
                />
                <Link to={'/settings'} onClick={openSettingsPage}>
                  <ExtendedBarIcon
                    src={
                      barId === 4
                        ? '/images/nav-bar-menu-icons/settings_activated.png'
                        : '/images/nav-bar-menu-icons/settings.png'
                    }
                    alt={'notifications'}
                  />
                </Link>
                <ExtendedBarIcon src={'/images/nav-bar-menu-icons/logout.png'} alt={'logout'} />
              </div>
            </div>

            {filterId === 0 ? (
              <div className="extended-bar-main-contents-box">
                <div className="extended-bar-main-contents-top">
                  <p className="extended-bar-main-contents-top-title">검색 결과</p>
                  {/* <p className="extended-bar-main-reset">모두 지우기</p> */}
                </div>
                <div className="extended-bar-main-contents-user">
                  {users.map((data) => {
                    return <UserView key={data.nickname} data={data} />;
                  })}
                </div>
              </div>
            ) : (
              <div className="extended-bar-main-contents-box">
                <div className="extended-bar-main-contents-top">
                  <p className="extended-bar-main-contents-top-title">연관 검색어</p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
      <div className="nav-bar-toggle-container" onClick={closeExtendedBar}>
        <div className="nav-bar-menu-icon-box">
          <img
            src="/images/nav-bar-toggle-button/close.png"
            alt="toggle_button"
            className="toggle-button"
          />
        </div>
      </div>
    </div>
  );
}
