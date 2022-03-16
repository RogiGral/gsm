package com.gymsystem.gms.service;

import com.gymsystem.gms.exceptions.model.WorkoutDateException;
import com.gymsystem.gms.exceptions.model.WorkoutExistException;
import com.gymsystem.gms.exceptions.model.WorkoutNotFoundException;
import com.gymsystem.gms.model.User;
import com.gymsystem.gms.model.Workout;

import java.util.Date;
import java.util.List;

public interface WorkoutService {
    List<Workout> getWorkouts();
    Workout createWorkout(String workoutName, String trainerUsername,String roomNumber, Date workoutStartDate, Date workoutEndDate) throws WorkoutDateException, WorkoutExistException;
    //Workout createWorkout(String workoutName, String trainerUsername, String roomNumber);
    Workout updateWorkout(String currentWorkoutName,String newWorkoutName, String newTrainerUsername,String newRoomNumber, Date newWorkoutStartDate, Date newWorkoutEndDate) throws WorkoutDateException, WorkoutExistException;
    void deleteWorkout(Long id) throws WorkoutNotFoundException;
}
