import java.util.ArrayList;
import java.util.function.Predicate;

public class TaskList {
    ArrayList<Task> tasks;
    
    TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;    
    }
    
    TaskList() {
        this.tasks = new ArrayList<>();
    }
    
    public void add(Task task) {
        tasks.add(task);
    }
    
    public Task remove(int index) {
        return tasks.remove(index);
    }
    
    public Task get(int index) {
        return tasks.get(index);
    }
    
    public void markAsDone(int index) {
        tasks.set(index, tasks.get(index).markAsDone());
    }
    
    public int size() {
        return tasks.size();
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < tasks.size(); i++) {
            str += (i + 1) + ". " + tasks.get(i).toString() + "\n";
        }
        return str;
    }
    
    public TaskList filter(Predicate<Task> predicate) {
        ArrayList<Task> filtered = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (predicate.test(task)) {
                filtered.add(task);
            }
        }
        return new TaskList(filtered);
    }
}
