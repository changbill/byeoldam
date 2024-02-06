const generateColor = (index, maxNum) => {
  if (index < 0 || index > maxNum - 1) {
    throw new Error('Index out of range');
  }
  var hue = Math.round(360 * (index / maxNum));
  return 'hsl(' + hue + ', 100%, 50%)';
};

export default generateColor;
