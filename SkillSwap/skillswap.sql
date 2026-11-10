
CREATE DATABASE IF NOT EXISTS SkillSwap;
USE SkillSwap;

CREATE TABLE STUDENT (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    year_of_study INT CHECK (year_of_study BETWEEN 1 AND 4)
);

CREATE TABLE SKILL (
    skill_id INT PRIMARY KEY AUTO_INCREMENT,
    skill_name VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(50),
    description VARCHAR(255)
);

CREATE TABLE STUDENT_SKILL (
    student_skill_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    skill_id INT NOT NULL,
    skill_type ENUM('TEACHING','LEARNING') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES STUDENT(student_id),
    FOREIGN KEY (skill_id) REFERENCES SKILL(skill_id),
    UNIQUE(student_id, skill_id, skill_type)
);

CREATE TABLE LEARNING_REQUEST (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    learner_id INT NOT NULL,
    teacher_id INT NOT NULL,
    skill_id INT NOT NULL,
    request_date DATE NOT NULL,
    status ENUM('PENDING','ACCEPTED','REJECTED','COMPLETED')
        DEFAULT 'PENDING',
    FOREIGN KEY (learner_id) REFERENCES STUDENT(student_id),
    FOREIGN KEY (teacher_id) REFERENCES STUDENT(student_id),
    FOREIGN KEY (skill_id) REFERENCES SKILL(skill_id),
    CHECK (learner_id <> teacher_id)
);

CREATE TABLE SESSION (
    session_id INT PRIMARY KEY AUTO_INCREMENT,
    request_id INT UNIQUE NOT NULL,
    session_date DATE NOT NULL,
    session_time TIME NOT NULL,
    duration INT NOT NULL,
    status ENUM('SCHEDULED','COMPLETED','CANCELLED')
        DEFAULT 'SCHEDULED',
    FOREIGN KEY (request_id) REFERENCES LEARNING_REQUEST(request_id),
    CHECK (duration > 0)
);

CREATE TABLE FEEDBACK (
    feedback_id INT PRIMARY KEY AUTO_INCREMENT,
    session_id INT UNIQUE NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comments VARCHAR(255),
    feedback_date DATE NOT NULL,
    FOREIGN KEY (session_id) REFERENCES SESSION(session_id)
);

-- SAMPLE STUDENTS
INSERT INTO STUDENT
(student_name,email,password,department,year_of_study)
VALUES
('Arun','arun@gmail.com','arun123','CSE',2),
('Priya','priya@gmail.com','priya123','IT',2),
('Rahul','rahul@gmail.com','rahul123','ECE',3),
('Divya','divya@gmail.com','divya123','CSE',2),
('Kavin','kavin@gmail.com','kavin123','AIDS',3);

-- SAMPLE SKILLS
INSERT INTO SKILL
(skill_name,category,description)
VALUES
('Java','Programming','Java and OOP'),
('Python','Programming','Python programming'),
('Photoshop','Design','Photo editing'),
('Web Development','Programming','HTML CSS JavaScript'),
('UI UX Design','Design','Interface design');

-- SAMPLE STUDENT SKILLS
INSERT INTO STUDENT_SKILL
(student_id,skill_id,skill_type)
VALUES
(1,1,'TEACHING'),
(1,3,'LEARNING'),
(2,3,'TEACHING'),
(2,1,'LEARNING'),
(3,2,'TEACHING'),
(3,4,'LEARNING'),
(4,4,'TEACHING'),
(4,2,'LEARNING'),
(5,5,'TEACHING'),
(5,1,'LEARNING');

-- SAMPLE LEARNING REQUESTS
INSERT INTO LEARNING_REQUEST
(learner_id,teacher_id,skill_id,request_date,status)
VALUES
(1,2,3,CURDATE(),'ACCEPTED'),
(2,1,1,CURDATE(),'PENDING');

-- SAMPLE SESSION
INSERT INTO SESSION
(request_id,session_date,session_time,duration,status)
VALUES
(1,CURDATE(),'10:00:00',60,'COMPLETED');

-- SAMPLE FEEDBACK
INSERT INTO FEEDBACK
(session_id,rating,comments,feedback_date)
VALUES
(1,5,'Excellent teaching!',CURDATE());
