import * as THREE from 'three';
import { useRef, useEffect } from 'react';
import { Image } from '@react-three/drei';

export default function Thumbnail({ url, position = [0, 0, 0] }) {
  const image = useRef();

  return (
    <group position={position}>
      <mesh position={[0, 0, -0.5]} scale={[26, 26, 0.5]}>
        <boxGeometry />
        <meshBasicMaterial toneMapped={false} />
      </mesh>
      <Image ref={image} url={url} scale={25} opacity={1} />
    </group>
  );
}
