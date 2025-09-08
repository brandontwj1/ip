package omni.app;

import java.nio.file.Path;

import omni.exceptions.OmniException;
import omni.parser.Parser;
import omni.storage.Storage;
import omni.tasklist.TaskList;
import omni.ui.Ui;

/**
 * Main class for the Omni task management application.
 * Coordinates between UI, storage, task list, and parser components to provide
 * GUI interfaces for managing tasks.
 *
 * @author Brandon Tan
 */
public class Omni {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    /**
     * Constructs an Omni application with the specified file path for task storage.
     * Initializes all components and loads existing tasks from storage.
     *
     * @param filePath The path to the tasks storage file.
     */
    public Omni(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (OmniException e) {
            ui.showLoadingError(e.getUserMessage());
            tasks = new TaskList();
        }
        parser = new Parser(ui, tasks, storage);
    }


    /**
     * Processes user input and returns the appropriate response.
     * This method is used for GUI interface mode to handle user commands.
     *
     * @param input The user's input command string.
     * @return The response message from processing the command.
     */
    public String getResponse(String input) {
        return parser.handleInput(input);
    }

    /**
     * Returns the greeting message for the application.
     * Used to display the initial welcome message to users.
     *
     * @return The greeting message string.
     */
    public String greet() {
        return ui.greet();
    }
}
