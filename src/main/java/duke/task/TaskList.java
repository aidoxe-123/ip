package duke.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Keeps track of the tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates a task list with elements.
     * @param tasks Elements of the task list
     */
    public TaskList(Collection<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Creates a task list with no elements.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the task list.
     * @param task The task to be added
     */
    public void add(Task task) {
        assert task != null : "task cannot be null";
        tasks.add(task);
    }

    /**
     * Removes the task at the specified position and returns it.
     * @param index The position of the task
     * @return The removed task
     */
    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "index out of bound";
        return tasks.remove(index);
    }

    /**
     * Gets the task at the specified position without removing it.
     * @param index The position of the task
     * @return The task at that position
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "index out of bound";
        return tasks.get(index);
    }

    /**
     * Marks the task at the specified position as done.
     * @param index The position of the task
     */
    public void markAsDone(int index) {
        tasks.set(index, tasks.get(index).markAsDone());
    }

    /**
     * Returns the size of the task list.
     * @return The size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Converts the task list to string.
     * @return The string representation of the task list
     */
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < tasks.size() - 1; i++) {
            str += (i + 1) + ". " + tasks.get(i).toString() + "\n";
        }
        str += tasks.size() + ". " + tasks.get(tasks.size() - 1);
        return str;
    }

    /**
     * Selects tasks that matches the predicate.
     * @param predicate The condition that the selected tasks have to fulfill
     * @return A new <code>TaskList</code> of the selected tasks
     */
    public TaskList filter(Predicate<Task> predicate) {
        assert predicate != null : "predicate cannot be null";
        List<Task> filteredTask = tasks.stream().filter(predicate).collect(Collectors.toList());
        return new TaskList(filteredTask);
    }
}
