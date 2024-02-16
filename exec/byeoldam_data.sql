-- MariaDB dump 10.19  Distrib 10.11.6-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: byeol_dam
-- ------------------------------------------------------
-- Server version	10.11.6-MariaDB-1:10.11.6+maria~ubu2204

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  `disclosure` enum('VISIBLE','INVISIBLE') NOT NULL,
  `hits` bigint(20) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `title` varchar(105) NOT NULL,
  `constellation_id` bigint(20) DEFAULT NULL,
  `image` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jopb9qivnsxwrahkamrv98hcw` (`image`),
  KEY `FKj8evmi1rs82988pm1hd79kkqq` (`constellation_id`),
  KEY `FKbrca0qso3spvjp5wlld4xxpd1` (`user_id`),
  CONSTRAINT `FKaalkoivoeipsf94ag214fro5b` FOREIGN KEY (`image`) REFERENCES `image` (`id`),
  CONSTRAINT `FKbrca0qso3spvjp5wlld4xxpd1` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`),
  CONSTRAINT `FKj8evmi1rs82988pm1hd79kkqq` FOREIGN KEY (`constellation_id`) REFERENCES `constellation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES
(1,'2024-02-16 03:29:52.026930',NULL,'제주 아쿠아플라넷에서','VISIBLE',0,'2024-02-16 03:31:48.716105','제주 아쿠아플라넷에서',1,12,1),
(2,'2024-02-16 03:31:19.357834',NULL,'신형 컴퓨터입니다','VISIBLE',0,'2024-02-16 03:31:19.367496','신형 컴퓨터입니다',3,13,1),
(3,'2024-02-16 03:31:54.641651',NULL,'미라이타워','VISIBLE',0,'2024-02-16 03:33:36.151688','미라이타워',4,14,3),
(4,'2024-02-16 03:34:36.157440',NULL,'일본 가정식','VISIBLE',0,'2024-02-16 03:34:36.169649','일본 가정식',4,18,3),
(5,'2024-02-16 03:35:36.263398',NULL,'붉은색 푸른색 신호등','VISIBLE',0,'2024-02-16 03:35:36.277933','붉은색 푸른색 신호등',4,22,3),
(6,'2024-02-16 03:36:17.471007',NULL,'일본 여행 가기 전 셀카','VISIBLE',0,'2024-02-16 03:36:17.483421','일본 여행 가기 전 셀카',4,26,3),
(7,'2024-02-16 03:36:49.281053',NULL,'이것이 히츠마부시','VISIBLE',0,'2024-02-16 03:36:49.289425','이것이 히츠마부시',4,27,3),
(8,'2024-02-16 03:37:08.574473',NULL,'아이유','VISIBLE',0,'2024-02-16 03:37:08.584053','아이유',5,28,1),
(9,'2024-02-16 03:37:36.615530',NULL,'일본거리','INVISIBLE',0,'2024-02-16 03:37:36.620774','일본거리',4,29,3),
(10,'2024-02-16 03:37:41.571585',NULL,'싸이','VISIBLE',0,'2024-02-16 03:37:41.579099','싸이',5,30,1),
(11,'2024-02-16 03:37:52.443346',NULL,'제주 아쿠아 플라넷','VISIBLE',0,'2024-02-16 03:37:52.457791','제주 아쿠아 플라넷',1,31,1),
(12,'2024-02-16 03:38:06.636105',NULL,'비','VISIBLE',0,'2024-02-16 03:38:06.643314','비',5,32,1),
(13,'2024-02-16 03:40:12.012390',NULL,'나 혼자만 아쿠아리움','VISIBLE',0,'2024-02-16 03:40:12.023006','나 혼자만 아쿠아리움',1,33,1),
(14,'2024-02-16 03:40:48.117688',NULL,'송바오와 함께','VISIBLE',0,'2024-02-16 03:40:48.126562','송바오와 함께',2,37,1),
(15,'2024-02-16 03:42:20.907260',NULL,'꽃과 함께','VISIBLE',0,'2024-02-16 03:42:20.918891','꽃과 함께',2,41,1),
(16,'2024-02-16 03:43:04.796034',NULL,'추억 사진','VISIBLE',0,'2024-02-16 03:43:04.808341','추억 사진',2,42,1),
(17,'2024-02-16 03:43:26.284502',NULL,'히익~','VISIBLE',0,'2024-02-16 03:43:26.292320','히익~',2,43,1),
(18,'2024-02-16 03:44:01.873408',NULL,'먹방~ 대나무는 맛있어','VISIBLE',0,'2024-02-16 03:44:01.885130','먹방~ 대나무는 맛있어',2,44,1),
(19,'2024-02-16 03:44:48.467661',NULL,'아쿠아리움','VISIBLE',0,'2024-02-16 03:44:48.480028','아쿠아리움',1,48,1),
(20,'2024-02-16 03:45:55.100543',NULL,'으어어어 늘어진다~','VISIBLE',0,'2024-02-16 03:45:55.115403','으어어어 늘어진다~',2,49,1),
(21,'2024-02-16 03:46:44.507414',NULL,'가을여행','VISIBLE',0,'2024-02-16 03:46:44.511772','가을여행',10,53,3),
(22,'2024-02-16 03:47:12.417607',NULL,'한밭수목원','VISIBLE',0,'2024-02-16 03:47:12.422158','한밭수목원',10,54,3),
(23,'2024-02-16 03:47:27.945882',NULL,'쿵했어요 ㅠㅠ','VISIBLE',0,'2024-02-16 03:47:27.957175','쿵했어요 ㅠㅠ',2,55,1),
(24,'2024-02-16 03:47:43.965312',NULL,'날씨가 좋아서','VISIBLE',0,'2024-02-16 03:47:43.969238','날씨가 좋아서',10,56,3),
(25,'2024-02-16 03:48:49.125242',NULL,'벼야 무럭무럭 자라라','VISIBLE',0,'2024-02-16 03:48:49.129358','벼야 무럭무럭 자라라',10,60,3),
(26,'2024-02-16 03:51:25.323344',NULL,'치킨','VISIBLE',0,'2024-02-16 03:51:25.330851','치킨',12,64,1),
(27,'2024-02-16 03:51:46.402859',NULL,'윈터 아머멘털!','VISIBLE',0,'2024-02-16 03:51:46.410976','윈터 아머멘털!',5,65,1),
(28,'2024-02-16 03:53:19.116457',NULL,'광안리','VISIBLE',0,'2024-02-16 03:53:19.123841','광안리',14,72,3),
(29,'2024-02-16 03:53:46.058207',NULL,'해운대','VISIBLE',0,'2024-02-16 03:53:46.065544','해운대',14,73,3),
(30,'2024-02-16 03:54:08.618131',NULL,'요트위에서 ','VISIBLE',0,'2024-02-16 03:54:08.622153','요트위에서 ',14,74,3),
(31,'2024-02-16 03:54:10.021822',NULL,'인천공항','VISIBLE',0,'2024-02-16 03:54:10.038926','인천공항',13,75,1),
(32,'2024-02-16 03:54:51.137822',NULL,'제주도 여행~','VISIBLE',0,'2024-02-16 03:54:51.154482','제주도 여행~',13,76,1),
(33,'2024-02-16 04:03:11.349383',NULL,'별','VISIBLE',0,NULL,'별',NULL,80,5),
(34,'2024-02-16 04:03:43.345815',NULL,'안동이다!','VISIBLE',0,'2024-02-16 04:03:43.358121','안동이다!',13,81,1),
(35,'2024-02-16 04:03:48.923822',NULL,'별별별','VISIBLE',0,'2024-02-16 04:03:48.926839','별별별',15,82,5),
(36,'2024-02-16 04:04:00.164316',NULL,'바다당','VISIBLE',0,'2024-02-16 04:04:00.167838','바다당',14,83,3),
(37,'2024-02-16 04:04:16.135424',NULL,'스타','VISIBLE',0,'2024-02-16 04:04:16.138434','스타',15,84,5),
(38,'2024-02-16 04:04:35.299295',NULL,'고뇌하는 주혁이형','VISIBLE',0,'2024-02-16 04:04:35.308747','고뇌하는 주혁이형',13,85,1),
(39,'2024-02-16 04:05:29.249928',NULL,'환상의 나라 에버랜드로','VISIBLE',0,'2024-02-16 04:05:29.259700','환상의 나라 에버랜드로',13,86,1),
(40,'2024-02-16 04:06:51.089072',NULL,'대마 라떼','VISIBLE',0,'2024-02-16 04:06:51.100447','대마 라떼',13,90,1),
(41,'2024-02-16 04:09:38.686549',NULL,'시계탑','VISIBLE',0,'2024-02-16 04:09:38.724411','시계탑',4,91,3),
(42,'2024-02-16 04:09:39.589705',NULL,'세부섬','VISIBLE',0,'2024-02-16 04:09:39.608859','세부섬',13,92,1),
(43,'2024-02-16 04:10:28.402769',NULL,'트리트리','VISIBLE',0,'2024-02-16 04:10:28.410014','트리트리',10,93,3),
(44,'2024-02-16 04:11:34.302475',NULL,'삿뽀르','VISIBLE',0,'2024-02-16 04:11:34.322729','삿뽀르',13,97,1),
(45,'2024-02-16 04:13:03.492335',NULL,'도톤보리 완전 좋았는데','VISIBLE',0,'2024-02-16 04:13:03.512487','도톤보리 완전 좋았는데',13,98,1),
(46,'2024-02-16 04:14:55.033350',NULL,'민우형 왜 저러는데','VISIBLE',0,'2024-02-16 04:14:55.046621','민우형 왜 저러는데',13,99,1),
(47,'2024-02-16 04:16:34.180435',NULL,'스키타고 다들 후즐근해','VISIBLE',0,'2024-02-16 04:16:34.190625','스키타고 다들 후즐근해',13,100,1),
(48,'2024-02-16 04:19:06.670417',NULL,'윈터야 ','VISIBLE',0,'2024-02-16 04:19:06.674353','윈터야 ',5,101,1),
(49,'2024-02-16 04:19:33.844786',NULL,'찜닭 먹고 싶어 안동가는 도파밍...','VISIBLE',0,'2024-02-16 04:19:33.858043','찜닭 먹고 싶어 안동가는 도파밍...',13,105,1),
(50,'2024-02-16 04:20:10.425024',NULL,'미륵사지 석탑','VISIBLE',0,'2024-02-16 04:20:10.434782','미륵사지 석탑',13,106,1),
(51,'2024-02-16 04:21:13.787152',NULL,'강아지','VISIBLE',0,'2024-02-16 04:21:13.790697','강아지',18,107,2),
(52,'2024-02-16 04:21:16.012405',NULL,'전북대다!!!! 신정문 예뻐','VISIBLE',0,'2024-02-16 04:21:16.026985','전북대다!!!! 신정문 예뻐',13,108,1),
(53,'2024-02-16 04:23:46.841358',NULL,'연화정도서관','VISIBLE',0,'2024-02-16 04:23:46.853533','연화정도서관',13,109,1),
(54,'2024-02-16 04:24:33.382709',NULL,'한옥마을 사람 진짜 없어','VISIBLE',0,'2024-02-16 04:24:33.394606','한옥마을 사람 진짜 없어',13,110,1),
(55,'2024-02-16 04:26:28.855040',NULL,'전북대 동물인 표범입니다','VISIBLE',0,'2024-02-16 04:26:28.919032','전북대 동물인 표범입니다',13,111,1),
(56,'2024-02-16 04:27:12.466549',NULL,'휴게소 도파민으로 먹는 공짜 간식','VISIBLE',0,'2024-02-16 04:27:12.485194','휴게소 도파민으로 먹는 공짜 간식',13,112,1),
(57,'2024-02-16 04:49:08.292085',NULL,'축신','VISIBLE',0,'2024-02-16 04:49:08.323952','축신',8,114,1);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_hashtag`
--

