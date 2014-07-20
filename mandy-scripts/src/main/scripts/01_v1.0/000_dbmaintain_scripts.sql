--
-- Table structure for table `dbmaintain_scripts`
--

CREATE TABLE IF NOT EXISTS `dbmaintain_scripts` (
  `file_name` varchar(150) collate utf8_unicode_ci default NULL,
  `file_last_modified_at` bigint(20) default NULL,
  `checksum` varchar(50) collate utf8_unicode_ci default NULL,
  `executed_at` varchar(20) collate utf8_unicode_ci default NULL,
  `succeeded` bigint(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

