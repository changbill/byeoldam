import { useRef } from 'react';
import { Layer, Stage, Image as KonvaImage, Line, Circle } from 'react-konva';
import getUuidByString from 'uuid-by-string';
import getSegmentDistance from '../../utils/getSegmentDistance';

export default function ContoursEditor({
  image,
  width,
  height,
  points,
  setPoints,
  history,
  setHistory,
}) {
  const editor = useRef();
  const imageRef = useRef();

  const getUuidByPosition = (x, y) => {
    const string = `${x},${y}`;
    return getUuidByString(string);
  };

  const deletePoint = (index) => {
    setHistory([...history, points]);
    setPoints([...points.slice(0, index), ...points.slice(index + 1)]);
  };

  const movePoint = (x, y, index) => {
    setHistory([...history, points]);
    setPoints([...points.slice(0, index), [x, y], ...points.slice(index + 1)]);
  };

  const addPoint = (x, y, index) => {
    setHistory([...history, points]);
    setPoints([...points.slice(0, index + 1), [x, y], ...points.slice(index + 1)]);
  };

  const saveClippedImage = () => {
    imageRef.current.clipFunc((ctx) => {
      ctx.beginPath();
      ctx.moveTo(points[0][0], points[0][1]);
      for (let i = 1; i < points.length; i++) {
        ctx.lineTo(points[i][0], points[i][1]);
      }
      ctx.closePath();
    });

    editor.draw();
  };

  const globalClickHandler = (e) => {
    if (e.evt.button === 0) {
      const x = e.evt.offsetX;
      const y = e.evt.offsetY;
      let nearestLineIdx = -1;
      let minDist = Infinity;

      for (let i = 0; i < points.length; i++) {
        const dist = getSegmentDistance(points[i], points[(i + 1) % points.length], [x, y]);
        if (minDist > dist) {
          minDist = dist;
          nearestLineIdx = i;
        }
      }

      if (nearestLineIdx > 0 && nearestLineIdx < points.length) addPoint(x, y, nearestLineIdx);
    } else if (e.evt.button === 1) {
      saveClippedImage();
    }
  };

  return (
    <Stage
      ref={editor}
      width={width}
      height={height}
      onContextMenu={(e) => {
        e.evt.preventDefault();
      }}
      onClick={globalClickHandler}
    >
      <Layer>
        <KonvaImage image={image} opacity={0.6} x={0} y={0} width={width} height={height} />
        {points.map(([x, y], i) => (
          <Line
            key={getUuidByPosition(x, y)}
            index={i}
            points={[x, y, ...points[(i + 1) % points.length]]}
            stroke={'red'}
            strokeWidth={4}
            closed={true}
          />
        ))}

        {points.map(([x, y], i) => (
          <Circle
            index={i}
            key={getUuidByPosition(x, y)}
            x={x}
            y={y}
            radius={6}
            fill={'white'}
            stroke={'red'}
            strokeWidth={1}
            onDragEnd={({ target }) => {
              movePoint(target.attrs.x, target.attrs.y, target.attrs.index);
            }}
            onMouseEnter={() => {
              editor.current.container().style.cursor = 'pointer';
            }}
            onMouseLeave={() => {
              editor.current.container().style.cursor = 'auto';
            }}
            onClick={(e) => {
              if (e.evt.button === 2) deletePoint(e.target.attrs.index);
            }}
            draggable
          />
        ))}
      </Layer>
    </Stage>
  );
}
