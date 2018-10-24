INSERT INTO `badge` (`id`,`description`) VALUES
('TESTMAN1','Smith, John H');

INSERT INTO `employee` (`badgeid`,`firstname`,`middlename`,`lastname`,`employeetypeid`,`departmentid`,`shiftid`,`active`,`inactive`) VALUES
('TESTMAN1','John','H','Smith',1,1,4,'2018-10-24 11:39:05',null);

INSERT INTO `punch` (`terminalid`,`badgeid`,`originaltimestamp`,`punchtypeid`) VALUES
(103,'TESTMAN1','2018-10-29 22:29:00',1),
(103,'TESTMAN1','2018-10-30 07:04:00',0),
(103,'TESTMAN1','2018-10-30 22:30:00',1),
(103,'TESTMAN1','2018-10-31 07:00:00',0),
(103,'TESTMAN1','2018-10-31 22:31:00',1),
(103,'TESTMAN1','2018-11-01 06:56:00',0),
(103,'TESTMAN1','2018-11-01 22:40:00',1),
(103,'TESTMAN1','2018-11-02 06:50:00',0),
(103,'TESTMAN1','2018-11-02 22:30:00',1),
(103,'TESTMAN1','2018-11-03 02:31:00',0),
(103,'TESTMAN1','2018-11-03 02:54:00',1),
(103,'TESTMAN1','2018-11-03 07:00:00',0);