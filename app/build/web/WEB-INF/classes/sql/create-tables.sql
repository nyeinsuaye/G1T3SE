CREATE TABLE IF NOT EXISTS student(
user_id VARCHAR(55) NOT NULL,
name VARCHAR(55) NOT NULL,
password VARCHAR(55) NOT NULL,
edollar DECIMAL(5,2) NOT NULL,
school VARCHAR(128) NOT NULL,
excess_edollar DECIMAL(5,2) NULL,

CONSTRAINT student_pk PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS course(
course VARCHAR(10) NOT NULL,
school VARCHAR(128) NOT NULL,
title VARCHAR(128) NOT NULL,
description VARCHAR(600) NOT NULL,
exam_date DATE NOT NULL,
exam_start TIME NOT NULL,
exam_end TIME NOT NULL,

CONSTRAINT course_pk PRIMARY KEY(course)
);

CREATE TABLE IF NOT EXISTS section(
course VARCHAR(10) NOT NULL,
section VARCHAR(3) NOT NULL,
day INTEGER NOT NULL,
start TIME NOT NULL,
end TIME NOT NULL,
instructor VARCHAR(55) NOT NULL,
venue VARCHAR(55) NOT NULL,
size INTEGER NOT NULL,

CONSTRAINT section_pk PRIMARY KEY(course, section),
CONSTRAINT section_fk FOREIGN KEY(course) references course(course)
);

CREATE TABLE IF NOT EXISTS prerequisite(
course VARCHAR(10) NOT NULL,
prerequisite VARCHAR(10) NOT NULL,

CONSTRAINT prerequisite_pk PRIMARY KEY(course,prerequisite),
CONSTRAINT prerequisite_fk FOREIGN KEY(course) references course(course),
CONSTRAINT prerequisite_fk1 FOREIGN KEY(prerequisite) references course(course)
);

CREATE TABLE IF NOT EXISTS course_completed(
user_id VARCHAR(55) NOT NULL,
course VARCHAR(10) NOT NULL,

CONSTRAINT course_completed_pk PRIMARY KEY(user_id,course),
CONSTRAINT course_completed_fk1 FOREIGN KEY(user_id) references student(user_id),
CONSTRAINT course_completed_fk2 FOREIGN KEY(course) references course(course)
);

CREATE TABLE IF NOT EXISTS bid(
user_id VARCHAR(55) NOT NULL,
amount DECIMAL(5,2) NOT NULL,
course VARCHAR(10) NOT NULL,
section VARCHAR(3) NOT NULL,

CONSTRAINT bid_pk PRIMARY KEY(user_id,course,section),
CONSTRAINT bid_fk1 FOREIGN KEY(user_id) references student(user_id),
CONSTRAINT bid_fk2 FOREIGN KEY(course,section) references section(course,section)
);

CREATE TABLE IF NOT EXISTS bid_temp(
user_id VARCHAR(55) NOT NULL,
amount DECIMAL(5,2) NOT NULL,
course VARCHAR(10) NOT NULL,
section VARCHAR(3) NOT NULL,
is_successful INTEGER(1) NULL,

CONSTRAINT bid_temp_pk PRIMARY KEY(user_id,course,section),
CONSTRAINT bid_temp_fk1 FOREIGN KEY(user_id) references student(user_id),
CONSTRAINT bid_temp_fk2 FOREIGN KEY(course,section) references section(course,section)
);

CREATE TABLE IF NOT EXISTS section_student(
user_id VARCHAR(55) NOT NULL,
amount DECIMAL(5,2) NOT NULL,
course VARCHAR(10) NOT NULL,
section VARCHAR(3) NOT NULL,
round_num INTEGER(1) NULL,

CONSTRAINT section_student_pk PRIMARY KEY(user_id,course,section),
CONSTRAINT section_student_fk1 FOREIGN KEY(user_id) references student(user_id),
CONSTRAINT section_student_fk2 FOREIGN KEY(course,section) references section(course,section)
);

CREATE TABLE IF NOT EXISTS failed_bid(
user_id VARCHAR(55) NOT NULL,
amount DECIMAL(5,2) NOT NULL,
course VARCHAR(10) NOT NULL,
section VARCHAR(3) NOT NULL,

CONSTRAINT failed_bid_pk PRIMARY KEY(user_id,course,section),
CONSTRAINT failed_bid_fk1 FOREIGN KEY(user_id) references student(user_id),
CONSTRAINT failed_bid_fk2 FOREIGN KEY(course,section) references section(course,section)
);

CREATE TABLE IF NOT EXISTS sudden_death(
user_id VARCHAR(55) NOT NULL,
amount DECIMAL(5,2) NOT NULL,
course VARCHAR(10) NOT NULL,
section VARCHAR(3) NOT NULL,
is_latest INTEGER(1) NULL,
is_successful INTEGER(1) NOT NULL, 

CONSTRAINT sudden_death_pk PRIMARY KEY(user_id,course),
CONSTRAINT sudden_death_fk1 FOREIGN KEY(user_id) references student(user_id),
CONSTRAINT sudden_death_fk2 FOREIGN KEY(course) references section(course)
);

CREATE TABLE IF NOT EXISTS minimum_price (
course VARCHAR(10) NOT NULL,
section VARCHAR(3) NOT NULL,
vacancy_left INTEGER NOT NULL,
price DECIMAL(5,2) NULL,

CONSTRAINT minimum_price_pk PRIMARY KEY(course,section),
CONSTRAINT minimum_price_fk FOREIGN KEY(course,section) references section(course,section)
);

CREATE TABLE IF NOT EXISTS round(
round_num INTEGER NOT NULL,

CONSTRAINT round_pk PRIMARY KEY(round_num)
);

INSERT INTO round(round_num) VALUES (0);