package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadcn.courseservice.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {}
