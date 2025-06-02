package omare.com.mx.microservice.service;

import omare.com.mx.microservice.exceptions.InvalidTaskException;
import omare.com.mx.microservice.exceptions.TaskNotFoundException;
import omare.com.mx.microservice.model.Priority;
import omare.com.mx.microservice.model.Task;
import omare.com.mx.microservice.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task validTask;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validTask = new Task();
        validTask.setTitle("Test Task");
        validTask.setDueDate(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        validTask.setPriority(Priority.MEDIUM);
        validTask.setCompleted(false);
    }

    @Test
    public void createTask_success() {
        when(taskRepository.save(validTask)).thenReturn(validTask);
        Task result = taskService.createTask(validTask);
        assertEquals(validTask, result);
        verify(taskRepository).save(validTask);
    }

    @Test
    public void createTask_fail_missingTitle() {
        validTask.setTitle(null);
        assertThrows(InvalidTaskException.class, () -> taskService.createTask(validTask));
    }

    @Test
    public void createTask_fail_dueDateInPast() {
        validTask.setDueDate(java.sql.Date.valueOf(LocalDate.now().minusDays(1)));
        assertThrows(InvalidTaskException.class, () -> taskService.createTask(validTask));
    }

    @Test
    public void getTasks_noFilters_success() {
        when(taskRepository.findAll()).thenReturn(List.of(validTask));
        List<Task> result = taskService.getTasks(Optional.empty(), Optional.empty(), Optional.empty());
        assertEquals(1, result.size());
    }

    @Test
    public void getTasks_dueBefore_success() {
        when(taskRepository.findAll()).thenReturn(List.of(validTask));
        String date = LocalDate.now().plusDays(2).toString();
        List<Task> result = taskService.getTasks(Optional.of(date), Optional.empty(), Optional.empty());
        assertEquals(1, result.size());
    }

    @Test
    public void getTasks_priorityFilter_returnsEmptyDueToReturnFalse() {
        when(taskRepository.findAll()).thenReturn(List.of(validTask));
        List<Task> result = taskService.getTasks(Optional.empty(), Optional.of(Priority.MEDIUM), Optional.empty());
        assertTrue(result.isEmpty());
    }

    @Test
    public void getTasks_completedFilter_success() {
        when(taskRepository.findAll()).thenReturn(List.of(validTask));
        List<Task> result = taskService.getTasks(Optional.empty(), Optional.empty(), Optional.of(false));
        assertEquals(1, result.size());
    }

    @Test
    public void getTaskById_success() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.of(validTask));
        Task result = taskService.getTaskById(id);
        assertEquals(validTask, result);
    }

    @Test
    public void getTaskById_notFound() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(id));
    }

    @Test
    public void updateTask_success() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.of(validTask));
        when(taskRepository.save(any())).thenReturn(validTask);

        Task update = new Task();
        update.setTitle("Updated");
        update.setDueDate(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        update.setPriority(Priority.HIGH);
        update.setCompleted(true);

        Task result = taskService.updateTask(id, update);
        assertEquals("Updated", result.getTitle());
        assertEquals(Priority.HIGH, result.getPriority());
        assertTrue(result.isCompleted());
    }

    @Test
    public void updateTask_fail_dueDateInPast() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.of(validTask));

        Task update = new Task();
        update.setDueDate(java.sql.Date.valueOf(LocalDate.now().minusDays(1)));

        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(id, update));
    }

    @Test
    public void updateTask_notFound() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        Task update = new Task();
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(id, update));
    }

    @Test
    public void deleteTask_success() {
        UUID id = UUID.randomUUID();
        when(taskRepository.existsById(id)).thenReturn(true);
        taskService.deleteTask(id);
        verify(taskRepository).deleteById(id);
    }

    @Test
    public void deleteTask_notFound() {
        UUID id = UUID.randomUUID();
        when(taskRepository.existsById(id)).thenReturn(false);
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(id));
    }
}
