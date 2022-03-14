package com.gymsystem.gms.controller;


import com.gymsystem.gms.exceptions.model.*;
import com.gymsystem.gms.model.Workout;
import com.gymsystem.gms.service.WorkoutService;
import com.gymsystem.gms.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value ="/workout")
@AllArgsConstructor
public class WorkoutController {
    @Autowired
    private WorkoutService workoutService;

    @PostMapping("/add")
    public ResponseEntity<Workout> addNewWorkout(@RequestParam("workoutName") String workoutName,
                                                    @RequestParam("trainerUsername") String trainerUsername,
                                                    @RequestParam("roomNumber") String roomNumber,
                                                    @RequestParam("workoutStartDate") Date workoutStartDate,
                                                    @RequestParam("workoutEndDate") Date workoutEndDate) throws WorkoutDateException, WorkoutExistException {
        Workout newWorkout = workoutService.createWorkout(workoutName,trainerUsername,roomNumber,workoutStartDate,workoutEndDate);
        return new ResponseEntity<>(newWorkout, OK);
    }
    @GetMapping("/list")
    public ResponseEntity<List<Workout>> getAllUsers() {
        List<Workout> workouts = workoutService.getWorkouts();
        return new ResponseEntity<>(workouts, OK);
    }


}
