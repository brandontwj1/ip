package Omni.tasklist;

import java.util.ArrayList;

import Omni.tasks.Task;

public class TaskList {

    private ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public int getSize() {
        return tasks.size();
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public Task markTaskDone(int index) {
        Task t = getTask(index);
        t.markDone();
        return t;
    }

    public Task unmarkTaskDone(int index) {
        Task t = getTask(index);
        t.unmarkDone();
        return t;
    }

    public Task addTask(Task task) {
        tasks.add(task);
        return task;
    }

    public Task removeTask(int index) {
        return tasks.remove(index);
    }
}
