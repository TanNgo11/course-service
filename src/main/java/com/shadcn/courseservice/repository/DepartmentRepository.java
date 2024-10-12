package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shadcn.courseservice.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDepartmentName(String departmentName);
}
