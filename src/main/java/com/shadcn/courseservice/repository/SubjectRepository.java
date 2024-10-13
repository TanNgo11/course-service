package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>, QuerydslPredicateExecutor<Subject> {}
