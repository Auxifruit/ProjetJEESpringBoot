CREATE DATABASE CYDataBasesb;

USE CYDataBasesb;

# Suppression des tables dans l'ordre
/*
DROP TABLE administrator;
DROP TABLE grade;
DROP TABLE student;
DROP TABLE users_to_validate;
DROP TABLE major;
DROP TABLE lesson_class;
DROP TABLE lesson;
DROP TABLE classes;
DROP TABLE course;
DROP TABLE teacher;
DROP TABLE subjects;
DROP TABLE users;
*/

CREATE TABLE major (
	major_id INT AUTO_INCREMENT PRIMARY KEY,
    major_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE users (
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_password VARCHAR(50) NOT NULL,
    user_last_name VARCHAR(50) NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    user_email VARCHAR(50) UNIQUE NOT NULL,
    user_birthdate VARCHAR(50) NOT NULL,
    user_role ENUM("student","teacher","administrator")
);

CREATE TABLE users_to_validate (
	user_to_validate_id INT AUTO_INCREMENT PRIMARY KEY,
    user_to_validate_password VARCHAR(50) NOT NULL,
    user_to_validate_lastname VARCHAR(50) NOT NULL,
    user_to_validate_name VARCHAR(50) NOT NULL,
    user_to_validate_email VARCHAR(50) UNIQUE NOT NULL,
    user_to_validate_birthdate VARCHAR(50) NOT NULL,
    user_to_validate_role ENUM("student","teacher","administrator"),
    user_to_validate_major_id INT,
    
    FOREIGN KEY  (user_to_validate_major_id) REFERENCES major(major_id) ON DELETE SET NULL
);

CREATE TABLE administrator (
    administrator_id INT,
    
    FOREIGN KEY (administrator_id) REFERENCES users(user_id) ON DELETE CASCADE,
	
    PRIMARY KEY (administrator_id)
);

CREATE TABLE teacher (
    teacher_id INT,
    
    FOREIGN KEY (teacher_id) REFERENCES users(user_id) ON DELETE CASCADE,
    
    PRIMARY KEY (teacher_id)
);

CREATE TABLE classes(
	class_id INT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE student (
    student_id INT NOT NULL,
    class_id INT, 
    major_id INT,
    
	FOREIGN KEY  (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY  (class_id) REFERENCES classes(class_id) ON DELETE SET NULL,
    FOREIGN KEY  (major_id) REFERENCES major(major_id) ON DELETE SET NULL,
    
    PRIMARY KEY (student_id)
);

CREATE TABLE subjects(
	subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE course(
	course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(50) UNIQUE NOT NULL,
    subject_id INT,
    
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE SET NULL
);

CREATE TABLE grade (
    grade_id INT AUTO_INCREMENT PRIMARY KEY,
    grade_name VARCHAR(50) NOT NULL,
    grade_value DOUBLE NOT NULL,
    grade_coefficient INT NOT NULL,
    student_id INT NOT NULL,
    course_id INT,
    teacher_id INT,

    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE SET NULL,
    FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id) ON DELETE SET NULL
);

CREATE TABLE lesson (
	lesson_id INT AUTO_INCREMENT PRIMARY KEY,
	lesson_start_date DATETIME NOT NULL,
    lesson_end_date DATETIME NOT NULL,
    course_id INT,
	teacher_id int,
    
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id) ON DELETE SET NULL
);

CREATE TABLE lesson_class (
	lesson_class_id INT AUTO_INCREMENT PRIMARY KEY,
    lesson_id INT,
    class_id INT,
    
    FOREIGN KEY (lesson_id) REFERENCES lesson(lesson_id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE SET NULL
);

# Jeu de données pour test
INSERT INTO classes VALUES (1, "GSI1");
INSERT INTO classes VALUES (2, "GSI2");
INSERT INTO classes VALUES (3, "GSI3");

INSERT INTO major VALUES (1, "GI");
INSERT INTO major VALUES (2, "GM");
INSERT INTO major VALUES (3, "BTC");

INSERT INTO subjects VALUES (1, "Mathématiques");
INSERT INTO subjects VALUES (2, "Informatique");

INSERT INTO users VALUES (1, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62", "Evans", "Mark", "mark@gmail.com", "2000-01-02", "student");
INSERT INTO users VALUES (2, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62", "Blaze", "Axel", "axel@gmail.com", "2002-05-06", "student");
INSERT INTO users VALUES (3, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62", "Sharp", "Jude", "jude@gmail.com", "2002-04-03", "student");
INSERT INTO users VALUES (4, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62", "Eau", "Ondine", "ondine@gmail.com", "1982-07-08", "teacher");
INSERT INTO users VALUES (5, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62", "Roche", "Pierre", "pierre@gmail.com", "1978-09-10", "teacher");
INSERT INTO users VALUES (6, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62", "Capone", "Bege", "bege@gmail.com", "1786-12-01", "administrator");
INSERT INTO users VALUES (7, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62", "Newgate", "Edward", "edward@gmail.com", "1781-02-11", "administrator");

INSERT INTO student VALUES (1, 3, 1);
INSERT INTO student VALUES (2, 1, 2);
INSERT INTO student VALUES (3, 2, 3);

INSERT INTO teacher VALUES(4);
INSERT INTO teacher VALUES(5);

INSERT INTO administrator VALUES(6);
INSERT INTO administrator VALUES(7);

INSERT INTO course VALUES (1, "Statistique", 1); 
INSERT INTO course VALUES (2, "JEE", 2); 
INSERT INTO course VALUES (3, "Optimisation Linéaire", 1); 
INSERT INTO course VALUES (4, "Design Patern", 2); 

INSERT INTO grade VALUES(1, "note 1", 8, 3, 1, 1, 5);
INSERT INTO grade VALUES(2, "note 1", 11, 2, 1, 2, 4);
INSERT INTO grade VALUES(3, "note 1", 12, 3, 2, 3, 5);
INSERT INTO grade VALUES(4, "note 1", 14, 2, 2, 4, 4);
INSERT INTO grade VALUES(5, "note 1", 16, 3, 3, 1, 5);
INSERT INTO grade VALUES(6, "note 1", 19, 2, 3, 2, 4);

INSERT INTO lesson VALUES (1, "2024-11-20T08:30:00", "2024-11-20T10:00:00", 1, 4);
INSERT INTO lesson VALUES (2, "2024-12-22T14:45:00", "2024-12-22T16:15:00", 1, 4);

INSERT INTO lesson_class VALUES (1, 1, 1);
INSERT INTO lesson_class VALUES (2, 2, 2);

SELECT * FROM course;
SELECT * FROM classes;
SELECT * FROM lesson;
SELECT * FROM lesson_class;

SELECT * FROM subjects;
SELECT * FROM grade;

SELECT * FROM administrator;
SELECT * FROM teacher;
SELECT * FROM users;
SELECT * FROM users_to_validate;
SELECT * FROM student;
SELECT * FROM major;
