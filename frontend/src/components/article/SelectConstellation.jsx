import React from 'react';

export default function SelectConstellation({ addArticle, settings, setSettings }) {
  const setDisclosure = (e) => {
    setSettings({ ...settings, disclosure: e.target.value });
  };

  const setConstellation = (e) => {
    setSettings({ ...settings, constellation: e.target.value });
  };

  return (
    <div>
      <label for="constellation">어느 별자리에 포함시키시겠습니까?</label>
      <select name="별자리 선택" onChange={setConstellation}>
        <option value="1">dog</option>
        <option value="2">cat</option>
      </select>
      <select name="공개여부" onChange={setDisclosure}>
        <option value="visible">공개</option>
        <option value="invisible">비공개</option>
      </select>
      <button type="button" onClick={addArticle}>
        등록
      </button>
    </div>
  );
}
