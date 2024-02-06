import React, { useEffect, useRef } from 'react';

export default function AddDescription({ setStep, details, setDetails, image }) {
  const canvas = useRef();

  useEffect(() => {
    const img = new Image();
    img.src = URL.createObjectURL(image);

    img.onload = () => {
      const ctx = canvas.current.getContext('2d');
      ctx.drawImage(img, 0, 0, 400, 400);
    };
  }, [image]);

  const setDescription = (e) => {
    setDetails({
      ...details,
      description: e.target.value,
    });
  };

  const setTags = (e) => {
    setDetails({
      ...details,
      tags: e.target.value,
    });
  };

  return (
    <div>
      <canvas ref={canvas} width={600} height={600} />
      <label htmlFor="description">글 제목</label>
      <input type="text" onChange={setDescription} name="description" />
      <label htmlFor="tags">태그</label>
      <input type="text" onChange={setTags} name="tags" />
      <button
        type="button"
        onClick={() => {
          setStep(3);
        }}
      >
        다음
      </button>
    </div>
  );
}
