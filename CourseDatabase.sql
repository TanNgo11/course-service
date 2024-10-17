CREATE DATABASE `course-service`;
USE `course-service`;

INSERT INTO `course-service`.academic_year (id, created_by, createddate, modifiedby, modifieddate, end_year,
                                            start_year)
VALUES (1, 'admin', '2024-10-24', null, null, '2025-06-10', '2024-10-01'),
       (2, 'admin', '2024-10-24', null, null, '2026-06-10', '2025-10-01'),
       (3, 'admin', '2024-10-24', null, null, '2027-06-10', '2026-10-01'),
       (4, 'admin', '2024-10-24', null, null, '2028-06-10', '2027-10-01');

INSERT INTO `course-service`.semester (id, created_by, createddate, modifiedby, modifieddate, end_date, name,
                                       start_date, academic_year_id)
VALUES (1, 'admin', '2024-10-24', null, null, '2024-12-15', 'Fall', '2024-10-01', null),
       (2, 'admin', '2024-10-24', null, null, '2025-03-20', 'Spring', '2025-01-05', null),
       (3, 'admin', '2024-10-24', null, null, '2025-06-10', 'Summer', '2025-05-01', null),
       (4, 'admin', '2024-10-24', null, null, '2025-12-10', 'Winter', '2025-09-20', null);

INSERT INTO `course-service`.department (id, created_by, createddate, modifiedby, modifieddate, department_name)
VALUES (1, 'admin', '2024-10-11', null, null, 'CNTT'),
       (2, 'admin', '2024-10-11', null, null, 'DIEU DUONG'),
       (3, 'admin', '2024-10-11', null, null, 'KINH TE'),
       (4, 'admin', '2024-10-11', null, null, 'KY THUAT');

INSERT INTO `course` (`id`, `created_by`, `createddate`, `modifiedby`, `modifieddate`, `name`)
VALUES (1, 'admin', '2024-10-13', NULL, NULL, 'BSC 110'),
       (2, 'admin', '2024-10-13', NULL, NULL, 'BSC 202'),
       (3, 'admin', '2024-10-13', NULL, NULL, 'CSE 101'),
       (4, 'admin', '2024-10-13', NULL, NULL, 'CSE 102'),
       (5, 'admin', '2024-10-13', NULL, NULL, 'ACTG 240'),
       (6, 'admin', '2024-10-13', NULL, NULL, 'ACTG 243'),
       (7, 'admin', '2024-10-13', NULL, NULL, 'ECE 201'),
       (8, 'admin', '2024-10-13', NULL, NULL, 'ECE 203');