DROP TABLE IF EXISTS `article_hashtag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_hashtag` (
  `tag_name` varchar(255) NOT NULL,
  PRIMARY KEY (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_hashtag`
--

LOCK TABLES `article_hashtag` WRITE;
/*!40000 ALTER TABLE `article_hashtag` DISABLE KEYS */;
INSERT INTO `article_hashtag` VALUES
('# 귀여워'),
('# 근육통'),
('# 나도 일본'),
('# 눈'),
('# 덕진공원'),
('# 도서관'),
('# 도톤보리'),
('# 도파민'),
('# 도파밍'),
('# 또 가고 싶다'),
('# 맥도날드'),
('# 맥반석 오징어'),
('# 미륵사지'),
('# 빨간 목도리'),
('# 빼는법을 몰라'),
('# 사람 진짜 없어'),
('# 삿포르'),
('# 세부'),
('# 스키'),
('# 신정문'),
('# 아 재밌어'),
('# 야밤마을'),
('# 어묵'),
('# 얼음 조각'),
('# 엄청 추워'),
('# 에메랄드 바다'),
('# 에버랜드'),
('# 여기 어딘데'),
('# 여기 휴게소는 공짜로 음식을 주네'),
('# 여기에 올리라고'),
('# 예쁨'),
('# 오사카'),
('# 오코노미야끼'),
('# 왕궁'),
('# 익산'),
('# 전북대'),
('# 젤리'),
('# 집간다!'),
('# 찜닭'),
('# 표범'),
('# 필리핀'),
('# 하얗다'),
('# 한옥마을'),
('# 한파'),
('# 환상의 나라'),
('#100일'),
('#DRAMA'),
('#imwinter'),
('#JMT'),
('#rain'),
('#siuuuuuuuuuuuuuuu'),
('#ㅋㅋㅋ'),
('#가보자'),
('#가수'),
('#가오리'),
('#가을'),
('#감성'),
('#것을'),
('#고뇌중'),
('#고래'),
('#고래상어'),
('#곧 돌아올 봄'),
('#공항'),
('#광안리'),
('#금산'),
('#기대만땅'),
('#깜놀'),
('#꽃향기'),
('#나고야'),
('#나의 모습'),
('#난 안빼'),
('#날씨미춌다'),
('#노트북'),
('#누우면'),
('#눕고싶다'),
('#대나무'),
('#대마'),
('#대마 라떼 파는 곳'),
('#떠나요'),
('#또 가고 싶다'),
('#또 가고 싶어'),
('#맛집'),
('#먹방'),
('#멋지다'),
('#멍멍'),
('#모든'),
('#물고기'),
('#미래타워'),
('#미소'),
('#바다'),
('#배고파'),
('#배고프다'),
('#별'),
('#봄봄봄'),
('#부산'),
('#불법?'),
('#상어'),
('#설렘'),
('#시계탑'),
('#식사시간'),
('#신호등'),
('#싸이'),
('#쌀농사'),
('#아이유'),
('#아쿠아플라넷'),
('#아프다'),
('#안녕'),
('#안동'),
('#어릴 적'),
('#엄청 크다'),
('#엉덩이'),
('#에스파'),
('#여름 싫어'),
('#여행'),
('#여행을'),
('#요트'),
('#요트 좋아'),
('#인천'),
('#일본'),
('#잊고'),
('#자고싶다'),
('#장어덮밥'),
('#정어리들'),
('#제주'),
('#존맛탱'),
('#찜닭'),
('#창고 카페'),
('#초록초록'),
('#출국'),
('#치킨'),
('#컴퓨터'),
('#트리'),
('#팬더'),
('#푸바오'),
('#해운대'),
('#호 해줄사람');
/*!40000 ALTER TABLE `article_hashtag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_like`
--

DROP TABLE IF EXISTS `article_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `article_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKabthli6g1qjriusniw93pbesw` (`article_id`),
  KEY `FKlxh8br8dtrcpd4f4l8yg3xq27` (`user_id`),
  CONSTRAINT `FKabthli6g1qjriusniw93pbesw` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `FKlxh8br8dtrcpd4f4l8yg3xq27` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_like`
--

LOCK TABLES `article_like` WRITE;
/*!40000 ALTER TABLE `article_like` DISABLE KEYS */;
INSERT INTO `article_like` VALUES
(1,'2024-02-16 04:14:36.741914',NULL,43,1),
(2,'2024-02-16 04:17:17.795153',NULL,25,1),
(3,'2024-02-16 04:17:19.379386',NULL,24,1),
(4,'2024-02-16 04:17:20.850253',NULL,22,1),
(5,'2024-02-16 04:17:25.247864',NULL,9,1),
(6,'2024-02-16 04:17:26.606207',NULL,7,1),
(7,'2024-02-16 04:17:28.607199','2024-02-16 04:17:29.000000',6,1),
(8,'2024-02-16 04:17:32.531610',NULL,5,1);
/*!40000 ALTER TABLE `article_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_tag_relation`
--

DROP TABLE IF EXISTS `article_tag_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_tag_relation` (
  `deleted_at` datetime(6) DEFAULT NULL,
  `saved_at` datetime(6) DEFAULT NULL,
  `article_id` bigint(20) NOT NULL,
  `article_hashtag_id` varchar(255) NOT NULL,
  PRIMARY KEY (`article_id`,`article_hashtag_id`),
  KEY `idx_saved_at` (`saved_at`),
  KEY `FKoinv7jam07ganeedrubjalqt9` (`article_hashtag_id`),
  CONSTRAINT `FKgyya7p1d9lq0fo1epd3jchsxf` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `FKoinv7jam07ganeedrubjalqt9` FOREIGN KEY (`article_hashtag_id`) REFERENCES `article_hashtag` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_tag_relation`
--

LOCK TABLES `article_tag_relation` WRITE;
/*!40000 ALTER TABLE `article_tag_relation` DISABLE KEYS */;
INSERT INTO `article_tag_relation` VALUES
(NULL,'2024-02-16 03:29:52.064596',1,'#가오리'),
(NULL,'2024-02-16 03:29:52.074449',1,'#물고기'),
(NULL,'2024-02-16 03:29:52.079431',1,'#상어'),
(NULL,'2024-02-16 03:29:52.069971',1,'#아쿠아플라넷'),
(NULL,'2024-02-16 03:29:52.058255',1,'#제주'),
(NULL,'2024-02-16 03:31:19.366879',2,'#노트북'),
(NULL,'2024-02-16 03:31:19.362551',2,'#컴퓨터'),
(NULL,'2024-02-16 03:31:54.655889',3,'#나고야'),
(NULL,'2024-02-16 03:31:54.647457',3,'#미래타워'),
(NULL,'2024-02-16 03:31:54.651562',3,'#일본'),
(NULL,'2024-02-16 03:34:36.160981',4,'#나고야'),
(NULL,'2024-02-16 03:34:36.164088',4,'#맛집'),
(NULL,'2024-02-16 03:34:36.169070',4,'#미소'),
(NULL,'2024-02-16 03:35:36.272688',5,'#감성'),
(NULL,'2024-02-16 03:35:36.267738',5,'#신호등'),
(NULL,'2024-02-16 03:35:36.277355',5,'#초록초록'),
(NULL,'2024-02-16 03:36:17.477922',6,'#기대만땅'),
(NULL,'2024-02-16 03:36:17.482892',6,'#설렘'),
(NULL,'2024-02-16 03:36:17.474318',6,'#일본'),
(NULL,'2024-02-16 03:36:49.288899',7,'#나고야'),
(NULL,'2024-02-16 03:36:49.285515',7,'#장어덮밥'),
(NULL,'2024-02-16 03:37:08.583572',8,'#가수'),
(NULL,'2024-02-16 03:37:08.578855',8,'#아이유'),
(NULL,'2024-02-16 03:37:36.619950',9,'#ㅋㅋㅋ'),
(NULL,'2024-02-16 03:37:41.578632',10,'#가수'),
(NULL,'2024-02-16 03:37:41.575778',10,'#싸이'),
(NULL,'2024-02-16 03:37:52.451857',11,'#고래'),
(NULL,'2024-02-16 03:37:52.448726',11,'#물고기'),
(NULL,'2024-02-16 03:37:52.457309',11,'#상어'),
(NULL,'2024-02-16 03:37:52.446592',11,'#제주'),
(NULL,'2024-02-16 03:38:06.640041',12,'#rain'),
(NULL,'2024-02-16 03:38:06.642884',12,'#가수'),
(NULL,'2024-02-16 03:40:12.022573',13,'#가오리'),
(NULL,'2024-02-16 03:40:12.019797',13,'#식사시간'),
(NULL,'2024-02-16 03:40:12.016426',13,'#정어리들'),
(NULL,'2024-02-16 03:40:48.125844',14,'#팬더'),
(NULL,'2024-02-16 03:40:48.121688',14,'#푸바오'),
(NULL,'2024-02-16 03:42:20.918458',15,'#곧 돌아올 봄'),
(NULL,'2024-02-16 03:42:20.911204',15,'#꽃향기'),
(NULL,'2024-02-16 03:42:20.914822',15,'#봄봄봄'),
(NULL,'2024-02-16 03:43:04.804899',16,'#100일'),
(NULL,'2024-02-16 03:43:04.807972',16,'#나의 모습'),
(NULL,'2024-02-16 03:43:04.800633',16,'#어릴 적'),
(NULL,'2024-02-16 03:43:26.288574',17,'#깜놀'),
(NULL,'2024-02-16 03:43:26.291925',17,'#안녕'),
(NULL,'2024-02-16 03:44:01.881373',18,'#대나무'),
(NULL,'2024-02-16 03:44:01.877257',18,'#먹방'),
(NULL,'2024-02-16 03:44:01.884684',18,'#배고프다'),
(NULL,'2024-02-16 03:44:48.471340',19,'#고래상어'),
(NULL,'2024-02-16 03:44:48.479631',19,'#또 가고 싶다'),
(NULL,'2024-02-16 03:44:48.475823',19,'#엄청 크다'),
(NULL,'2024-02-16 03:45:55.104297',20,'#누우면'),
(NULL,'2024-02-16 03:45:55.114998',20,'#눕고싶다'),
(NULL,'2024-02-16 03:45:55.111683',20,'#여름 싫어'),
(NULL,'2024-02-16 03:45:55.108313',20,'#자고싶다'),
(NULL,'2024-02-16 03:46:44.511429',21,'#가을'),
(NULL,'2024-02-16 03:47:12.421835',22,'#멋지다'),
(NULL,'2024-02-16 03:47:27.956828',23,'#아프다'),
(NULL,'2024-02-16 03:47:27.953745',23,'#엉덩이'),
(NULL,'2024-02-16 03:47:27.949715',23,'#호 해줄사람'),
(NULL,'2024-02-16 03:47:43.968876',24,'#날씨미춌다'),
(NULL,'2024-02-16 03:48:49.128990',25,'#쌀농사'),
(NULL,'2024-02-16 03:51:25.327271',26,'#배고파'),
(NULL,'2024-02-16 03:51:25.330484',26,'#치킨'),
(NULL,'2024-02-16 03:51:46.406566',27,'#DRAMA'),
(NULL,'2024-02-16 03:51:46.410607',27,'#에스파'),
(NULL,'2024-02-16 03:53:19.120201',28,'#광안리'),
(NULL,'2024-02-16 03:53:19.123562',28,'#부산'),
(NULL,'2024-02-16 03:53:46.065171',29,'#요트'),
(NULL,'2024-02-16 03:53:46.061916',29,'#해운대'),
(NULL,'2024-02-16 03:54:08.621825',30,'#요트 좋아'),
(NULL,'2024-02-16 03:54:10.025391',31,'#가보자'),
(NULL,'2024-02-16 03:54:10.032254',31,'#공항'),
(NULL,'2024-02-16 03:54:10.028379',31,'#여행'),
(NULL,'2024-02-16 03:54:10.035480',31,'#인천'),
(NULL,'2024-02-16 03:54:10.038633',31,'#출국'),
(NULL,'2024-02-16 03:54:51.144864',32,'#것을'),
(NULL,'2024-02-16 03:54:51.151092',32,'#떠나요'),
(NULL,'2024-02-16 03:54:51.154141',32,'#모든'),
(NULL,'2024-02-16 03:54:51.148085',32,'#여행을'),
(NULL,'2024-02-16 03:54:51.141717',32,'#잊고'),
(NULL,'2024-02-16 04:03:11.353213',33,'#별'),
(NULL,'2024-02-16 04:03:43.349301',34,'#난 안빼'),
(NULL,'2024-02-16 04:03:43.352296',34,'#대마 라떼 파는 곳'),
(NULL,'2024-02-16 04:03:43.357887',34,'#안동'),
(NULL,'2024-02-16 04:03:43.355156',34,'#찜닭'),
(NULL,'2024-02-16 04:03:48.926583',35,'#별'),
(NULL,'2024-02-16 04:04:00.167576',36,'#바다'),
(NULL,'2024-02-16 04:04:16.138166',37,'#별'),
(NULL,'2024-02-16 04:04:35.305994',38,'#고뇌중'),
(NULL,'2024-02-16 04:04:35.308526',38,'#금산'),
(NULL,'2024-02-16 04:04:35.302864',38,'#창고 카페'),
(NULL,'2024-02-16 04:05:29.253068',39,'# 에버랜드'),
(NULL,'2024-02-16 04:05:29.255923',39,'# 환상의 나라'),
(NULL,'2024-02-16 04:05:29.259440',39,'#또 가고 싶어'),
(NULL,'2024-02-16 04:06:51.100175',40,'#JMT'),
(NULL,'2024-02-16 04:06:51.097369',40,'#대마'),
(NULL,'2024-02-16 04:06:51.094836',40,'#불법?'),
(NULL,'2024-02-16 04:06:51.092217',40,'#존맛탱'),
(NULL,'2024-02-16 04:09:38.722912',41,'#시계탑'),
(NULL,'2024-02-16 04:09:39.607923',42,'# 세부'),
(NULL,'2024-02-16 04:09:39.596405',42,'# 에메랄드 바다'),
(NULL,'2024-02-16 04:09:39.602139',42,'# 필리핀'),
(NULL,'2024-02-16 04:10:28.409158',43,'#트리'),
(NULL,'2024-02-16 04:11:34.317148',44,'# 눈'),
(NULL,'2024-02-16 04:11:34.312294',44,'# 삿포르'),
(NULL,'2024-02-16 04:11:34.307783',44,'# 얼음 조각'),
(NULL,'2024-02-16 04:11:34.321881',44,'# 하얗다'),
(NULL,'2024-02-16 04:13:03.507459',45,'# 도톤보리'),
(NULL,'2024-02-16 04:13:03.502180',45,'# 또 가고 싶다'),
(NULL,'2024-02-16 04:13:03.497544',45,'# 오사카'),
(NULL,'2024-02-16 04:13:03.511815',45,'# 오코노미야끼'),
(NULL,'2024-02-16 04:14:55.046153',46,'# 나도 일본'),
(NULL,'2024-02-16 04:14:55.041623',46,'# 여기 어딘데'),
(NULL,'2024-02-16 04:14:55.037970',46,'# 여기에 올리라고'),
(NULL,'2024-02-16 04:16:34.184211',47,'# 근육통'),
(NULL,'2024-02-16 04:16:34.187206',47,'# 스키'),
(NULL,'2024-02-16 04:16:34.190293',47,'# 집간다!'),
(NULL,'2024-02-16 04:19:06.674047',48,'#imwinter'),
(NULL,'2024-02-16 04:19:33.857648',49,'# 도파민'),
(NULL,'2024-02-16 04:19:33.848557',49,'# 빼는법을 몰라'),
(NULL,'2024-02-16 04:19:33.851541',49,'# 아 재밌어'),
(NULL,'2024-02-16 04:19:33.854249',49,'# 찜닭'),
(NULL,'2024-02-16 04:20:10.430942',50,'# 미륵사지'),
(NULL,'2024-02-16 04:20:10.428174',50,'# 왕궁'),
(NULL,'2024-02-16 04:20:10.434498',50,'# 익산'),
(NULL,'2024-02-16 04:21:13.790421',51,'#멍멍'),
(NULL,'2024-02-16 04:21:16.015510',52,'# 도파밍'),
(NULL,'2024-02-16 04:21:16.026664',52,'# 맥도날드'),
(NULL,'2024-02-16 04:21:16.020831',52,'# 신정문'),
(NULL,'2024-02-16 04:21:16.018172',52,'# 전북대'),
(NULL,'2024-02-16 04:21:16.023649',52,'# 젤리'),
(NULL,'2024-02-16 04:23:46.847117',53,'# 덕진공원'),
(NULL,'2024-02-16 04:23:46.853213',53,'# 도서관'),
(NULL,'2024-02-16 04:23:46.850305',53,'# 엄청 추워'),
(NULL,'2024-02-16 04:23:46.844434',53,'# 예쁨'),
(NULL,'2024-02-16 04:24:33.385985',54,'# 사람 진짜 없어'),
(NULL,'2024-02-16 04:24:33.394311',54,'# 야밤마을'),
(NULL,'2024-02-16 04:24:33.391367',54,'# 한옥마을'),
(NULL,'2024-02-16 04:24:33.388599',54,'# 한파'),
(NULL,'2024-02-16 04:26:28.893148',55,'# 귀여워'),
(NULL,'2024-02-16 04:26:28.910206',55,'# 빨간 목도리'),
(NULL,'2024-02-16 04:26:28.904905',55,'# 전북대'),
(NULL,'2024-02-16 04:26:28.917646',55,'# 표범'),
(NULL,'2024-02-16 04:27:12.484397',56,'# 맥반석 오징어'),
(NULL,'2024-02-16 04:27:12.473055',56,'# 어묵'),
(NULL,'2024-02-16 04:27:12.479020',56,'# 여기 휴게소는 공짜로 음식을 주네'),
(NULL,'2024-02-16 04:49:08.322908',57,'#siuuuuuuuuuuuuuuu');
/*!40000 ALTER TABLE `article_tag_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `article_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id` (`article_id`),
  KEY `FKbirsghsxk73gt60hqu92be2mq` (`user_id`),
  CONSTRAINT `FK5yx0uphgjc6ik6hb82kkw501y` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `FKbirsghsxk73gt60hqu92be2mq` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES
(1,'왁\n','2024-02-16 04:14:18.836567',NULL,NULL,30,1),
(2,'트리는 세그먼트 트리지\n','2024-02-16 04:14:34.448293',NULL,NULL,43,5),
(3,'여기 어디임??\n','2024-02-16 04:14:38.677860',NULL,NULL,43,1),
(4,'같이 가실래요?\n','2024-02-16 04:14:41.515925',NULL,NULL,21,1),
(5,'와 너무 가고 싶다\n','2024-02-16 04:14:47.479164',NULL,NULL,42,5),
(6,'나도 여기 가봤는데\n','2024-02-16 04:15:18.599088',NULL,NULL,22,1),
(7,'키 너무 큰 거 아니에요?\n','2024-02-16 04:15:21.145949',NULL,NULL,24,1),
(8,'웃는 모습 너무 멋있어요\n','2024-02-16 04:15:32.110964',NULL,NULL,21,1),
(9,'헐 나두,....\n','2024-02-16 04:15:40.798163',NULL,NULL,7,1),
(10,'yoat\n','2024-02-16 04:17:02.276427',NULL,NULL,36,5),
(11,'jejuuuuuuuuu\n','2024-02-16 04:17:18.424425',NULL,NULL,32,5),
(12,'언제나 두근거리는 공항\n','2024-02-16 04:17:30.242443',NULL,NULL,31,5),
(13,'크\n','2024-02-16 04:17:46.114259',NULL,NULL,30,5),
(14,'나무랑 잘 어울리네요\n','2024-02-16 04:18:51.702812',NULL,NULL,24,5),
(15,'차즈는 뽀삐\n','2024-02-16 04:21:39.073960',NULL,NULL,51,1),
(16,'어이 차현철 당신은 프론트야 백을 다시는 넘보지마\n','2024-02-16 04:22:00.074139',NULL,NULL,51,1),
(17,'키위키위\n','2024-02-16 04:22:55.201152',NULL,NULL,47,1),
(18,'백신 무럭무럭 자라라\n','2024-02-16 04:24:02.689935',NULL,NULL,25,2),
(19,'나도 데려가!\n','2024-02-16 04:28:24.880167',NULL,NULL,41,1),
(20,'입력합니다','2024-02-16 04:54:07.827141',NULL,2,43,1),
(21,'세개를','2024-02-16 04:54:08.205149',NULL,3,43,1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `constellation`
--

DROP TABLE IF EXISTS `constellation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `constellation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contour_id` bigint(20) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `hits` bigint(20) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `constellation`
--

LOCK TABLES `constellation` WRITE;
/*!40000 ALTER TABLE `constellation` DISABLE KEYS */;
INSERT INTO `constellation` VALUES
(1,1,'2024-02-16 03:22:57.607936',0,NULL,'아쿠아리움'),
(2,2,'2024-02-16 03:23:05.097214',0,NULL,'팬더'),
(3,3,'2024-02-16 03:26:15.861789',0,NULL,'노트북'),
(4,4,'2024-02-16 03:33:25.452109',0,NULL,'일본여행'),
(5,5,'2024-02-16 03:35:04.745303',0,NULL,'마이크'),
(6,6,'2024-02-16 03:36:07.963143',0,NULL,'SSAFY'),
(7,7,'2024-02-16 03:40:29.117515',0,NULL,'거미남자'),
(8,8,'2024-02-16 03:41:27.527156',0,NULL,'마리오'),
(9,9,'2024-02-16 03:44:39.545113',0,NULL,'강철남자'),
(10,10,'2024-02-16 03:46:15.387351',0,NULL,'단풍'),
(11,11,'2024-02-16 03:48:12.293257',0,NULL,'드림카'),
(12,12,'2024-02-16 03:50:15.142801',0,NULL,'chicken'),
(13,13,'2024-02-16 03:52:16.203144',0,NULL,'여행'),
(14,14,'2024-02-16 03:52:41.540839',0,NULL,'부산'),
(15,15,'2024-02-16 04:02:28.840433',0,NULL,'별'),
(16,16,'2024-02-16 04:06:29.647833',0,NULL,'축구'),
(17,17,'2024-02-16 04:11:12.364561',0,NULL,'김치'),
(18,18,'2024-02-16 04:19:23.341676',0,NULL,'우왕');
/*!40000 ALTER TABLE `constellation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `constellation_like`
--

DROP TABLE IF EXISTS `constellation_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `constellation_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `constellation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4e2kmasn0m2d1jo0ipt5k2gdw` (`constellation_id`),
  KEY `FKgxf887jsnj04m06atk2xj3yut` (`user_id`),
  CONSTRAINT `FK4e2kmasn0m2d1jo0ipt5k2gdw` FOREIGN KEY (`constellation_id`) REFERENCES `constellation` (`id`),
  CONSTRAINT `FKgxf887jsnj04m06atk2xj3yut` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `constellation_like`
--

LOCK TABLES `constellation_like` WRITE;
/*!40000 ALTER TABLE `constellation_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `constellation_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `constellation_user`
--

DROP TABLE IF EXISTS `constellation_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `constellation_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `constellation_user_role` enum('ADMIN','USER') DEFAULT NULL,
  `constellation_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdmi9517m7x8fx71hvbjgkccco` (`constellation_id`),
  KEY `FKh0ef0rydfefat2wp0jbo5v1j1` (`user_id`),
  CONSTRAINT `FKdmi9517m7x8fx71hvbjgkccco` FOREIGN KEY (`constellation_id`) REFERENCES `constellation` (`id`),
  CONSTRAINT `FKh0ef0rydfefat2wp0jbo5v1j1` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `constellation_user`
--

LOCK TABLES `constellation_user` WRITE;
/*!40000 ALTER TABLE `constellation_user` DISABLE KEYS */;
INSERT INTO `constellation_user` VALUES
(1,'ADMIN',1,1),
(2,'ADMIN',2,1),
(3,'ADMIN',3,1),
(4,'ADMIN',4,3),
(5,'ADMIN',5,1),
(6,'ADMIN',6,1),
(7,'ADMIN',7,1),
(8,'ADMIN',8,1),
(9,'ADMIN',9,1),
(10,'ADMIN',10,3),
(11,'ADMIN',11,1),
(12,'ADMIN',12,1),
(13,'ADMIN',13,1),
(14,'ADMIN',14,3),
(15,'ADMIN',15,5),
(16,'ADMIN',16,5),
(17,'ADMIN',17,5),
(18,'ADMIN',18,2);
/*!40000 ALTER TABLE `constellation_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follow`
--

DROP TABLE IF EXISTS `follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `follow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `accept_date` datetime(6) DEFAULT NULL,
  `request_date` datetime(6) DEFAULT NULL,
  `status` enum('REQUEST','ACCEPT','CANCEL','NOTHING') DEFAULT NULL,
  `from_user_id` bigint(20) DEFAULT NULL,
  `to_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6ub2ep3gp6gips8p2o8dq99ap` (`from_user_id`),
  KEY `FKtp5ueaac6c9kafdssohu97uev` (`to_user_id`),
  CONSTRAINT `FK6ub2ep3gp6gips8p2o8dq99ap` FOREIGN KEY (`from_user_id`) REFERENCES `user_account` (`id`),
  CONSTRAINT `FKtp5ueaac6c9kafdssohu97uev` FOREIGN KEY (`to_user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow`
--

LOCK TABLES `follow` WRITE;
/*!40000 ALTER TABLE `follow` DISABLE KEYS */;
INSERT INTO `follow` VALUES
(1,'2024-02-16 03:57:34.620424','2024-02-16 03:57:34.621199','ACCEPT',3,1),
(2,'2024-02-16 03:57:46.996193','2024-02-16 03:57:46.996510','ACCEPT',3,2),
(3,'2024-02-16 03:59:11.612499','2024-02-16 03:59:11.612836','ACCEPT',3,6),
(4,'2024-02-16 04:00:15.574158','2024-02-16 04:00:15.574471','ACCEPT',5,3),
(5,'2024-02-16 04:00:19.384082','2024-02-16 04:00:19.384435','ACCEPT',1,2),
(6,'2024-02-16 04:00:25.526237','2024-02-16 04:00:25.526656','ACCEPT',5,1),
(7,'2024-02-16 04:01:34.453854','2024-02-16 04:01:34.454167','ACCEPT',2,1),
(8,'2024-02-16 04:04:10.266884','2024-02-16 04:04:10.267219','ACCEPT',2,3),
(10,'2024-02-16 04:13:12.085338','2024-02-16 04:13:12.085875','ACCEPT',1,5),
(11,'2024-02-16 04:13:18.673786','2024-02-16 04:13:18.674227','ACCEPT',1,6),
(16,'2024-02-16 04:26:53.127040','2024-02-16 04:26:53.127502','ACCEPT',2,5),
(18,'2024-02-16 04:28:48.802114','2024-02-16 04:28:48.802617','ACCEPT',7,1),
(19,'2024-02-16 04:52:20.544735','2024-02-16 04:52:20.545430','ACCEPT',1,3);
/*!40000 ALTER TABLE `follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image_type` enum('ARTICLE','CONSTELLATION','PROFILE') NOT NULL,
  `name` varchar(255) NOT NULL,
  `thumbnail_url` varchar(512) DEFAULT NULL,
  `url` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES
(1,'PROFILE','gettyimages-jv11340318-580x387.jpg',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/profiles/439be1b4-9498-4518-9e37-ec9397f89243.jpg'),
(2,'PROFILE','KakaoTalk_20240216_032506779_08.jpg',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/profiles/ad9a7a76-022b-4c46-91b9-eb886dc872f4.jpg'),
(3,'CONSTELLATION','고래1.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/d298dc26-901d-4a7e-abc5-f48ab2825cc4.png'),
(4,'CONSTELLATION','고래1.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/1ca7f863-5156-44f7-b36a-5f4881a8ca2d.png'),
(5,'CONSTELLATION','고래1.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/486d6084-90a7-4a98-98c6-d7aa5e29191f.png'),
(6,'CONSTELLATION','XL.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/3815e425-17ca-4978-9364-356e006f69c9.png'),
(7,'CONSTELLATION','XL.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/65a4262e-5cbd-4d2e-9ba7-59ebd5eac441.png'),
(8,'CONSTELLATION','XL.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/14295397-bd00-447e-8a82-38c04af7ff0e.png'),
(9,'CONSTELLATION','computer.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/2eb15d2a-3d99-4332-a14d-c56ad8affa72.png'),
(10,'CONSTELLATION','computer.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/6bf3dc27-6c9e-43d6-8964-9a933e4991f7.png'),
(11,'CONSTELLATION','computer.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/b072d029-7ef3-467b-aaa5-51210db57f24.png'),
(12,'ARTICLE','KakaoTalk_20240216_032659354.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/611127d6-5051-4d1a-9622-05d2badd2ece.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/d9ea0f50-20f5-4e0e-8f9a-ae065a98a7ef.png'),
(13,'ARTICLE','computer.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/1537e687-b502-49ab-9051-46366e8c7919.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/7bcf4ef9-629d-48b1-b2c8-e24d5f0671e6.png'),
(14,'ARTICLE','KakaoTalk_20240216_032506779_03.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/e5e2b846-abb1-4469-9adb-8edac833d6d4.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/ad05022f-3266-4e99-a3f1-0bd509869511.png'),
(15,'CONSTELLATION','KakaoTalk_20240216_032506779_03.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/f1449fc0-8a40-4244-bc04-9b244a34a133.png'),
(16,'CONSTELLATION','KakaoTalk_20240216_032506779_03.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/ab2f6065-7fea-457c-b705-050ec2f475a4.png'),
(17,'CONSTELLATION','KakaoTalk_20240216_032506779_03.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/6ba5b4bc-9a41-47d5-9f43-0269b833cddf.png'),
(18,'ARTICLE','KakaoTalk_20240216_032506779_02.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/97fd9b41-6ea1-44c8-9464-acee5bcfa501.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/31da15e7-bc02-4f41-a1c8-f6ff4343990f.png'),
(19,'CONSTELLATION','mic.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/81ab75b6-06a5-43a7-a271-2d6d121282bc.png'),
(20,'CONSTELLATION','mic.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/6abbbd2f-de01-4f47-9189-0dd11730f995.png'),
(21,'CONSTELLATION','mic.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/b1417383-d9e2-4015-be0f-c69d89903ab9.png'),
(22,'ARTICLE','KakaoTalk_20240216_032506779_04.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/12c95068-ad85-4614-bd72-645b3c7826e4.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/2906b423-03b6-459e-a64f-1d017b65229b.png'),
(23,'CONSTELLATION','6XZE2BY8FOQN6O9MYNQB.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/7c579f5d-4873-42ab-abbf-e14cf41549c8.png'),
(24,'CONSTELLATION','6XZE2BY8FOQN6O9MYNQB.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/63efe73f-9e69-4545-8ac2-8ce8fd710b57.png'),
(25,'CONSTELLATION','6XZE2BY8FOQN6O9MYNQB.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/1d6c2d66-6308-4b74-8741-ecb53fd257b5.png'),
(26,'ARTICLE','KakaoTalk_20240216_032506779_08.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/68ee9c85-1254-49c7-9ec7-848e947ea6ac.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/46a2d35f-5e40-48e3-8df3-a245c52cfdb5.png'),
(27,'ARTICLE','KakaoTalk_20240216_032506779_07.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/ae2f111d-38b3-45c5-ab31-1ab6dd1baa5b.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/60d7e605-7164-404f-b6af-aa9974b8fe03.png'),
(28,'ARTICLE','iu.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/ee45fd62-ef5f-4769-ac3f-578483fff247.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/016604fd-4d5e-4baa-8326-c32fafb2e107.png'),
(29,'ARTICLE','KakaoTalk_20240216_032506779_06.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/39caec57-254e-410a-aa2b-16c9fbd1eecb.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/49526f1c-cd8b-4775-afe2-f9ee5aaa9f16.png'),
(30,'ARTICLE','psy.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/513a1a50-28ce-4ed1-aa40-d12c3eb29108.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/4bf76713-ee34-4615-a463-4b125e70204a.png'),
(31,'ARTICLE','KakaoTalk_20240216_032659354_01.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/0024ab05-9cbd-47b6-b06a-08c23f5b8ce5.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/c0f2ab8a-0592-4ba3-9a50-4c38150b5ac5.png'),
(32,'ARTICLE','rain.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/fc2e4407-5783-4319-a4fa-3f32566f418a.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/bc8230dc-0817-4cf9-bad0-2e7806c844e8.png'),
(33,'ARTICLE','KakaoTalk_20240216_032659354_03.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/acb66f77-aa08-4b6f-a643-78d2126e304a.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/eb7cacf2-544e-490b-8e9d-b7dee3862cd0.png'),
(34,'CONSTELLATION','-M6BoZmo2igm6JJaRJXJzpRPQQ9yQSJny-wzqQGxAnW4ymhuFuN8ag0t447LJKJ_MxSnOjNbym5E5CgQ_RXGhg.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/3337bf35-99ce-4221-b04c-06aef6553611.png'),
(35,'CONSTELLATION','-M6BoZmo2igm6JJaRJXJzpRPQQ9yQSJny-wzqQGxAnW4ymhuFuN8ag0t447LJKJ_MxSnOjNbym5E5CgQ_RXGhg.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/cf0c8c83-a94a-430c-8fe8-1ee5cf92baca.png'),
(36,'CONSTELLATION','-M6BoZmo2igm6JJaRJXJzpRPQQ9yQSJny-wzqQGxAnW4ymhuFuN8ag0t447LJKJ_MxSnOjNbym5E5CgQ_RXGhg.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/f44ed52e-bcad-49f7-af5c-7072bde8e3c8.png'),
(37,'ARTICLE','120479343.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/28a946e7-dc75-4dee-92ca-fa7b7a565ad6.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/e787603a-6c87-4f57-8d4b-12f84e809c98.png'),
(38,'CONSTELLATION','mario.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/5365aec4-2ebb-4f8c-8b5b-9b2517b27dd2.png'),
(39,'CONSTELLATION','mario.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/f441b6c0-005e-443c-8c73-43610059c172.png'),
(40,'CONSTELLATION','mario.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/a75604a7-9065-4357-9bc0-f45bad5407d5.png'),
(41,'ARTICLE','1685519005341_0.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/0a701f1e-9c90-4ae1-84dc-50b467a96838.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/e816df1e-7e65-4d32-97f8-7e8e9756a180.png'),
(42,'ARTICLE','20230416010808.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/554c9e2e-fb57-49ed-ba94-35434658cd4d.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/716e5390-9d9f-4986-b321-0376750dc95f.png'),
(43,'ARTICLE','ED91B8EBB094EC98A4_EC97B0ED95A9EB89B4EC8AA4.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/994ac0d9-e47a-43a1-aaf6-ad1dbac273ea.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/4641b610-8b1c-4f96-af66-1925d32774bd.png'),
(44,'ARTICLE','NISI20230824_0020007545_web.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/1a8adaca-0f74-4e6c-b754-50e4ac49cb21.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/b88bd884-2143-40f8-8614-98a3b269ec57.png'),
(45,'CONSTELLATION','iron1.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/c5203079-fc97-4484-b112-1c62e0052da4.png'),
(46,'CONSTELLATION','iron1.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/07f79898-2fa2-4fb3-b25c-7736218ee49e.png'),
(47,'CONSTELLATION','iron1.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/7caf1410-9dc0-4764-b145-205c6942d793.png'),
(48,'ARTICLE','아1.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/23ec87db-f343-4168-b5c6-53ade98698fd.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/a57aaf57-3ffc-4016-834f-79f2c63069a2.png'),
(49,'ARTICLE','SSC_20230824141045.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/89092783-f6e8-4053-9385-a5de2e4a4f57.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/04f9082f-013a-4be8-92f1-6fd5ec487831.png'),
(50,'CONSTELLATION','KakaoTalk_20240216_032904604.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/c6a8dfe2-d5cb-48c8-bf96-89bdf704cd56.png'),
(51,'CONSTELLATION','KakaoTalk_20240216_032904604.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/0cd72b93-fc7b-42f3-a669-3a84a4f65499.png'),
(52,'CONSTELLATION','KakaoTalk_20240216_032904604.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/3de26004-648b-4d89-9be7-188e6a3b5cef.png'),
(53,'ARTICLE','KakaoTalk_20240216_032904604_02.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/82744406-5cfd-41f7-91e0-edf6b6408379.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/4cd982ad-aa99-4e1e-964a-98743657e1af.png'),
(54,'ARTICLE','KakaoTalk_20240216_032904604_01.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/ef471ffe-91a9-45ac-acef-c403777611d4.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/4edb619a-84c1-4445-8467-2e79d6fc9bae.png'),
(55,'ARTICLE','다운로드.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/d805e0ac-9226-4180-9da2-10d3cf046d60.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/ffed0c31-41e2-411a-ad97-058c7e5b5d7a.png'),
(56,'ARTICLE','KakaoTalk_20240216_032904604_03.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/85c04c10-72a4-4a7a-992d-79614629fcc5.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/ba24171b-6ea2-4128-92b8-aeaa38fff51e.png'),
(57,'CONSTELLATION','22564_72789_5839.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/26b7d507-df02-48cb-8261-8049fcaaec1f.png'),
(58,'CONSTELLATION','22564_72789_5839.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/63a1ed00-546b-41ba-a898-baabc3991b47.png'),
(59,'CONSTELLATION','22564_72789_5839.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/10eaf9ab-cad7-4e30-ae43-56dd57a5b8b4.png'),
(60,'ARTICLE','KakaoTalk_20240216_032812685.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/709cf5c2-d397-43a7-8e88-1b80ce0b8d93.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/4f9cb65c-60b6-4bc1-9df1-b10b9354d049.png'),
(61,'CONSTELLATION','chicken.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/07ffdb8b-1fd4-4d13-a30c-1723cb6d44e8.png'),
(62,'CONSTELLATION','chicken.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/9ef7d1a7-a6fc-4b85-95c3-cae8ccebe0eb.png'),
(63,'CONSTELLATION','chicken.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/851be07d-a4d7-496f-b755-2be1213e896f.png'),
(64,'ARTICLE','chicken1.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/5ad206eb-ff0e-4557-b31f-bad0bc025492.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/5a2b45b9-c65d-49b9-9975-47364fffedfe.png'),
(65,'ARTICLE','1971_3293_2637.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/b390cab8-554e-49b0-b20b-1cda9b23a5fd.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/a815ea88-319a-4006-ac0f-cf0b18591977.png'),
(66,'CONSTELLATION','비행기.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/d253f39b-8b7d-4c7c-8b92-758a563ee068.png'),
(67,'CONSTELLATION','비행기.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/2111e23f-6baa-4ad1-ac36-86f1388d96ec.png'),
(68,'CONSTELLATION','비행기.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/31884e2d-4389-496c-9cd7-61d61247bd7f.png'),
(69,'CONSTELLATION','KakaoTalk_20240216_033008825_06.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/102febbc-af97-4cfc-9efe-677f8073cb0d.png'),
(70,'CONSTELLATION','KakaoTalk_20240216_033008825_06.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/802bb923-7380-4343-b956-b35f8c2fa40b.png'),
(71,'CONSTELLATION','KakaoTalk_20240216_033008825_06.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/8da7ffee-70ff-4eb5-a57e-38b3148df71c.png'),
(72,'ARTICLE','KakaoTalk_20240216_033008825_04.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/57ca2147-b263-44b2-ae5b-80c541049b76.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/f0e4dd61-4555-436c-811c-c95400eb3fca.png'),
(73,'ARTICLE','KakaoTalk_20240216_033008825_01.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/19852155-3f8a-467a-bf75-89f1d4914c6c.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/5857ae5c-2a3d-4bf7-8695-e0a902b034a2.png'),
(74,'ARTICLE','KakaoTalk_20240216_033008825.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/d2f10208-8852-4a16-9ab1-7b4d45cf3f91.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/2960e19f-04ca-43f0-bd56-4c32b1aa7298.png'),
(75,'ARTICLE','인천공항.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/6177cc99-6571-4310-a472-ad4e96acd31b.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/c5c0846a-1800-4be7-a679-0ebe36b5f57c.png'),
(76,'ARTICLE','798b8da0-9b83-4ef4-87a6-3eba89e563a5.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/3516b5bd-32f0-41fe-8ba5-f331c060ef9b.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/79bb0fda-84a9-4a00-b48d-d6c751523547.png'),
(77,'CONSTELLATION','star.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/29e042b5-1da6-4a81-8b8f-4ae8aaad1dc0.png'),
(78,'CONSTELLATION','star.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/a807580a-6a3f-45d2-a4e0-cb07547e52fa.png'),
(79,'CONSTELLATION','star.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/eb41065b-7958-4a25-a6a3-dd07b8f418d4.png'),
(80,'ARTICLE','star3.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/f5013445-f18a-4bb1-b4b7-bca316aae94f.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/41f534ec-80d8-42a2-ac1d-799eb3d89508.png'),
(81,'ARTICLE','KakaoTalk_20240215_224939054_06.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/3efbf92c-9ffe-46a5-aaa4-0092d55d1be4.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/6b1d6f97-70a4-440d-821d-6b5395c7dec5.png'),
(82,'ARTICLE','star.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/91d578bc-a837-4316-af24-98d929603fa9.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/c42699e6-82f2-406f-8266-7e574dcc6c11.png'),
(83,'ARTICLE','KakaoTalk_20240216_033008825_05.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/ab7b1374-eb36-4e27-b8a2-b51d65d31dcb.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/c099ee5a-a764-40e8-81a1-3aad91647ddb.png'),
(84,'ARTICLE','star2.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/98cc2328-2110-4450-8735-bd5eec69874d.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/848ef9fa-8f6c-4395-850f-65e10f94d870.png'),
(85,'ARTICLE','KakaoTalk_20240215_224939054_04.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/9872fe34-7024-4774-970f-993deb296b86.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/d4e86ee9-28ef-4d26-8878-240e8ddd985d.png'),
(86,'ARTICLE','everland.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/9ce542fe-bc72-493d-85a5-4ea167183354.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/6a5c0bb3-10af-44a1-9867-40d8c69856b0.png'),
(87,'CONSTELLATION','soccer.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/a2cfe1e2-a566-49e4-82ae-f71c09d23d8d.png'),
(88,'CONSTELLATION','soccer.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/8e794efc-c1a5-4262-a3da-f7141cde4778.png'),
(89,'CONSTELLATION','soccer.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/0763bf2f-0fff-4b81-9b42-fcd4bde3c2e6.png'),
(90,'ARTICLE','KakaoTalk_20240215_224939054_05.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/32527b88-0532-4b93-8e5a-3b8a2e247076.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/4d66ee05-6c8a-4fb5-8c32-10e5196b2b2d.png'),
(91,'ARTICLE','KakaoTalk_20240216_032506779_05.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/358975d0-0ab9-4f93-84b1-c2d385246a66.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/cd144b34-47e3-449f-bc67-58414abfd656.png'),
(92,'ARTICLE','세부섬.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/d516260c-0b96-4235-a755-4e821247bdbd.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/1f6e5571-12f6-41c7-b50c-5d9436a4b809.png'),
(93,'ARTICLE','KakaoTalk_20240216_032812685_06.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/44d2ab98-6f9a-4822-bcbf-1a64954a02e1.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/691ecaff-ddd8-4b6c-bbff-d7cea4f8b33e.png'),
(94,'CONSTELLATION','kimchi.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/457718d8-b56a-4fdb-817c-e52ac8e3aaa7.png'),
(95,'CONSTELLATION','kimchi.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/77eb7761-54b0-4cbb-91d3-d99d318b7686.png'),
(96,'CONSTELLATION','kimchi.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/8eae6612-9a97-45fb-bfa8-d50437808f2f.png'),
(97,'ARTICLE','삿포르.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/800e6f0a-2fb9-4196-8f89-489907af48f6.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/d6e509fc-a902-4043-95be-d9eadb57eb0b.png'),
(98,'ARTICLE','도톤보리.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/3aaf2241-2338-4bf5-9202-333ddc616318.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/c455042b-b222-4a07-89cd-4b8c0791dd2c.png'),
(99,'ARTICLE','민우형일본여행.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/610ebbf0-4b85-447a-9d76-a40bcf54af17.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/59428fd7-01ac-429c-9c3c-cda99be17be9.png'),
(100,'ARTICLE','KakaoTalk_20240215_224939054_03.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/54e1114b-4e3c-4945-a7cd-fcc27f1d68f3.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/de201a3e-499a-4921-95fa-46c7701d410e.png'),
(101,'ARTICLE','원터.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/30e91f52-903a-4721-8147-7b88651ead2a.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/2c5ac679-d6b9-42ef-ad24-a6b59eba6191.png'),
(102,'CONSTELLATION','20231207_040442.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/origin/4d2f7cd6-33eb-48bb-a3e1-c6d3550d72a5.png'),
(103,'CONSTELLATION','20231207_040442.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/thumb/a3279e24-6d8e-488f-b99f-97684338fe74.png'),
(104,'CONSTELLATION','20231207_040442.png',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/constellation/cthumb/65634ce9-7e0c-4689-95b6-a857c32dfa9e.png'),
(105,'ARTICLE','도파밍안동.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/266dd021-2efd-4665-bb31-0928a95dc727.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/e38fc3f8-5569-4a38-b9f9-64674911c870.png'),
(106,'ARTICLE','미륵사지.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/e16051f9-25cd-44be-9d0c-6918214a993c.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/0f265f76-6c01-4b48-a15e-eaeb1b594628.png'),
(107,'ARTICLE','다운로드 (4).png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/77a3b211-baf0-40be-adc7-3538615f2c0e.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/92a71a1b-0ed3-4cbd-8263-4c5c925feabe.png'),
(108,'ARTICLE','전북대.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/ff3cebaf-2bdb-4e89-97be-574be0263d74.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/f18293ce-3525-473a-92ab-3c161738fa2b.png'),
(109,'ARTICLE','도서관.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/5302bf07-8a45-41ac-a461-84f8d01169e3.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/56e05fa2-004a-4cdf-80d6-c5a5d90cf8b3.png'),
(110,'ARTICLE','한옥마을.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/23cbfd04-f53d-4484-a0be-f138f3c4835b.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/dcfd30d5-6295-4350-b36f-593d9ae89400.png'),
(111,'ARTICLE','전북대 동물.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/47509c54-da5b-4eb9-91fe-8d5434c92d85.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/e689b7e7-ba43-4896-bee6-34ca47156d96.png'),
(112,'ARTICLE','휴게소.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/66173924-6930-4ecb-8d3e-00dad310ea84.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/e6b61a00-04b2-4035-8127-ccd4426786a1.png'),
(113,'PROFILE','원터.JPG',NULL,'https://byeoldam.s3.ap-northeast-2.amazonaws.com/profiles/2a6670af-633c-468e-ae57-e17c9ed68d95.JPG'),
(114,'ARTICLE','sports.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/thumbnails/7808a1dd-d061-4756-b40b-b335bb6b317c.png','https://byeoldam.s3.ap-northeast-2.amazonaws.com/articles/904623c1-0bf3-47ec-9884-e209fb95db71.png');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `birthday` date DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `disclosure_type` enum('VISIBLE','INVISIBLE') NOT NULL,
  `email` varchar(255) NOT NULL,
  `memo` varchar(512) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `nickname` varchar(32) NOT NULL,
  `password` varchar(255) NOT NULL,
  `provider_type` enum('GOOGLE','KAKAO','NAVER','LOCAL') DEFAULT NULL,
  `role_type` enum('USER','ADMIN') DEFAULT NULL,
  `image` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hl02wv5hym99ys465woijmfib` (`email`),
  UNIQUE KEY `UK_d5s80v1ic05f862oo5hs0rw8q` (`nickname`),
  UNIQUE KEY `UK_5f2550weemt98iyv3xoc3yarh` (`image`),
  CONSTRAINT `FK3xqe8al90ako7hrpxl294fnpv` FOREIGN KEY (`image`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES
(1,'1997-09-01','2024-02-16 02:57:41.327524',NULL,'VISIBLE','lmw@naver.com','안녕 반가워','2024-02-16 03:19:20.033203','이민우','immigrant_co','$2a$10$fqlbjg6lerkQx99r.mBxpOyqMMGRDTD6hhz6U8ii9YvDK9zJZHkPO','LOCAL','USER',1),
(2,NULL,'2024-02-16 03:17:30.262235',NULL,'VISIBLE','ckguscjf03@gmail.com',NULL,'2024-02-16 03:57:02.281787','차현철','bright','$2a$10$nVf16NQVRo/fXqZXVkDQFutPcnZ8GgxDin/ibgyCAZaTdj74.2lFG','GOOGLE','USER',NULL),
(3,'2019-07-16','2024-02-16 03:20:47.082474',NULL,'VISIBLE','lmw7414@naver.com','파워블로거 미스터리','2024-02-16 03:21:39.797978','이민우','mysterlee','$2a$10$OkYgDoEifsQ1sk3u.GfEgOUqQzdso1p4VL7i1bhn4Hl6tdjfNXiEe','NAVER','USER',2),
(4,NULL,'2024-02-16 03:28:20.212202',NULL,'VISIBLE','sini3181@gmail.com',NULL,NULL,'황정민','sini31812820209','$2a$10$.YqeAxzFQhlnrIUkQDDWfehcxHx0YRV1rhDPAkYYhgrNQlCjaWwdS','GOOGLE','USER',NULL),
(5,NULL,'2024-02-16 03:53:48.261734',NULL,'VISIBLE','chang2739@gmail.com',NULL,'2024-02-16 03:58:48.141456','이창헌','chang','$2a$10$Ly0QG9XNEZstSpp1az7DBOYiZYkjXJFK.0ArmfxRstFmcXc1s8VPa','GOOGLE','USER',NULL),
(6,NULL,'2024-02-16 03:57:46.166016',NULL,'VISIBLE','rlatngur1101@gmail.com','내가 돌아왔다','2024-02-16 03:58:40.910136','김수혁','hoorooke','$2a$10$PIYSLHMCl6v25oEhm4S2huzpkWjYuKY/h/1XUIfl6Y7TZWkSaqX6a','GOOGLE','USER',NULL),
(7,NULL,'2024-02-16 04:26:35.447809',NULL,'VISIBLE','lmw741469@gmail.com','홀리몰리과카몰리','2024-02-16 04:27:15.208828','이민우','moly','$2a$10$65DU3TdQb.dua8g4paJCjumyBmQ6H7exRnUp.KbAQ4WNiJo1DvQS6','GOOGLE','USER',113),
(8,NULL,'2024-02-16 05:14:06.563316',NULL,'VISIBLE','sini3181@naver.com',NULL,NULL,'황정민','hwang','$2a$10$wv3bwUr7WPtlF9oJSAzGm.btgx1WnmhRtDcxefpViaonrp9xob/d.','LOCAL','USER',NULL);
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_refresh_token`
--

DROP TABLE IF EXISTS `user_refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_refresh_token` (
  `refresh_token_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `refresh_token` varchar(256) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`refresh_token_seq`),
  UNIQUE KEY `UK_qca3mjxv5a1egwmn4wnbplfkt` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_refresh_token`
--

LOCK TABLES `user_refresh_token` WRITE;
/*!40000 ALTER TABLE `user_refresh_token` DISABLE KEYS */;
INSERT INTO `user_refresh_token` VALUES
(1,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MlMxRTgzRkdINVM2MTFINVMzNTFHNVMxM0YyMUFERjMiLCJpYXQiOjE3MDgwMjA5OTUsImV4cCI6MTcwODYyNTc5NX0.CUr1-Yg8dUMHPOm-YAFbXQex2v063wCfILMooJseMu0','lmw@naver.com'),
(2,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MlMxRTgzRkdINVM2MTFINVMzNTFHNVMxM0YyMUFERjMiLCJpYXQiOjE3MDgwMjEwNTAsImV4cCI6MTcwODAyMjg1MH0.zMB2-7sMV5GhJfmAaE9clFIqLy1hgzZ4ojHlr-OSYbQ','ckguscjf03@gmail.com'),
(3,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MlMxRTgzRkdINVM2MTFINVMzNTFHNVMxM0YyMUFERjMiLCJpYXQiOjE3MDgwMjEyNDcsImV4cCI6MTcwODAyMzA0N30.ox7810h8H8HLBZ953gMV4oPgJGsPXJGoXUmz9fIDbHE','lmw7414@naver.com'),
(4,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MlMxRTgzRkdINVM2MTFINVMzNTFHNVMxM0YyMUFERjMiLCJpYXQiOjE3MDgwMjE3MDAsImV4cCI6MTcwODAyMzUwMH0.l-f7L3BptuBG7_-2g_IiDsB-BDpXacsODphUN-1RVMY','sini3181@gmail.com'),
(5,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MlMxRTgzRkdINVM2MTFINVMzNTFHNVMxM0YyMUFERjMiLCJpYXQiOjE3MDgwMjMyMjgsImV4cCI6MTcwODAyNTAyOH0.ydoN9aQpbplwJsC3Bihk6TEV997EAUCdtiVR5sqngrg','chang2739@gmail.com'),
(6,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MlMxRTgzRkdINVM2MTFINVMzNTFHNVMxM0YyMUFERjMiLCJpYXQiOjE3MDgwMjM0NjYsImV4cCI6MTcwODAyNTI2Nn0.LOc9iJ03mObJtz62fRfnhaf_mXwSAk1iXqkp7WqIvFw','rlatngur1101@gmail.com'),
(7,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MlMxRTgzRkdINVM2MTFINVMzNTFHNVMxM0YyMUFERjMiLCJpYXQiOjE3MDgwMjUxOTUsImV4cCI6MTcwODAzMjM5NX0.uGxwJ91vr0tplcbQ9ULKi70n6lBIDu8ybWj0CGrRbd8','lmw741469@gmail.com'),
(8,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MlMxRTgzRkdINVM2MTFINVMzNTFHNVMxM0YyMUFERjMiLCJpYXQiOjE3MDgwMjgwNTYsImV4cCI6MTcwODYzMjg1Nn0.fmaOFEgXdauIDIFjbMP77TY8yIvbz1Ouk23c73XQl8c','sini3181@naver.com');
/*!40000 ALTER TABLE `user_refresh_token` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-16  5:14:25
