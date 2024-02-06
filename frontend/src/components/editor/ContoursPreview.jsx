import React from 'react';
import { Stage, Layer, Line, Circle } from 'react-konva';

import resizePoints from '../../utils/resizePoints';
import generateColor from '../../utils/generateColor';

export default function ContoursPreview({ width, height, selected, hovered, pointList, points }) {
  return (
    <Stage width={width} height={height}>
      <Layer>
        {selected < pointList.length && hovered < pointList.length && (
          <Line
            points={resizePoints(
              hovered > -1 ? pointList[hovered] : points,
              1200,
              1200,
              600,
              600,
            ).reduce((prev, [x, y]) => [...prev, x, y], [])}
            stroke={generateColor(hovered > -1 ? hovered : selected, pointList.length)}
            strokeWidth={4}
            closed={true}
          />
        )}
        {selected < pointList.length &&
          hovered < pointList.length &&
          resizePoints(hovered > -1 ? pointList[hovered] : points, 1200, 1200, 600, 600).map(
            ([x, y], i) => (
              <Circle
                index={i}
                key={i}
                x={x}
                y={y}
                radius={4}
                fill={'black'}
                stroke={generateColor(hovered > -1 ? hovered : selected, pointList.length)}
                strokeWidth={1}
                lineCap={'round'}
                lineJoin={'round'}
              />
            ),
          )}
      </Layer>
    </Stage>
  );
}
