import exceptions.OmniException;

import java.util.Scanner;

public class Omni {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    public Omni(String filePath) {
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

    public static void main(String[] args) {
        new Omni("data/tasks.txt").run();
    }
}
