-- phpMyAdmin SQL Dump
-- version 3.5.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 06, 2015 at 10:34 AM
-- Server version: 5.5.29
-- PHP Version: 5.4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `db_storefinder`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_storefinder_authentication`
--

CREATE TABLE `tbl_storefinder_authentication` (
  `authentication_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text CHARACTER SET utf8 NOT NULL,
  `password` text CHARACTER SET utf8 NOT NULL,
  `name` text CHARACTER SET utf8 NOT NULL,
  `role_id` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  `deny_access` int(11) NOT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tbl_storefinder_authentication`
--

INSERT INTO `tbl_storefinder_authentication` (`authentication_id`, `username`, `password`, `name`, `role_id`, `is_deleted`, `deny_access`) VALUES
(1, 'admin', '3dba44de6dbf6ad13444799ed798f5b8', 'Admin', 1, 0, 0),
(2, 'guest', '25d55ad283aa400af464c76d713c07ad', 'Guest', 2, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_storefinder_categories`
--

CREATE TABLE `tbl_storefinder_categories` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category` text CHARACTER SET utf8 NOT NULL,
  `category_icon` text CHARACTER SET utf8 NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `tbl_storefinder_categories`
--

INSERT INTO `tbl_storefinder_categories` (`category_id`, `category`, `category_icon`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1, 'Coffee Shop', 'http://localhost/personal/storefinder/upload_pic/icon_1403627684.png', 1403283079, 1403961634, 0),
(2, 'Grocery Shop', 'http://localhost/personal/storefinder/upload_pic/icon_1403627691.png', 1403283093, 1403627691, 0),
(3, 'Restobar', 'http://localhost/personal/storefinder/upload_pic/icon_1404012154.png', 1404012154, 1404012154, 0),
(4, 'Restaurants', 'http://localhost/personal/storefinder/upload_pic/icon_1404147515.png', 1404147515, 1404147515, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_storefinder_news`
--

