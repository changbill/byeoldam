// const constellationList = [
//   { id: 0, url: './src/assets/images/sample/cat.png', position: [0, 20, 50] },
//   { id: 1, url: './src/assets/images/sample/cat.png', position: [0, 20, -50] },
//   { id: 2, url: './src/assets/images/sample/cat.png', position: [-50, 20, 0] },
//   { id: 3, url: './src/assets/images/sample/cat.png', position: [-35.3, 20, 35.3] },
//   { id: 4, url: './src/assets/images/sample/cat.png', position: [35.3, 20, 35.3] },
//   { id: 5, url: './src/assets/images/sample/cat.png', position: [-35.3, 20, -35.3] },
//   { id: 6, url: './src/assets/images/sample/cat.png', position: [35.3, 20, -35.3] },
//   { id: 7, url: './src/assets/images/sample/cat.png', position: [50, 20, 0] },
// ];

const R = 40;
const size = 15;
const data = [
  {
    id: 'bread',
    url: './src/assets/data/bread.json',
    thumbnail: './src/assets/images/sample/bread.jpg',
  },
  { id: 'car', url: './src/assets/data/car.json', thumbnail: './src/assets/images/sample/car.jpg' },
  { id: 'cat', url: './src/assets/data/cat.json', thumbnail: './src/assets/images/sample/cat.jpg' },
  {
    id: 'chicken',
    url: './src/assets/data/chicken.json',
    thumbnail: './src/assets/images/sample/chicken.jpg',
  },
  {
    id: 'coffee',
    url: './src/assets/data/coffee.json',
    thumbnail: './src/assets/images/sample/coffee.jpg',
  },
  {
    id: 'couple',
    url: './src/assets/data/couple.json',
    thumbnail: './src/assets/images/sample/couple.jpg',
  },
  {
    id: 'diary',
    url: './src/assets/data/diary.json',
    thumbnail: './src/assets/images/sample/diary.jpg',
  },
  {
    id: 'family',
    url: './src/assets/data/family.json',
    thumbnail: './src/assets/images/sample/family.jpg',
  },
  {
    id: 'food',
    url: './src/assets/data/food.json',
    thumbnail: './src/assets/images/sample/food.jpg',
  },
  {
    id: 'redpanda',
    url: './src/assets/data/redpanda.json',
    thumbnail: './src/assets/images/sample/redpanda.jpg',
  },
  {
    id: 'running',
    url: './src/assets/data/running.json',
    thumbnail: './src/assets/images/sample/running.jpg',
  },
  {
    id: 'scenery',
    url: './src/assets/data/scenery.json',
    thumbnail: './src/assets/images/sample/scenery.jpg',
  },
  {
    id: 'selfie',
    url: './src/assets/data/selfie.json',
    thumbnail: './src/assets/images/sample/selfie.jpg',
  },
  {
    id: 'sports',
    url: './src/assets/data/sports.json',
    thumbnail: './src/assets/images/sample/sports.jpg',
  },
  {
    id: 'friend',
    url: './src/assets/data/friend.json',
    thumbnail: './src/assets/images/sample/friend.jpg',
  },
];

const constellationList = [
  ...Array.from({ length: size }, (x, i) => {
    const rand = Math.random() * 10;
    const angle = (Math.PI / 180) * (i % 2 ? 42 + rand : 67 + rand);
    const pos =
      (Math.PI / 180) * (((2 * 360) / size) * parseInt(i / 2) + rand * 4 + (i % 2 ? 0 : 10));

    return {
      id: data[i].id,
      position: [
        Math.cos(pos) * R * Math.sin(angle),
        R * Math.cos(angle),
        Math.sin(pos) * R * Math.sin(angle),
      ],
      url: data[i].url,
      thumbnail: data[i].thumbnail,
    };
  }),
];

export default constellationList;
