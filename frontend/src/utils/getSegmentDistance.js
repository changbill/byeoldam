const getDistance = (a, b) => {
  return Math.sqrt((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]));
};

const getSegmentDistance = (a, b, p) => {
  const AtoB = getDistance(a, b);
  if (AtoB < 0.00001) return getDistance(a, p);

  // 직선 AP와 AB의 내적을 구하고 선분 AB의 길이로 나누면 점 P를 직선 AB에 수직으로 내린 수선의 길이를 알 수 있다.
  // 결과가 음수면 선분 AB와 점 P의 길이는 AP의 길이와 같고
  // 결과가 1보다 크면 BP의 길이와 같다
  // 그 외의 경우는 수선의 발이 직선과의 거리가 된다.
  const prj = ((p[0] - a[0]) * (b[0] - a[0]) + (p[1] - a[1]) * (b[1] - a[1])) / AtoB;
  if (prj < 0) return getDistance(a, p);
  else if (prj > AtoB) return getDistance(b, p);
  return Math.abs((b[0] - a[0]) * (p[1] - a[1]) - (p[0] - a[0]) * (b[1] - a[1])) / AtoB;
};

export default getSegmentDistance;
