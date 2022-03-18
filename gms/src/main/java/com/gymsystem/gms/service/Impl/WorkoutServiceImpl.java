package com.gymsystem.gms.service.Impl;

import com.gymsystem.gms.enumeration.Role;
import com.gymsystem.gms.exceptions.model.WorkoutDateException;
import com.gymsystem.gms.exceptions.model.WorkoutExistException;
import com.gymsystem.gms.exceptions.model.WorkoutNotFoundException;
import com.gymsystem.gms.model.User;
import com.gymsystem.gms.model.Workout;
import com.gymsystem.gms.repository.UserRepository;
import com.gymsystem.gms.repository.WorkoutRepository;
import com.gymsystem.gms.service.WorkoutService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.gymsystem.gms.constraints.UserImplConstant.*;
import static com.gymsystem.gms.constraints.WorkoutConstraint.*;

@Service
@Transactional
public class WorkoutServiceImpl implements WorkoutService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private WorkoutRepository workoutRepository;

    public WorkoutServiceImpl(UserRepository userRepository, WorkoutRepository workoutRepository) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
    }

    @Override
    public List<Workout> getWorkouts() {
        return workoutRepository.findAll();
    }

    @Override
    public Workout createWorkout(String workoutName, String trainerUsername, String roomNumber, Date workoutStartDate, Date workoutEndDate) throws WorkoutDateException, WorkoutExistException {
        checkIfTrainerExists(trainerUsername);
        validateStartEndDate(workoutStartDate,workoutEndDate);
        checkIfWorkoutExists(StringUtils.EMPTY,workoutName,trainerUsername,roomNumber,workoutStartDate,workoutEndDate);
        Workout workout = new Workout();
        workout.setWorkoutName(workoutName);
        workout.setTrainerUsername(trainerUsername);
        workout.setRoomNumber(roomNumber);
        workout.setWorkoutStartDate(workoutStartDate);
        workout.setWorkoutEndDate(workoutEndDate);
        workoutRepository.save(workout);
        //wyslij mail do trenera odnosnie treningu; docelowo dodać do kalenarza
        return workout;
    }

    @Override
    public Workout updateWorkout(String currentWorkoutName, String newWorkoutName, String newTrainerUsername, String newRoomNumber, Date newWorkoutStartDate, Date newWorkoutEndDate) throws WorkoutDateException, WorkoutExistException {
        checkIfTrainerExists(newTrainerUsername);
        validateStartEndDate(newWorkoutStartDate,newWorkoutEndDate);
        Workout workout = checkIfWorkoutExists(currentWorkoutName,newWorkoutName,newTrainerUsername,newRoomNumber,newWorkoutStartDate,newWorkoutEndDate);
        workout.setWorkoutName(newWorkoutName);
        workout.setTrainerUsername(newTrainerUsername);
        workout.setRoomNumber(newRoomNumber);
        workout.setWorkoutStartDate(newWorkoutStartDate);
        workout.setWorkoutEndDate(newWorkoutEndDate);
        workoutRepository.save(workout);
        return workout;
    }

    @Override
    public void deleteWorkout(Long id) throws WorkoutNotFoundException {
        Workout workout = workoutRepository.findWorkoutById(id);
        if(workout == null){
            throw new WorkoutNotFoundException(NO_WORKOUT_FOUND_BY_ID+id);
        }
        workoutRepository.deleteById(id);
    }
    private void checkIfTrainerExists(String trainerUsername) {
        User trainer = userRepository.findUserByUsername(trainerUsername);
        if (trainer == null) {
            throw new UsernameNotFoundException(NO_TRAINER_FOUND_BY_USERNAME + trainerUsername);
        }
        if(trainer.getRole()==Role.ROLE_COACH.toString()){
            throw new UsernameNotFoundException(USER_IS_NOT_TRAINER  + trainerUsername);
        }
    }

    private void validateStartEndDate(Date workoutStartDate, Date workoutEndDate) throws WorkoutDateException {
        if(workoutEndDate==null || workoutStartDate==null)return;
        if(workoutEndDate.after(workoutStartDate)){
            throw new WorkoutDateException(WORKOUT_DATE_INVALID);
        }
    }

    private Workout checkIfWorkoutExists(String currentWorkout, String workoutName, String trainerUsername, String roomNumber, Date workoutStartDate, Date workoutEndDate) throws WorkoutExistException {
        if(currentWorkout.isEmpty()){
            Workout workout = workoutRepository.findWorkoutByWorkoutNameAndRoomNumberAndWorkoutEndDateAndWorkoutStartDateAndTrainerUsername(workoutName,roomNumber,workoutEndDate,workoutStartDate,trainerUsername);
            if (workout != null) {
                throw new WorkoutExistException(WORKOUT_ALREADY_EXISTS + workoutName);
            }
            return workout;
        }
        else{
            Workout workout = workoutRepository.findWorkoutByWorkoutNameAndRoomNumberAndWorkoutEndDateAndWorkoutStartDateAndTrainerUsername(currentWorkout,roomNumber,workoutEndDate,workoutStartDate,trainerUsername);
            if (workout != null) {
                throw new WorkoutExistException(WORKOUT_ALREADY_EXISTS + workoutName);
            }
            return workout;
        }
    }
}
