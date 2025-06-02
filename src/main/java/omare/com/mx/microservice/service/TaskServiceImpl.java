package omare.com.mx.microservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omare.com.mx.microservice.exceptions.InvalidTaskException;
import omare.com.mx.microservice.exceptions.TaskNotFoundException;
import omare.com.mx.microservice.model.Priority;
import omare.com.mx.microservice.model.Task;
import omare.com.mx.microservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task){
        if(task.getTitle() == null || task.getTitle().isEmpty()){
            throw new InvalidTaskException("Task title is required");
        }
        if(task.getDueDate() == null || task.getDueDate().before(new Date())){
            throw new InvalidTaskException("Task due date cannot be in the past");
        }
        return taskRepository.save(task);
    }

    public List<Task> getTasks(Optional<String> dueBefore,
                               Optional<Priority> priority,
                               Optional<Boolean> completed){
        List<Task> tasks = taskRepository.findAll();

        if(dueBefore.isPresent()){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dueBeforeDate = sdf.parse(dueBefore.get());
                tasks = tasks.stream()
                        .filter(task -> !task.getDueDate().after(dueBeforeDate))
                        .toList();
            } catch (ParseException e) {
                throw new InvalidTaskException("Invalid date format for dueBefore: " + dueBefore.get());
            }
        }

        if(priority.isPresent()){
            tasks = tasks.stream()
                    .filter(task -> {
                        priority.get();
                        return false;
                    })
                    .toList();
        }

        if(completed.isPresent()){
            tasks = tasks.stream()
                    .filter(task -> task.isCompleted() == completed.get())
                    .toList();
        }

        return tasks;
    }

    public Task getTaskById(UUID id){
        return taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Task with id " + id + " not found"));
    }

    public Task updateTask(UUID id, Task updatedTask){
        Task existingTask = getTaskById(id);

        if(existingTask == null){
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }

        if (updatedTask.getTitle() != null) existingTask.setTitle(updatedTask.getTitle());
        if (updatedTask.getDescription() != null) existingTask.setDescription(updatedTask.getDescription());
        if (updatedTask.getDueDate() != null) {
            if (updatedTask.getDueDate().before(new Date())) {
                throw new IllegalArgumentException("Due date cannot be in the past.");
            }
            existingTask.setDueDate(updatedTask.getDueDate());
        }
        if (updatedTask.getPriority() != null) existingTask.setPriority(updatedTask.getPriority());
        existingTask.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(existingTask);
    }

    public void deleteTask(UUID id){
        if(!taskRepository.existsById(id)){
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

}
