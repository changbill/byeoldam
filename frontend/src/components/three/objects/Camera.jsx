import { PerspectiveCamera } from '@react-three/drei';
import React from 'react';

const Camera = React.forwardRef((props, ref) => {
  return <PerspectiveCamera ref={ref} makeDefault />;
});

export default Camera;
