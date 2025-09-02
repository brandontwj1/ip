package Omni.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import Omni.exceptions.CorruptedFileException;
import Omni.tasks.Deadline;
import Omni.tasks.Event;
import Omni.tasks.Task;
import Omni.tasks.Todo;

public class StorageTest {
    @Test
    public void loadTasks_regular_success() throws Exception {
        Path path = Paths.get("src", "test", "java", "Omni", "storage", "loadTasksTest.txt");
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("todo", true));
        tasks.add(new Deadline("deadline", false, "01-01-2025"));
        tasks.add(new Event("event", true, "01-01-2025", "02-02-2025"));
        assertEquals(tasks.toString(), new Storage(path).loadTasks().toString());
    }

    @Test
    public void loadTasks_incorrectType_exceptionThrown() throws Exception {
        Path path = Paths.get("src", "test", "java", "Omni", "storage", "incorrectTypeTest.txt");

        CorruptedFileException exception = assertThrows(CorruptedFileException.class, () -> {
            new Storage(path).loadTasks();
        });

        assertTrue(exception.getMessage().contains("Task type not found."));
    }

    @Test
    public void loadTasks_incorrectLength_exceptionThrown() throws Exception {
        Path path1 = Paths.get("src", "test", "java", "Omni", "storage", "incorrectLengthTest.txt");
        CorruptedFileException exception1 = assertThrows(CorruptedFileException.class, () -> {
            new Storage(path1).loadTasks();
        });
        assertTrue(exception1.getMessage().contains("Entry length invalid."));

        Path path2 = Paths.get("src", "test", "java", "Omni", "storage", "incorrectTodoLengthTest.txt");
        CorruptedFileException exception2 = assertThrows(CorruptedFileException.class, () -> {
            new Storage(path2).loadTasks();
        });
        assertTrue(exception2.getMessage().contains("Entry length for todo invalid."));

        Path path3 = Paths.get("src", "test", "java", "Omni", "storage", "incorrectDeadlineLengthTest.txt");
        CorruptedFileException exception3 = assertThrows(CorruptedFileException.class, () -> {
            new Storage(path3).loadTasks();
        });
        assertTrue(exception3.getMessage().contains("Entry length for deadline invalid."));

        Path path4 = Paths.get("src", "test", "java", "Omni", "storage", "incorrectEventLengthTest.txt");
        CorruptedFileException exception4 = assertThrows(CorruptedFileException.class, () -> {
            new Storage(path4).loadTasks();
        });
        assertTrue(exception4.getMessage().contains("Entry length for event invalid."));
    }
}
