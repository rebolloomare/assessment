package omare.com.mx.microservice.service;

import omare.com.mx.microservice.model.Priority;
import omare.com.mx.microservice.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    Task createTask(Task task);

    List<Task> getTasks(Optional<String> dueBefore, Optional<Priority> priority, Optional<Boolean> completed);

    Task getTaskById(UUID id);

    Task updateTask(UUID id, Task task);

    void deleteTask(UUID id);
}
