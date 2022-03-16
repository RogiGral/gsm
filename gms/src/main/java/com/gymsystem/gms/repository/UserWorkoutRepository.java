package com.gymsystem.gms.repository;

import com.gymsystem.gms.model.UserWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWorkoutRepository extends JpaRepository<UserWorkout,Long> {
    @Query(value = "SELECT * FROM user_workout WHERE user_id = :id",nativeQuery = true)
    List<UserWorkout> findAllByUserId(@Param("id") Long id);
}
