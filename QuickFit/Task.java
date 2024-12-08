package QuickFit;

public class Task {
    private int taskId;
    private int memoryUsage;
    private String listName;  // The list from which this task's memory block was allocated

    public Task(int taskId, int memoryUsage, String listName) {
        this.taskId = taskId;
        this.memoryUsage = memoryUsage;
        this.listName = listName;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getMemoryUsage() {
        return memoryUsage;
    }

    public String getListName() {
        return listName;
    }

    @Override
    public String toString() {
        return "Task " + taskId + " - memory usage " + memoryUsage;
    }
}
