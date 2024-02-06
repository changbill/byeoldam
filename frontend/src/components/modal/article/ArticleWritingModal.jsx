import { useState } from 'react';
import ImageUpload from '../../article/ImageUpload';
import AddDescription from '../../article/AddDescription';
import SelectConstellation from '../../article/SelectConstellation';
import { addArticle } from '../../../apis/articles';

export default function ArticleWritingModal({ nickName }) {
  const [step, setStep] = useState(1);
  const [image, setImage] = useState(null);
  const [details, setDetails] = useState({
    description: '',
    tags: '',
  });
  const [settings, setSettings] = useState({
    disclosure: '',
    constellation: '',
  });

  const writeArticle = async () => {
    if (!image || !details.description?.trim()) return;

    console.log({ nickName, image, settings, details });

    const { resultCode } = addArticle({
      nickName,
      image,
      disclosure: settings.disclosure,
      articleName: details.description,
      tagName: details.tags,
    });

    return resultCode;
  };

  return (
    <div>
      {step == 1 && <ImageUpload setStep={setStep} setImage={setImage} />}
      {step == 2 && (
        <AddDescription setStep={setStep} setDetails={setDetails} details={details} image={image} />
      )}
      {step == 3 && (
        <SelectConstellation
          addArticle={writeArticle}
          settings={settings}
          setSettings={setSettings}
        />
      )}
    </div>
  );
}
