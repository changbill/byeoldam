import { useCursor } from '@react-three/drei';
import { useState } from 'react';
import Thumbnail from './Thumbnail';
import useModal from '../../../hooks/useModal';
import ArticleModal from '../../modal/article/ArticleModal';

import { getArticles } from '../../../apis/articles';

export default function Star({
  position = [0, 0, 0],
  scale = 1.5,
  isActive,
  thumbnail,
  articleId = 1,
}) {
  const [hovered, hover] = useState(false);
  const [modalState, setModalState] = useModal();

  const openAritcleModal = async () => {
    const { resultCode, data } = await getArticles(1);
    // const data = {
    //   title: '글 제목',
    //   description: '글 내용',
    //   hits: 3,
    //   createdAt: '2023-01-01',
    // };

    setModalState({
      isOpen: true,
      title: data.title,
      children: (
        <ArticleModal
          articleId={articleId}
          description={data.description}
          hits={data.hits}
          createdAt={data.createdAt}
        />
      ),
    });
  };

  useCursor(hovered);

  return (
    <group position={position}>
      <mesh
        scale={scale * (hovered ? 1 : 0.7)}
        onPointerOver={(e) => {
          e.stopPropagation();
          if (!isActive) return;
          hover(true);
        }}
        onPointerOut={() => {
          if (!isActive) return;
          hover(false);
        }}
        onClick={(e) => {
          e.stopPropagation();
          if (!isActive) return;
          openAritcleModal();
        }}
      >
        <sphereGeometry />
        <meshBasicMaterial toneMapped={false} />
      </mesh>
      {hovered && (
        <Thumbnail
          url={thumbnail}
          position={[position[0] < 0 ? -15 : 15, position[1] < 0 ? -15 : 15, 2]}
        />
      )}
    </group>
  );
}
