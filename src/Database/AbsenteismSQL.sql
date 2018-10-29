CREATE TABLE `absenteeism` (
  `badgeid` char(8) NOT NULL,
  `payperiod` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `percentage` double NOT NULL,
  PRIMARY KEY (`badgeid`,`payperiod`),
  CONSTRAINT `FK_absenteeism_1` FOREIGN KEY (`badgeid`) REFERENCES `badge` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;