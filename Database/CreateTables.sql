drop database museum
create database museum
create table OWNER (
    ownerID int primary key not null,
    name varchar(100) not null,
    phoneNumber varchar(9) not null,
    email varchar(100)
);

create table owner_address (
    ownerID int primary key not null,
    street varchar(100) not null,
    buildingNumber int not null,
    unitNumber int,
    postCode varchar(100),
    city varchar(100),
    country varchar(100),
    FOREIGN KEY (ownerID) REFERENCES OWNER(ownerID)
);

create table age (
    ageID int primary key not null,
    name varchar(100) not null,
    startYear int null, 
    endYear int null
);

create table exhibit (
    exhibitID int primary key not null,
    name varchar(100) not null,
    description varchar(250) not null, 
    dateOfAcquisition date,
    value int,
    ageID int not null,
    FOREIGN key (ageID) REFERENCES age(ageID)
);

create table exhibit_owner (
    ownerID int not null,
    exhibitID int not null,
    primary key (ownerID, exhibitID),
    FOREIGN key (ownerID) REFERENCES owner(ownerID),
    FOREIGN key (exhibitID) REFERENCES exhibit(exhibitID)
);

create table category (
    categoryID int primary key not null,
    name varchar(100)
);

create table exhibit_category(
    exhibitID int not null,
    categoryID int not null,
    primary key (exhibitID, categoryID),
    FOREIGN key (exhibitID) REFERENCES exhibit(exhibitID),
    FOREIGN key (categoryID) REFERENCES category(categoryID)
);

create table room(
    roomID int PRIMARY key not null,
    roomNumber int not null,
    floor int not null,
    area int not null
);


create table exhibition(
    exhibitionID int PRIMARY key not null,
    title varchar(100) not null,
    startDate date not null,
    endDate date not null
);

create table exhibition_room(
    exhibitionID int not null,
    roomID int not null,
    primary key (exhibitionID, roomID),
    FOREIGN key (exhibitionID) REFERENCES exhibition(exhibitionID),
    FOREIGN key (roomID) REFERENCES room(roomID)
);

create table exhibit_exhibition (
    exhibitionID int not null,
    exhibitID int not null,
    primary key (exhibitionID, exhibitID),
    FOREIGN key (exhibitionID) REFERENCES exhibition(exhibitionID),
    FOREIGN key (exhibitID) REFERENCES exhibit(exhibitID)
);


create table ticket (
    ticketID int primary key not null,
    type varchar(24) not null,
    price int not null
);

create table tour (
    tourID int primary key not null,
    groupLeader varchar(100) not null,
    tourDate datetime not null,
    size int not null,
    language varchar(30) not null,
    workerID int not null
);

create table tour_ticket (
    tourID int not null,
    ticketID int not null,
    amount int not null,
    primary key (tourID, ticketID),
    foreign key (tourID) REFERENCES tour(tourID),
    foreign key (ticketID) REFERENCES ticket(ticketID)    
);

create table worker (
    workerID int primary key not null,
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
    foreign key (managerID) REFERENCES worker(workerID),
    businessID varchar(30)
);

alter table tour add FOREIGN key (workerID) REFERENCES worker(workerID);

create table worker_exhibition (
    workerID int not null,
    exhibitionID int not null,
    primary key(workerID, exhibitionID),
    FOREIGN KEY (workerID) REFERENCES worker(workerID),
    FOREIGN KEY (exhibitionID) REFERENCES exhibition(exhibitionID)
);

create table worker_address (
    workerID int not null,
    street varchar(100) not null,
    buildingNumber int not null,
    unitNumber int,
    postCode varchar(100),
    city varchar(100),
    country varchar(100),
    FOREIGN KEY (workerID) REFERENCES worker(workerID)
);

create table museum (
    businessID varchar(30) primary key not null,
    name varchar(50) not null,
    accountNumber varchar(50) not null
);

create table museum_address (
    businessID varchar(30) not null,
    street varchar(100) not null,
    buildingNumber int not null,
    unitNumber int,
    postCode varchar(100),
    city varchar(100),
    country varchar(100),
    primary key (businessID),
    FOREIGN key (businessID) REFERENCES museum(businessID)
)





