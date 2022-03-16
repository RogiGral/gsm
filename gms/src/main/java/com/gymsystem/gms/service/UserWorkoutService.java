package com.gymsystem.gms.service;

import com.gymsystem.gms.exceptions.model.WorkoutDateException;
import com.gymsystem.gms.exceptions.model.WorkoutExistException;
import com.gymsystem.gms.exceptions.model.WorkoutNotFoundException;
import com.gymsystem.gms.model.UserWorkout;
import com.gymsystem.gms.model.Workout;

import java.util.Date;
import java.util.List;

public interface UserWorkoutService {
    List<UserWorkout> getUserWorkouts(Long id);
    UserWorkout addUserToWorkout(Long userId, Long workoutId) throws WorkoutNotFoundException;
    void deleteUserWorkout(Long id) throws WorkoutNotFoundException;
}
