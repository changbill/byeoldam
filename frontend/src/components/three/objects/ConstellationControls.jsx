import { useEffect, useRef, useState } from 'react';
import { useFrame } from '@react-three/fiber';
import { easing } from 'maath';
import * as THREE from 'three';

import Constellation from './Constellation';

export default function ConstellationControls({ controller, constellationList }) {
  const controllerRef = controller;
  const ref = useRef();
  const clicked = useRef();
  const [selected, setSelected] = useState('none');
  const [lastCameraState, setLastCameraState] = useState({
    prev: controllerRef?.current?.object?.clone(),
    target: new THREE.Vector3(0, 0.03, 0),
    position: new THREE.Vector3(0, 0, 0.1),
    isDone: true,
  });

  useEffect(() => {
    clicked.current = ref.current.getObjectByName(selected);

    if (clicked.current) {
      setLastCameraState({
        prev: !lastCameraState.prev ? controllerRef.current.object.clone() : lastCameraState.prev,
        target: clicked.current.localToWorld(new THREE.Vector3(0, 0, 0)),
        position: new THREE.Vector3(0, 3, 0),
        isDone: false,
      });
    } else {
      if (controllerRef.current.target.distanceTo(new THREE.Vector3(0, 0, 0)) < 0.00001) {
        controllerRef.current.target = new THREE.Vector3(0, 0.001, 0);
      }
      const lastTarget = lastCameraState.target;
      const origin = new THREE.Vector3(0, 0.01, 0);

      const dist = lastCameraState.target.distanceTo(origin);
      if (dist < 0.00000001) return;
      const ratio = 0.104403 / dist;

      lastTarget.subVectors(lastTarget, origin);
      origin.subVectors(origin, lastTarget.multiplyScalar(ratio));

      setLastCameraState({
        prev: null,
        target: new THREE.Vector3(0, 0.03, 0),
        position: origin,
        isDone: false,
      });
    }
  }, [selected]);

  useFrame((state, dt) => {
    if (!lastCameraState.isDone) {
      if (!lastCameraState.prev) {
        easing.damp3(controllerRef.current.target, lastCameraState.target, 0.2, dt);
        easing.damp3(controllerRef.current.object.position, lastCameraState.position, 0.2, dt);
      } else {
        // const ease = (x) => (0.0041 * x * x - 0.0137 * x + 0.1857) / 20;
        // const smoothTime = ease(controllerRef.current.target.distanceTo(lastCameraState.target));
        const smoothTime = 0.2;
        easing.damp3(controllerRef.current.target, lastCameraState.target, smoothTime, dt);
        easing.damp3(
          controllerRef.current.object.position,
          lastCameraState.position,
          smoothTime / 1.1,
          dt,
        );
      }
      controllerRef.current.minDistance = 0;
      if (
        controllerRef.current.target.distanceTo(lastCameraState.target) < 0.1 ||
        controllerRef.current.object.position.distanceTo(lastCameraState.position) < 0.1
      ) {
        if (
          controllerRef.current.target.distanceTo(lastCameraState.target) > 2 ||
          controllerRef.current.object.position.distanceTo(lastCameraState.position) > 2
        )
          return;

        setLastCameraState({ ...lastCameraState, isDone: true });
        if (!lastCameraState.prev) {
          controllerRef.current.enableRotate = true;
          controllerRef.current.enableDamping = true;
        } else {
          const distance = new THREE.Vector3(0, 0, 0).distanceTo(lastCameraState.target);
          controllerRef.current.enableZoom = true;
          controllerRef.current.maxDistance = distance;
          controllerRef.current.minDistance = distance / 3;
        }
      }
    }
  });

  return (
    <group
      ref={ref}
      onClick={(e) => {
        e.stopPropagation();
        if (!lastCameraState.isDone || !e.object || !e.object.name) return;
        controllerRef.current.enableRotate = false;
        controllerRef.current.enableDamping = false;
        controllerRef.current.enableZoom = false;
        setSelected(clicked.current === e.object ? 'none' : e.object.name);
      }}
    >
      {constellationList.map(({ id, position, points, hoverArticles, thumbnail }) => (
        <Constellation
          key={id}
          name={id}
          id={id}
          position={position}
          selected={selected}
          points={points}
          hoverArticles={hoverArticles}
          thumbnail={thumbnail}
        />
      ))}
    </group>
  );
}
