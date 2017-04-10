-- phpMyAdmin SQL Dump
-- version 3.5.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 22, 2014 at 09:37 PM
-- Server version: 5.5.29
-- PHP Version: 5.4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `db_restaurants`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_restaurants_authentication`
--

CREATE TABLE `tbl_restaurants_authentication` (
  `authentication_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `role_id` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  `deny_access` int(11) NOT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `tbl_restaurants_authentication`
--

INSERT INTO `tbl_restaurants_authentication` (`authentication_id`, `username`, `password`, `name`, `role_id`, `is_deleted`, `deny_access`) VALUES
(1, 'admin', '3dba44de6dbf6ad13444799ed798f5b8', 'Admin', 1, 0, 0),
(2, 'guest', '62c8ad0a15d9d1ca38d5dee762a16e01 ', 'Guest', 2, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_restaurants_categories`
--

CREATE TABLE `tbl_restaurants_categories` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) NOT NULL,
  `created_at` varchar(30) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `tbl_restaurants_categories`
--

INSERT INTO `tbl_restaurants_categories` (`category_id`, `category`, `created_at`, `is_deleted`) VALUES
(1, 'Fast Food', '', 0),
(2, 'Fast Casual', '', 0),
(3, 'Casual Dining', '', 0),
(4, 'Family Style', '', 0),
(5, 'Fine Dining', '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_restaurants_photos`
--

CREATE TABLE `tbl_restaurants_photos` (
  `photo_id` int(11) NOT NULL AUTO_INCREMENT,
  `photo_url` text NOT NULL,
  `thumb_url` text NOT NULL,
  `restaurant_id` int(11) NOT NULL,
  `created_at` varchar(30) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `tbl_restaurants_photos`
--

INSERT INTO `tbl_restaurants_photos` (`photo_id`, `photo_url`, `thumb_url`, `restaurant_id`, `created_at`, `is_deleted`) VALUES
(1, 'http://4.bp.blogspot.com/-XIy89D2mvt0/Us9W1utBVqI/AAAAAAAACCU/fQKOUpqEXU4/s1600/5.jpg', 'http://media-cdn.tripadvisor.com/media/photo-s/03/2e/7f/8a/grand-shanghai.jpg', 5, '', 0),
(2, 'http://s3.amazonaws.com/foodspotting-ec2/reviews/3339817/thumb_600.jpg', 'http://s3.amazonaws.com/foodspotting-ec2/reviews/3339817/thumb_600.jpg', 1, '', 0),
(3, 'http://www.asia-bars.com/wp-content/uploads/2012/12/jim-thompson-singapore-0001.jpg', 'http://www.jimthompson.com/restaurants_bars/images/THOMPSON-RESTAURANT05.jpg', 2, '', 0),
(4, 'http://1.bp.blogspot.com/-OmqMx_1tz_4/ThW_kjqI9BI/AAAAAAAARgU/-WmBGBXZBgQ/s1600/Picture+259.jpg', 'http://blog.omy.sg/danielang/files/2008/06/putien11.jpg', 3, '', 0),
(5, 'http://4.bp.blogspot.com/_IdSQboBHMX4/SxR871k-u_I/AAAAAAAAFAc/svDksEPOyhc/s1600/P1080116.JPG', 'http://www.gayatrirestaurant.com/static/uploads/reserve5.jpg', 4, '', 0),
(6, 'http://www.shentrepreneur.org/wp-content/uploads/2012/08/NDD-01.png', 'http://www.shentrepreneur.org/wp-content/uploads/2012/08/NDD-01.png', 1, '', 0),
(7, 'http://sg.clubalc.com/content/dam/alacarte/images/ParticipatingRestaurantsandBars/GrandShanghai1.jpg', 'http://sg.clubalc.com/content/dam/alacarte/images/ParticipatingRestaurantsandBars/GrandShanghai1.jpg', 5, '', 0),
(8, 'http://d2jzxcrnybzkkt.cloudfront.net/uploads/2013/11/dbslider2_jpg_1383719620.jpg', 'http://d2jzxcrnybzkkt.cloudfront.net/uploads/2013/11/dbslider2_jpg_1383719620.jpg', 1, '', 0),
(9, 'http://www.chainedesrotisseurs.com/news_online/uploads/images/s_01361/raw/a01%20001.jpg', 'http://www.chainedesrotisseurs.com/news_online/uploads/images/s_01361/raw/a01%20001.jpg', 1, '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_restaurants_restaurants`
--

CREATE TABLE `tbl_restaurants_restaurants` (
  `restaurant_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `lat` varchar(100) NOT NULL,
  `lon` varchar(100) NOT NULL,
  `desc1` text NOT NULL,
  `email` varchar(100) NOT NULL,
  `website` varchar(255) NOT NULL,
  `amenities` text NOT NULL,
  `food_rating` float NOT NULL,
  `price_rating` float NOT NULL,
  `featured` int(11) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `hours` varchar(100) NOT NULL,
  `created_at` varchar(100) NOT NULL,
  `category_id` int(11) NOT NULL,
  `is_deleted` int(11) NOT NULL,
  PRIMARY KEY (`restaurant_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `tbl_restaurants_restaurants`
--

INSERT INTO `tbl_restaurants_restaurants` (`restaurant_id`, `name`, `address`, `lat`, `lon`, `desc1`, `email`, `website`, `amenities`, `food_rating`, `price_rating`, `featured`, `phone`, `hours`, `created_at`, `category_id`, `is_deleted`) VALUES
(1, 'Swee Choon Tim Sum Restaurant Pte Ltd', '191 Jalan Besar, Singapore 208882', '1.308290', '103.856954', 'Restaurant for all ocassions.', 'support@sweechoon.com', 'www.sweechoon.com', 'Parking, Wifi, Credit Card, Lunch, Dinner, Breakfast, Take-out, Dine-in', 3.5, 4.5, 1, '+65 6225 7788', '6:00 PM - 6:00 AM', '', 3, 0),
(2, 'Jim Thompson', '45 Minden Road, Dempsey Hill, Singapore 248817', '1.305573', '103.815691', 'Good for children.', 'support@jimthompson.com', 'www.jimthompson.com', 'Parking, Wifi, Credit Card, Lunch, Dinner, Breakfast, Take-out, Dine-in', 4, 3, -1, '+65 6475 6088', '6:00 PM - 6:00 AM', '1400781610', 3, 0),
(3, 'PuTien', '127 Kitchener Rd, Singapore 208514', '1.309865', '103.857383', 'For Childrens too.', 'support@putien.com', 'www.putien.com', 'Parking, Wifi, Credit Card, Lunch, Dinner, Breakfast', 4, 3.5, -1, '+65 6295 6358', '11:30 AM - 3:00 PM, 5:30 AM - 11:00 PM', '1400781733', 4, 0),
(4, 'Gayatri Restaurant', '122 Race Course Rd, Singapore 218583', '1.309974', '103.851971', 'We open 24 hrs to serve you.', 'support@gayatrirestaurant.com', 'www.gayatrirestaurant.com', 'Parking, Wifi, Credit Card, Lunch, Dinner, Breakfast', 4, 5, 1, '+65 6292 4544', 'Open 24 Hrs', '', 4, 0),
(5, 'Grand ShangHai Restaurant', '390 Havelock Rd Singapore 169662', '1.299021', '103.834580', 'You will be loving our place, so come and dine with us.', 'support@grandshanghai.com', 'www.grandshanghai.com', 'Parking, Wifi, Credit Card, Can Smoke', 4, 4.5, -1, '+65 6836 6866', '11:30 AM - 3:00 PM, 5:30 AM - 11:00 PM', '1400782952', 0, 0);