CREATE TABLE `tbl_storefinder_news` (
  `news_id` int(11) NOT NULL AUTO_INCREMENT,
  `news_content` text CHARACTER SET utf8 NOT NULL,
  `news_title` text CHARACTER SET utf8 NOT NULL,
  `news_url` text CHARACTER SET utf8 NOT NULL,
  `photo_url` text CHARACTER SET utf8 NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`news_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tbl_storefinder_news`
--

INSERT INTO `tbl_storefinder_news` (`news_id`, `news_content`, `news_title`, `news_url`, `photo_url`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1, 'Apparently, there’s still life in the old social video dog yet. FightMe, an iOS app that lets you challenge others by recording and sharing 30 second-long videos, has raised a further, modest seed round of funding.\\nNew backing comes from VC firm HTG Ventures, Daniel and Raphael Khalili (who also invested in Yahoo-acquired Summly), and David Reuben Junior of Reuben Brothers, who, together, have put $1.35 million into the London-based started. This adds to the $500,000 FightMe raised back in October 2013.\\nDescribed as a “social video network” designed to showcase any talent or opinion, the iOS app (with Android pegged to follow) lets you post your own 30-second, unedited videos, and add appropriate hashtags so that others can browse and discover your videos. They can then choose to respond with a similar themed video, as well as follow you, or share your video. &quot;asdasdasd&quot;', 'FightMe, An App That Lets You Challenge Others Through Video, Picks Up $1.35M In New Backing', 'http://techcrunch.com/2014/06/12/fightme-again/', 'http://tctechcrunch2011.files.wordpress.com/2014/06/screen-shot-fightme-singing.png?w=738', 1418883119, 1419349925, 0),
(2, 'There have been a lot of murmurs that Amazon would turn on a new music streaming service this week, and it looks like that’s just what it quietly did a little while ago. A link to Amazon Prime Music, if you are an Amazon Prime subscriber that is, now takes you to a page heavy on playlists, promising over 1 million songs ready for your streaming pleasure, on top of the downloading service that Amazon already offered. And Amazon has also relaunched its mobile app on iOS with a new name: it’s now called Amazon Music instead of Amazon Cloud Player, which now also features access to Amazon Prime Music.\\nBut, unless Amazon is going to add more features before an official unveiling, I’m not sure what we’re seeing right now is quite a Spotify killer. The service as it is right now appears to be missing the most current releases; and you have to go through several steps, including adding music to your library, before you can actually stream tracks. On a search results page, what you still get is the option to listen to an excerpt of a track.', 'Amazon Turns On Prime Music Streaming, Sans Current Hits', 'http://techcrunch.com/2014/06/11/amazon-turns-on-prime-music-streaming-sans-current-hits/', 'http://localhost/personal/storefinder/upload_pic/news_1404012277.png', 1404012277, 1404012277, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_storefinder_photos`
--

CREATE TABLE `tbl_storefinder_photos` (
  `photo_id` int(11) NOT NULL AUTO_INCREMENT,
  `photo_url` text CHARACTER SET utf8 NOT NULL,
  `thumb_url` text CHARACTER SET utf8 NOT NULL,
  `store_id` int(11) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `tbl_storefinder_photos`
--

INSERT INTO `tbl_storefinder_photos` (`photo_id`, `photo_url`, `thumb_url`, `store_id`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1, 'http://lacanvas.com/wp-content/uploads/2015/08/Philz-720x438.jpg', 'http://lacanvas.com/wp-content/uploads/2015/08/Philz-720x438.jpg', 2, 1403283256, 0, 0),
(2, 'http://trikeenan.com/assets/starbucks-after-front2.jpg', 'http://asiasociety.org/files/121024_starbucks_blog.jpg', 1, 1403283305, 0, 0),
(3, 'http://www.eonline.com/eol_images/Entire_Site/2013429/rs_1024x759-130529144218-1024.Grocery.mh.052913.jpg', 'http://rdujour.com/wp-content/uploads/2013/09/The-Grocery-Store-Presidio-Heights-San-Francisco.jpg', 3, 1403289572, 1403289572, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_storefinder_ratings`
--

CREATE TABLE `tbl_storefinder_ratings` (
  `rating_id` int(11) NOT NULL AUTO_INCREMENT,
  `rating` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  PRIMARY KEY (`rating_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `tbl_storefinder_ratings`
--

INSERT INTO `tbl_storefinder_ratings` (`rating_id`, `rating`, `user_id`, `created_at`, `updated_at`, `store_id`) VALUES
(1, 4, 1, 1403286618, 1403286618, 1),
(2, 5, 1, 1403289597, 1403289597, 2),
(3, 4, 1, 1403292062, 1403292062, 3),
(4, 3, 4, 1404015472, 1404015472, 3);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_storefinder_reviews`
--

CREATE TABLE `tbl_storefinder_reviews` (
  `review_id` int(11) NOT NULL AUTO_INCREMENT,
  `review` text CHARACTER SET utf8 NOT NULL,
  `store_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`review_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `tbl_storefinder_reviews`
--

INSERT INTO `tbl_storefinder_reviews` (`review_id`, `review`, `store_id`, `user_id`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1, 'I love this place a lot of people coming in and out. I love the cozy place and the staff too. Cheers!', 1, 1, 1403289952, 1403289952, 0),
(2, 'I love it!', 1, 1, 1403292040, 1403292040, 0),
(3, 'Cool and awesome place!', 1, 1, 1403622188, 1403622188, 0),
(4, 'Wanting to Go back but damn it is cool than the other.', 1, 1, 1403622206, 1403622206, 0),
(5, 'Loving this place and it is awesome!', 1, 1, 1403622297, 1403622297, 0),
(6, 'Yuhoo. Hey Mate!', 1, 1, 1403622310, 1403622310, 0),
(7, 'This is crazy Cool!', 1, 1, 1403622329, 1403622329, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_storefinder_stores`
--

CREATE TABLE `tbl_storefinder_stores` (
  `store_id` int(11) NOT NULL AUTO_INCREMENT,
  `store_name` text CHARACTER SET utf8 NOT NULL,
  `store_address` text CHARACTER SET utf8 NOT NULL,
  `store_desc` text CHARACTER SET utf8 NOT NULL,
  `lat` varchar(255) CHARACTER SET utf8 NOT NULL,
  `lon` varchar(255) CHARACTER SET utf8 NOT NULL,
  `sms_no` varchar(255) CHARACTER SET utf8 NOT NULL,
  `phone_no` varchar(255) CHARACTER SET utf8 NOT NULL,
  `email` varchar(255) CHARACTER SET utf8 NOT NULL,
  `website` varchar(255) CHARACTER SET utf8 NOT NULL,
  `category_id` int(11) NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `featured` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `tbl_storefinder_stores`
--

INSERT INTO `tbl_storefinder_stores` (`store_id`, `store_name`, `store_address`, `store_desc`, `lat`, `lon`, `sms_no`, `phone_no`, `email`, `website`, `category_id`, `created_at`, `updated_at`, `featured`, `is_deleted`) VALUES
(1, 'Starbucks Coffee', '3595 California St San Francisco, CA 94118', 'Our Company\\nTo say Starbucks purchases and roasts high-quality whole bean coffees is very true. That’s the essence of what we do – but it hardly tells the whol &quot;asd&quot;', '37.786262', '-122.453143', '+123 456 7897', '(415) 387-2249', '', 'www.starbucks.com', 1, 1418798093, 1426774745, 1, 0),
(2, 'Philz Coffee', '748 Van Ness Ave San Francisco, CA 94102', 'Our Company\\nTo say Starbucks purchases and roasts high-quality whole bean coffees is very true. That’s the essence of what we do – but it hardly tells the whole story ..\\nOur coffeehouses have become a beacon for coffee lovers everywhere. Why do they insist on Starbucks? Because they know they can count on genuine service, an inviting atmosphere and a superb cup of expertly roasted and richly brewed coffee every time.', '37.758121362626966', '-122.4430846888572', '+123 456 7890', '(415) 292-7660', 'philzcoffee@gmail.com', 'philzcoffee.com', 1, 1412396131, 1412396131, 1, 0),
(3, 'The Grocery Store', '3625 Sacramento St San Francisco, CA 94118 b/t Spruce St &amp; Locust St in Presidio Heights', 'Our Company\\nTo say Starbucks purchases and roasts high-quality whole bean coffees is very true.', '37.76463544202423', '-122.41424557752907', '(415) 928-3615', '(415) 928-3615', 'thegrocerystore@gmail.com', 'www.thegrocerystore.com', 2, 1412396137, 1412396137, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_storefinder_users`
--

CREATE TABLE `tbl_storefinder_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 NOT NULL,
  `login_hash` varchar(255) CHARACTER SET utf8 NOT NULL,
  `facebook_id` text CHARACTER SET utf8 NOT NULL,
  `twitter_id` text CHARACTER SET utf8 NOT NULL,
  `email` varchar(255) CHARACTER SET utf8 NOT NULL,
  `deny_access` int(11) NOT NULL,
  `thumb_url` text CHARACTER SET utf8 NOT NULL,
  `photo_url` text CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `tbl_storefinder_users`
--

INSERT INTO `tbl_storefinder_users` (`user_id`, `full_name`, `username`, `password`, `login_hash`, `facebook_id`, `twitter_id`, `email`, `deny_access`, `thumb_url`, `photo_url`) VALUES
(1, 'John Doe', 'dummy_user', '25d55ad283aa400af464c76d713c07ad', 'W9VLmqTvvin+du3Rx7/YbI9dF1sxNzliYThhMzk1', '', '', 'dummy_user@gmail.com', 0, 'http://localhost/personal/storefinder/upload_pic/thumb_53af8ace7d3be.png', 'http://localhost/personal/storefinder/upload_pic/photo_53af8abaad0b5.png');
