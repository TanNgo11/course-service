package com.shadcn.courseservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.shadcn.courseservice.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, QuerydslPredicateExecutor<Room> {

    boolean existsByCode(String code);

    Optional<Room> findByCode(String code);

    List<Room> findByBuildingId(Long buildingId);

    Room getRoomById(Long roomId);
}
