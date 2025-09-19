package omni.storage;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import omni.exceptions.CorruptedFileException;
import omni.exceptions.OmniException;
import omni.tasks.Deadline;
import omni.tasks.Event;
import omni.tasks.Task;
import omni.tasks.Todo;

/**
 * Handles reading from and writing to the task storage file.
 * Provides methods to load, update, add, and remove tasks from persistent storage.
 *
 * @author Brandon Tan
 */
public class Storage {
    private Path tasksPath;

    /**
     * Constructs a Storage object with the specified file path.
     *
     * @param filePath The path to the tasks file.
     */
    public Storage(Path filePath) {
        this.tasksPath = filePath;
    }

    /**
     * Loads tasks from the file and returns them as an ArrayList.
     *
     * @return An ArrayList of tasks loaded from the file.
     * @throws OmniException If the file is corrupted or cannot be read, or when date format is invalid.
     */
    public ArrayList<Task> loadTasks() throws OmniException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(tasksPath)) {
            createTasksFile();
            return tasks;
        }

        List<String> lines = getAllLines();
        for (String line : lines) {
            String[] values = getValues(line);
            String type = values[0].trim();
            String description = values[1].trim();
            boolean isDone = parseInt(values[2].trim()) != 0;
            Task taskToAdd = getTaskToAdd(line, type, values, description, isDone);
            tasks.add(taskToAdd);
        }
        return tasks;
    }

    private static Task getTaskToAdd(String line, String type, String[] values, String description, boolean isDone)
            throws OmniException {
        return switch (type) {
        case "T" -> createTodo(line, values, description, isDone);
        case "D" -> createDeadline(line, values, description, isDone);
        case "E" -> createEvent(line, values, description, isDone);
        default -> throw new CorruptedFileException("Task type not found.\n" + line);
        };
    }

    private static Event createEvent(String line, String[] values, String description, boolean isDone)
            throws OmniException {
        if (values.length != 5) {
            throw new CorruptedFileException("Entry length for event invalid.\n" + line);
        }
        return new Event(description, isDone, values[3].trim(), values[4].trim());
    }

    private static Deadline createDeadline(String line, String[] values, String description, boolean isDone)
            throws OmniException {
        if (values.length != 4) {
            throw new CorruptedFileException("Entry length for deadline invalid.\n" + line);
        }
        return new Deadline(description, isDone, values[3].trim());
    }

    private static Todo createTodo(String line, String[] values, String description, boolean isDone)
            throws OmniException {
        if (values.length != 3) {
            throw new CorruptedFileException("Entry length for todo invalid.\n" + line);
        }
        return new Todo(description, isDone);
    }

    private static String[] getValues(String line) throws CorruptedFileException {
        String[] values = line.split("\\|");
        if (values.length < 3 || values.length > 5) {
            throw new CorruptedFileException("Entry length invalid.\n" + line);
        }
        return values;
    }

    private List<String> getAllLines() throws CorruptedFileException {
        List<String> lines;
        try {
            lines = Files.readAllLines(tasksPath);
        } catch (IOException e) {
            throw new CorruptedFileException(e.getMessage());
        }
        return lines;
    }

    /** Create file based on the specified tasksPath */
    private void createTasksFile() throws CorruptedFileException {
        try {
            Files.createDirectories(tasksPath.getParent());
            Files.createFile(tasksPath);
        } catch (IOException e) {
            throw new CorruptedFileException(e.getMessage());
        }
    }

    /**
     * Rewrites the task at the specified index in the file.
     *
     * @param task The task to write.
     * @param index The index of the task to rewrite.
     * @throws IOException If an I/O error occurs.
     */
    public void rewriteTask(Task task, int index) throws IOException {
        assert task != null : "task cannot be null";
        assert index >= 0 : "index must be non-negative";
        List<String> lines = Files.readAllLines(tasksPath);
        lines.remove(index);
        lines.add(index, task.getEntryString());
        Files.write(tasksPath, lines);
    }

    /**
     * Appends a new task to the file.
     *
     * @param task The task to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeTask(Task task) throws IOException {
        Files.writeString(tasksPath, task.getEntryString() + "\n", StandardOpenOption.APPEND);
    }

    /**
     * Erases the task at the specified index from the file.
     *
     * @param index The index of the task to erase.
     * @throws IOException If an I/O error occurs.
     */
    public void eraseTask(int index) throws IOException {
        List<String> lines = Files.readAllLines(tasksPath);
        lines.remove(index);
        Files.write(tasksPath, lines);
    }
}
