package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>, QuerydslPredicateExecutor<Department> {
    Department findByDepartmentName(String departmentName);
}
