import { useRef } from 'react';

export default function ImageUpload({ setStep, setImage }) {
  const canvas = useRef();

  const uploadImage = () => {
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');

    input.click();

    input.addEventListener('change', () => {
      const image = input.files[0];
      setImage(image);

      const img = new Image();
      img.src = URL.createObjectURL(image);
      img.onload = () => {
        const ctx = canvas.current.getContext('2d');
        ctx.drawImage(img, 0, 0, 600, 600);
      };
    });
  };

  return (
    <div>
      <canvas ref={canvas} width={600} height={600} />
      <div
        onClick={(e) => {
          e.stopPropagation();
          uploadImage();
        }}
      >
        사진 추가
      </div>
      <button
        type="button"
        onClick={() => {
          setStep(2);
        }}
      >
        다음
      </button>
    </div>
  );
}
