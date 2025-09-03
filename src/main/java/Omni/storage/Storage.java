package Omni.storage;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import Omni.exceptions.CorruptedFileException;
import Omni.tasks.Deadline;
import Omni.tasks.Event;
import Omni.tasks.Task;
import Omni.tasks.Todo;

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
     * @param filePath the path to the tasks file
     */
    public Storage(Path filePath) {
        this.tasksPath = filePath;
    }

    /**
     * Loads tasks from the file and returns them as an ArrayList.
     *
     * @return an ArrayList of tasks loaded from the file
     * @throws CorruptedFileException if the file is corrupted or cannot be read
     */
    public ArrayList<Task> loadTasks() throws CorruptedFileException {
        if (!Files.exists(tasksPath)) {
            try {
                Files.createDirectories(tasksPath.getParent());
                Files.createFile(tasksPath);
            } catch (IOException e) {
                throw new CorruptedFileException(e.getMessage());
            }
        }

        ArrayList<Task> tasks = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(tasksPath);
            for (String line : lines) {
                String[] values = line.split("\\|");
                if (values.length < 3 || values.length > 5) {
                    throw new CorruptedFileException("Entry length invalid.\n" + line);
                } else {
                    String type = values[0].trim();
                    String desc = values[1].trim();
                    boolean isDone = parseInt(values[2].trim()) != 0;
                    switch (type) {
                    case "T":
                        if (values.length != 3) {
                            throw new CorruptedFileException("Entry length for todo invalid.\n" + line);
                        }
                        tasks.add(new Todo(desc, isDone));
                        break;
                    case "D":
                        if (values.length != 4) {
                            throw new CorruptedFileException("Entry length for deadline invalid.\n" + line);
                        }
                        tasks.add(new Deadline(desc, isDone, values[3].trim()));
                        break;
                    case "E":
                        if (values.length != 5) {
                            throw new CorruptedFileException("Entry length for event invalid.\n" + line);
                        }
                        tasks.add(new Event(desc, isDone, values[3].trim(), values[4].trim()));
                        break;
                    default:
                        throw new CorruptedFileException("Task type not found.\n" + line);
                    }
                }
            }
        } catch (IOException e) {
            throw new CorruptedFileException(e.getMessage());
        }

        return tasks;
    }

    /**
     * Rewrites the task at the specified index in the file.
     *
     * @param task the task to write
     * @param index the index of the task to rewrite
     * @throws IOException if an I/O error occurs
     */
    public void rewriteTask(Task task, int index) throws IOException {
        List<String> lines = Files.readAllLines(tasksPath);
        lines.remove(index);
        lines.add(index, task.getEntryString());
        Files.write(tasksPath, lines);
    }

    /**
     * Appends a new task to the file.
     *
     * @param task the task to write
     * @throws IOException if an I/O error occurs
     */
    public void writeTask(Task task) throws IOException {
        Files.writeString(tasksPath, task.getEntryString() + "\n", StandardOpenOption.APPEND);
    }

    /**
     * Erases the task at the specified index from the file.
     *
     * @param index the index of the task to erase
     * @throws IOException if an I/O error occurs
     */
    public void eraseTask(int index) throws IOException {
        List<String> lines = Files.readAllLines(tasksPath);
        lines.remove(index);
        Files.write(tasksPath, lines);
    }

}
