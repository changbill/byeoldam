import { Suspense, useEffect, useRef, useState } from 'react';
import { Canvas } from '@react-three/fiber';
import { FirstPersonControls, Stars } from '@react-three/drei';
import { useNavigate } from 'react-router-dom';

import ConstellationGenerator from '../../components/three/objects/ConstellationGenerator';
import Logo from '../../assets/images/temporary-logo.png';
import {
  signup,
  checkEmail,
  checkNickname,
  verificateEmail,
  verificateCode,
} from '../../apis/member';

export default function SignUp() {
  const controller = useRef();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [checkPassword, setCheckPassword] = useState('');
  const [name, setName] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [nickname, setNickname] = useState('');
  const [isDuplicatedNickname, setisDuplicatedNickname] = useState(false);
  const [isDuplicatedEmail, setisDuplicatedEmail] = useState(false);
  const [isSamePassword, setIsSamePassword] = useState(true);
  const [isStatredEmailValidation, setIsStartedEmailValidation] = useState(false);
  const [countDown, setCountDown] = useState(300);
  const [validationCode, setValidationCode] = useState('');
  const [isEndValidation, setIsEndValidation] = useState(false);

  const userSignUp = (e) => {
    e.preventDefault();

    if (
      !email.trim() ||
      !password.trim() ||
      !checkPassword.trim() ||
      !nickname.trim() ||
      !name.trim()
    )
      return;
    if (isDuplicatedNickname || isDuplicatedEmail || !isEndValidation) return;
    signup({ email, password, name, nickname })
      .then((result) => {
        alert('회원가입에 성공했습니다.');
        navigate('/login');
      })
      .catch((e) => {
        console.error(e);
      });
  };

  const timeFormat = (time) => {
    return time < 10 ? `0${time}` : time;
  };

  useEffect(() => {
    checkNickname(nickname).then(({ data }) => {
      setisDuplicatedNickname(!data.result);
    });
  }, [nickname]);

  useEffect(() => {
    checkEmail(email).then(({ data }) => {
      setisDuplicatedEmail(!data.result);
    });
  }, [email]);

  useEffect(() => {
    if (!password.trim() && !checkPassword.trim()) setIsSamePassword(true);
    else if (!password.trim() || !checkPassword.trim()) setIsSamePassword(false);
    else setIsSamePassword(password.trim() === checkPassword.trim());
  }, [password, checkPassword]);

  useEffect(() => {
    if (isStatredEmailValidation) {
      const startTime = new Date();
      const countDownInterval = setInterval(() => {
        if (countDown > 0) setCountDown(parseInt(300 - (new Date() - startTime) / 1000));
      }, 1000);

      return () => {
        clearInterval(countDownInterval);
      };
    } else {
      setCountDown(300);
    }
  }, [isStatredEmailValidation]);

  useEffect(() => {
    if (countDown <= 0) setIsStartedEmailValidation(false);
  }, [countDown]);

  useEffect(() => {
    if (sessionStorage['access_token'] && sessionStorage['profile']) navigate('/home');
  }, []);

  return (
    <div className="member-container">
      <div className="member-box">
        <div className="member-logo-box">
          <img src={Logo} className="member-box-logo" alt="logo" />
        </div>
        <form className="member-login-box" onSubmit={userSignUp}>
          <label htmlFor="name" className="member-input-label">
            이름
          </label>
          <input
            className="member-input"
            type="text"
            name="name"
            onChange={(e) => {
              setName(e.target.value);
            }}
            placeholder="이름을 입력해주세요"
          />
          <label htmlFor="nickname" className="member-input-label">
            닉네임
          </label>
          <input
            className="member-input"
            type="text"
            name="nickname"
            onChange={(e) => {
              setNickname(e.target.value);
            }}
            placeholder="닉네임을 입력해주세요"
          />
          {isDuplicatedNickname && <div className="member-duplicated">중복된 닉네임입니다</div>}
          <label htmlFor="email" className="member-input-label">
            이메일
          </label>
          <div className="member-input-email-box">
            <input
              className="member-input"
              type="email"
              name="email"
              onChange={(e) => {
                setEmail(e.target.value);
              }}
              placeholder="이메일을 입력해주세요"
              disabled={isStatredEmailValidation}
            />
            <button
              type="button"
              onClick={() => {
                if (!email.trim() || isDuplicatedEmail || isStatredEmailValidation) return;
                setIsStartedEmailValidation(true);
                verificateEmail(email);
              }}
            >
              인증하기
            </button>
          </div>
          {isDuplicatedEmail && <div className="member-duplicated">이미 가입한 이메일입니다.</div>}
          {isStatredEmailValidation && (
            <div className="member-input-validation-container">
              <label htmlFor="validation" className="member-input-label">
                인증코드
              </label>
              <div className="member-input-validation-box">
                <div className="member-input-validation-wrapper">
                  <input
                    className="member-input"
                    type="text"
                    name="validation"
                    onChange={(e) => {
                      setValidationCode(e.target.value);
                    }}
                    placeholder="이메일을 입력해주세요"
                    readOnly={isEndValidation}
                  />
                  <div>{`${timeFormat(parseInt(countDown / 60))}:${timeFormat(countDown % 60)}`}</div>
                </div>
                <button
                  type="button"
                  onClick={() => {
                    verificateCode({ email, code: validationCode }).then((result) => {
                      setIsEndValidation(true);
                    });
                  }}
                >
                  확인
                </button>
              </div>
            </div>
          )}
          {isEndValidation && <div className="member-validation-end">인증이 완료되었습니다.</div>}
          <label htmlFor="password" className="member-input-label">
            비밀번호
          </label>
          <input
            className="member-input"
            type="password"
            name="password"
            onChange={(e) => {
              setPassword(e.target.value);
            }}
            placeholder="비밀번호를 입력해주세요"
          />
          <label htmlFor="passwordCheck" className="member-input-label">
            비밀번호 확인
          </label>
          <input
            className="member-input"
            type="password"
            name="passwordCheck"
            onChange={(e) => {
              setCheckPassword(e.target.value);
            }}
            placeholder="비밀번호를 다시 입력해주세요"
          />
          {!isSamePassword && <div className="member-duplicated">비밀번호가 일치하지 않습니다</div>}
          {/* <label htmlFor="birthDate" className="member-input-label">
            생일
          </label>
          <input
            className="member-input"
            type="date"
            name="birthDate"
            max={new Date().toISOString().slice(0, 10)}
            onChange={(e) => {
              setBirthDate(e.target.value);
            }}
          /> */}
          <button className="member-button">회원가입</button>
        </form>
        <div
          className="member-singup-button"
          onClick={() => {
            navigate('/login');
          }}
        >
          로그인 화면으로 가기
        </div>
      </div>
      <Canvas>
        <FirstPersonControls ref={controller} position={[0, 0, 0]} movementSpeed={0} />
        <Stars saturation={0.1} speed={0.7} fade />
        <Suspense fallback={null}>
          <ConstellationGenerator controllerRef={controller} />
        </Suspense>
      </Canvas>
    </div>
  );
}
