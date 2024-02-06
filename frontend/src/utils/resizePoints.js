const resizePoints = (points, prevWidth, prevHeight, newWidth, newHeight) => {
  const [lx, rx] = points.reduce(
    (prev, now) => [Math.min(now[0], prev[0]), Math.max(now[0], prev[1])],
    [prevWidth, 0],
  );

  const [dy, uy] = points.reduce(
    (prev, now) => [Math.min(now[1], prev[0]), Math.max(now[1], prev[1])],
    [prevHeight, 0],
  );

  const width = rx - lx;
  const height = uy - dy;
  const size = Math.max(width, height) * 1.1;

  const newPoints = points.map(([x, y]) => {
    const X = (x - lx + (size - width) / 2) / size;
    const Y = (y - dy + (size - height) / 2) / size;

    return [X * newWidth, Y * newHeight];
  });

  return newPoints;
};

export default resizePoints;
