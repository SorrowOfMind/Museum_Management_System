
CREATE TABLE OWNER (
    ownerID int PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null,
    phoneNumber varchar(9) not null,
    email varchar(100)
);

CREATE TABLE owner_address (
    ownerID int PRIMARY KEY,
    street varchar(100) not null,
    buildingNumber int not null,
    unitNumber int,
    postCode varchar(100),
    city varchar(100),
    country varchar(100),
    FOREIGN KEY (ownerID) REFERENCES OWNER(ownerID)
);

CREATE TABLE age (
    ageID int PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) not null,
    startYear int null,
    endYear int null
);

CREATE TABLE `exhibit` (
    `exhibitID` int PRIMARY KEY AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `author` varchar(250) DEFAULT NULL,
    `creationDate` varchar(100) DEFAULT NULL,
    `origins` varchar(250) DEFAULT NULL,
    `description` varchar(250) DEFAULT NULL,
    `acquisitionDate` date DEFAULT NULL,
    `value` int DEFAULT NULL,
    `ageID` int NOT NULL,
    `lastConservation` date DEFAULT NULL,
    `nextConservation` date DEFAULT NULL,
    `status` varchar(100) NOT NULL,
    `security` varchar(100) NOT NULL,
    `filePath` varchar(250) DEFAULT NULL,
    KEY `ageID` (`ageID`),
    CONSTRAINT `exhibit_ibfk_1` FOREIGN KEY (`ageID`) REFERENCES `age` (`ageID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE exhibit_owner (
    ownerID int NOT NULL,
    exhibitID int NOT NULL,
    PRIMARY KEY (ownerID, exhibitID),
    FOREIGN KEY (ownerID) REFERENCES owner(ownerID),
    FOREIGN KEY (exhibitID) REFERENCES exhibit(exhibitID)
);

CREATE TABLE category (
    categoryID int PRIMARY KEY AUTO_INCREMENT,
    name varchar(100)
);

CREATE TABLE exhibit_category(
    exhibitID int NOT NULL,
    categoryID int NOT NULL,
    PRIMARY KEY (exhibitID, categoryID),
    FOREIGN KEY (exhibitID) REFERENCES exhibit(exhibitID),
    FOREIGN KEY (categoryID) REFERENCES category(categoryID)
);

CREATE TABLE room(
    roomID int PRIMARY KEY AUTO_INCREMENT,
    roomNumber int not null,
    floor int not null,
    area int not null
);

CREATE TABLE exhibition(
    exhibitionID int PRIMARY KEY AUTO_INCREMENT,
    title varchar(100) not null,
    startDate date not null,
    endDate date null
);

CREATE TABLE exhibition_room(
    exhibitionID int NOT NULL AUTO_INCREMENT,
    roomID int NOT NULL,
    PRIMARY KEY (exhibitionID, roomID),
    FOREIGN KEY (exhibitionID) REFERENCES exhibition(exhibitionID),
    FOREIGN KEY (roomID) REFERENCES room(roomID)
);

CREATE TABLE exhibit_exhibition (
    exhibitionID int NOT NULL AUTO_INCREMENT,
    exhibitID int NOT NULL,
    PRIMARY KEY (exhibitionID, exhibitID),
    FOREIGN KEY (exhibitionID) REFERENCES exhibition(exhibitionID),
    FOREIGN KEY (exhibitID) REFERENCES exhibit(exhibitID)
);

CREATE TABLE worker (
    workerID int PRIMARY KEY AUTO_INCREMENT,
    forename varchar(50) not null,
    surname varchar(50) not null,
    dateOfBirth date not NULL,
    phoneNumber varchar(9) not null,
    email varchar(100),
    dateOfTermination date,
    agreementType varchar(30) not null,
    dateOfAgreement date not null,
    accountNumber varchar(50) not null,
    salary int not null,
    jobTitle varchar(30),
    managerID int,
    FOREIGN KEY (managerID) REFERENCES worker(workerID),
    businessID varchar(30)
);

CREATE TABLE tour (
    tourID int PRIMARY KEY AUTO_INCREMENT,
    groupLeader varchar(100) not null,
    tourDate datetime not null,
    tourHour varchar(5),
    size int not null,
    language varchar(30)  null,
    standardTicketCount int null,
    discountTicketCount int null,
    workerID int  null,
    foreign key (workerID) REFERENCES worker(workerID)
);


ALTER TABLE tour ADD FOREIGN KEY (workerID) REFERENCES worker(workerID);

CREATE TABLE worker_exhibition (
    workerID int NOT NULL AUTO_INCREMENT,
    exhibitionID int NOT NULL,
    PRIMARY KEY(workerID, exhibitionID),
    FOREIGN KEY (workerID) REFERENCES worker(workerID),
    FOREIGN KEY (exhibitionID) REFERENCES exhibition(exhibitionID)
);

CREATE TABLE worker_address (
    workerID int NOT NULL,
    street varchar(100) not null,
    buildingNumber int not null,
    unitNumber int,
    postCode varchar(100),
    city varchar(100),
    country varchar(100),
    FOREIGN KEY (workerID) REFERENCES worker(workerID)
);

CREATE TABLE museum (
    businessID varchar(30) PRIMARY KEY NOT NULL,
    name varchar(50) not null,
    accountNumber varchar(50) not null
);

CREATE TABLE museum_address (
    businessID varchar(30) NOT NULL,
    street varchar(100) not null,
    buildingNumber int not null,
    unitNumber int,
    postCode varchar(100),
    city varchar(100),
    country varchar(100),
    PRIMARY KEY (businessID),
    FOREIGN KEY (businessID) REFERENCES museum(businessID)
);

CREATE TABLE `user` (
  `userID` int PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` tinyint NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE visitor (
    ID int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    month varchar(50) not null,
    number int not null
);

INSERT INTO OWNER (name, phoneNumber, email) VALUES
('John Doe', '123456789', 'john.doe@example.com'),
('Jane Smith', '987654321', 'jane.smith@example.com');

INSERT INTO owner_address (ownerID, street, buildingNumber, unitNumber, postCode, city, country) VALUES
(1, 'Main St', 123, NULL, '12345', 'City1', 'Country1'),
(2, 'Broad St', 456, 789, '54321', 'City2', 'Country2');

INSERT INTO visitor (month, number) VALUES
("01.2023", 2110),
("02.2023", 2050),
("03.2023", 2810),
("04.2023", 2500),
("05.2023", 3160),
("06.2023", 3500),
("07.2023", 4080),
("08.2023", 4290),
("09.2023", 3200),
("10.2023", 3030),
("11.2023", 2910),
("12.2023", 2600),
("01.2024", 2220);

INSERT INTO age (name, startYear, endYear) VALUES
('Starożytność', -5000, 500),
('Hellenizm', -323, -30),
('Cesarski Rzym', -30, 1453),
('Średniowiecze', 476, 1492),
('Nowożytność', 1492, 1789),
('Epoka XIX w.', 1789, 1914),
('Współczesność', 1914, null);


INSERT INTO category (name) VALUES
('Abstract'),
('Realism');

INSERT INTO room (roomNumber, floor, area) VALUES
(101, 1, 50),
(201, 2, 75),
(202, 2, 100),
(210, 2, 150);


INSERT INTO worker (forename, surname, dateOfBirth, phoneNumber, email, dateOfTermination, agreementType, dateOfAgreement, accountNumber, salary, jobTitle, managerID, businessID) VALUES
('Borzygniew', 'Ciemięga', '1981-07-29', '111222333', 'bciemiega@museum.com', NULL, 'B2B', '2020-01-01', '123456789', 5000, 'kurator', NULL, 'BUS1'),
('Pomścibór', 'Boligłowa', '1990-02-02', '444555666', 'pboliglowa@museum.com', NULL, 'umowa o pracę', '2021-01-01', '987654321', 3000, 'kustosz', 1, 'BUS2'),
('Mściwój', 'Półnoga', '1990-02-02', '444555666', 'mpolnoga@museum.com', NULL, 'umowa o pracę', '2021-01-01', '9876456321', 3000, 'konserwator', 2, 'BUS2'),
('Myślimir', 'Pomietlarz', '1990-02-02', '444555666', 'mpomietlarz@museum.com', NULL, 'umowa o pracę', '2020-01-01', '687699876', 3500, 'konserwator', 2, 'BUS2');

INSERT INTO tour (groupLeader, tourDate, tourHour, language, standardTicketCount, discountTicketCount, workerID, size)
VALUES
    ('Group1 Leader', '2023-12-01 10:00:00', '10:00', 'English', 5, 3, 1, (5 + 3)),
    ('Group2 Leader', '2023-12-02 14:30:00', '14:30', 'Spanish', 3, 2, 2, (3 + 2));


INSERT INTO worker_address (workerID, street, buildingNumber, unitNumber, postCode, city, country) VALUES
(1, 'Manager St', 10, NULL, '54321', 'City1', 'Country1'),
(2, 'Employee St', 20, 5, '12345', 'City2', 'Country2');

INSERT INTO museum (businessID, name, accountNumber) VALUES
('BUS1', 'Museum 1', '111111'),
('BUS2', 'Museum 2', '222222');

INSERT INTO museum_address (businessID, street, buildingNumber, unitNumber, postCode, city, country) VALUES
('BUS1', 'Main St', 100, NULL, '11111', 'City1', 'Country1'),
('BUS2', 'Broad St', 200, 10, '22222', 'City2', 'Country2');

-- password: 1234
INSERT INTO `user` (userID, username, password, role) VALUES
(1, 'superadmin', '81dc9bdb52d04dc20036dbd8313ed055', 0),
(2, 'admin', '81dc9bdb52d04dc20036dbd8313ed055', 1),
(3, 'user', '81dc9bdb52d04dc20036dbd8313ed055', 2);

INSERT INTO exhibit (name, author, creationDate, origins, description, acquisitionDate, value, ageID, lastConservation, nextConservation, status, security) VALUES
("Kuźnia Wulkana", "Francesco da Ponte", "II poł. XVI w.", "Włochy", "olej na płótnie", "2023-01-01",  300000, 5, "2023-01-21", "2024-01-21", "wystawa", "ekstra"),
("Kobieta przed lustrem", "Pieter Codde", "XVII w.", "Holandia", "olej na desce dębowej", "2023-01-01", 100000, 5, "2023-01-01", "2024-01-01", "wystawa", "ekstra"),
("Żydówka z pomarańczami", "Aleksander Gierymski", "1880-1881", "Polska", "olej na płótnie", "2023-01-01", 200000, 6, "2023-01-01", "2024-01-01", "wystawa", "ekstra"),
("Figura Króla Dawida", "nieznany", "XVw", "Hiszpania", "drewno polichromowane", "2023-01-01", 300000, 4, "2023-01-01", "2024-01-21", "wystawa", "standard"),
("Topór perski", "nieznany", "XVIII w.", "Persja", "", "2023-01-01", 100000, 6, "2023-04-01", "2024-04-01", "wystawa", "standard"),
("Szyszak turniejowy", "nieznany", "XVI w.", "Niemcy", "","2023-01-01", 100000, 5, "2023-04-01", "2024-04-01", "wystawa", "standard");

