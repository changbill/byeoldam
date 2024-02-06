import ConstellationRow from './ConstellationRow';

export default function ConstellationModal() {
  return (
    <div className="constellation-modal-container">
      <div className="">
        {/* map으로 별자리 목록 전부 출력 예정 */}
        <ConstellationRow />
        <ConstellationRow />
      </div>
      <div className="constellation_main_buttons_box">
        <img
          src="/src/assets/images/constellation-modal/constellation_create_button.png"
          alt="post_create_button"
          className="constellation-main-button"
          // onClick={openConstellationModal}
        />
        <img
          src="/src/assets/images/constellation-modal/post_create_button.png"
          alt="post_create_button"
          className="constellation-main-button"
          // onClick={openConstellationModal}
        />
      </div>
    </div>
  );
}
