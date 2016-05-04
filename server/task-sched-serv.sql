-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 04, 2016 at 03:28 PM
-- Server version: 5.5.47-0+deb8u1
-- PHP Version: 5.6.19-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `task-sched-serv`
--

-- --------------------------------------------------------

--
-- Table structure for table `new_tasks`
--

CREATE TABLE IF NOT EXISTS `new_tasks` (
`task_id` int(11) NOT NULL,
  `subject` text NOT NULL,
  `completion_status` tinyint(4) DEFAULT NULL,
  `completion_percentage` tinyint(4) DEFAULT NULL,
  `repeat_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `deadline_time` datetime DEFAULT NULL,
  `estimated_time` datetime DEFAULT NULL,
  `priority` tinyint(4) DEFAULT NULL,
  `repeat_conditions` text,
  `description` text
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tasks`
--

CREATE TABLE IF NOT EXISTS `tasks` (
  `_id` int(11) NOT NULL,
  `subject` text NOT NULL,
  `completion_status` tinyint(4) DEFAULT NULL,
  `completion_percentage` tinyint(4) DEFAULT NULL,
  `repeat_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `deadline_time` datetime DEFAULT NULL,
  `estimated_time` datetime DEFAULT NULL,
  `priority` tinyint(4) DEFAULT NULL,
  `repeat_conditions` text,
  `description` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `new_tasks`
--
ALTER TABLE `new_tasks`
 ADD PRIMARY KEY (`task_id`);

--
-- Indexes for table `tasks`
--
ALTER TABLE `tasks`
 ADD PRIMARY KEY (`_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `new_tasks`
--
ALTER TABLE `new_tasks`
MODIFY `task_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=28;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
