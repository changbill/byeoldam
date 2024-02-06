import { useEffect, useState } from 'react';

import douglasPeucker from '../../utils/douglasPeucker';

import ContoursEditor from '../../components/editor/ContoursEditor';
import MaskPreview from '../../components/editor/MaskPreview';
import ContoursPreview from '../../components/editor/ContoursPreview';

export default function ConstellationWriting() {
  const [points, setPoints] = useState([]);
  const [image, setImage] = useState(null);
  const [pointList, setPointList] = useState([]);
  const [selected, setSelected] = useState(0);
  const [hovered, setHovered] = useState(-1);
  const [history, setHistory] = useState([]);

  useEffect(() => {
    fetch('./src/assets/data/test.json')
      .then((data) => data.json())
      .then((data) => {
        setPointList(
          data
            .map((dt) => {
              const height = dt.image_height;
              const width = dt.image_width;

              const zipped = douglasPeucker(
                dt.contours.map(([point]) => {
                  const [x, y] = point;
                  return { x, y };
                }),
                3,
              );

              return zipped.map(({ x, y }) => [(x * 1200) / width, (y * 1200) / height]);
            })
            .filter((dt) => dt.length > 5),
        );
      });

    const img = new Image();
    img.src = './src/assets/images/sample/test.jpg';
    img.onload = () => {
      setImage(img);
    };

    window.addEventListener('keydown', (e) => {});
  }, []);

  useEffect(() => {
    if (pointList.length > 0) {
      setHistory([]);
      setPoints(pointList[selected]);
    }
  }, [selected]);

  useEffect(() => {
    if (pointList.length > 0) setPoints(pointList[0]);
  }, [pointList]);

  const undo = () => {
    if (history.length == 0) return;
    setPoints(history[history.length - 1]);
    setHistory([...history.slice(0, history.length - 1)]);
  };

  const keyInputHandler = (e) => {
    if (e.key === 'z' && (e.ctrlKey || e.metaKey)) undo();
  };

  return (
    <div
      style={{ display: 'flex', flexDirection: 'row', marginLeft: '360px' }}
      tabIndex={1}
      onKeyDown={keyInputHandler}
    >
      <ContoursEditor
        width={1200}
        height={1200}
        image={image}
        points={points}
        setPoints={setPoints}
        setHistory={setHistory}
        history={history}
      />
      <div style={{ height: '680px', display: 'flex', flexDirection: 'column' }}>
        <MaskPreview
          width={600}
          height={600}
          pointList={pointList}
          setHovered={setHovered}
          setSelected={setSelected}
          image={image}
        />
        <ContoursPreview
          width={600}
          height={600}
          selected={selected}
          hovered={hovered}
          pointList={pointList}
          points={points}
        />
      </div>
    </div>
  );
}
