package Omni;

import Omni.exceptions.OmniException;
import Omni.storage.Storage;
import Omni.parser.Parser;
import Omni.tasklist.TaskList;
import Omni.ui.Ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Main class for the Omni task management application.
 * Coordinates between UI, storage, task list, and parser components to provide
 * a command-line interface for managing tasks.
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
     * @param filePath the path to the tasks storage file
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
     * Starts the main application loop that handles user input and commands.
     * Continues running until the user enters the exit command "bye".
     */
    public void run() {
        ui.greet();
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        boolean cont = true;

        while (cont) {
            cont = parser.handleInput(input);
            if (cont) {
                input = sc.nextLine().trim();
            }
        }
        sc.close();
    }

    /**
     * Main entry point for the Omni application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Path dataPath = Paths.get("data", "tasks.txt");
        new Omni(dataPath).run();
    }
}
