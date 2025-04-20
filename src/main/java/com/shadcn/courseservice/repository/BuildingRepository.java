package com.shadcn.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shadcn.courseservice.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Long>, QuerydslPredicateExecutor<Building> {
    boolean existsByCode(String code);
}
