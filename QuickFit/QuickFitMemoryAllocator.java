package QuickFit;

import java.util.ArrayList;

public class QuickFitMemoryAllocator {

    // Free lists categorized by block size
    private ArrayList<MemoryBlock> listSmall = new ArrayList<>();
    private ArrayList<MemoryBlock> listMedium = new ArrayList<>();
    private ArrayList<MemoryBlock> listLarge = new ArrayList<>();

    // List to store running tasks
    private ArrayList<Task> tasks = new ArrayList<>();
    private int taskIdCounter = 1;

    // Method to allocate memory
    public String allocate(int size) {
        MemoryBlock allocatedBlock = null;
        String listName = "";

        // Try to allocate from the small blocks list
        if (size <= 30 && !listSmall.isEmpty()) {
            allocatedBlock = allocateFromList(listSmall, size);
            listName = "Small list";
        }
        // Try to allocate from the medium blocks list
        else if (size <= 80 && !listMedium.isEmpty()) {
            allocatedBlock = allocateFromList(listMedium, size);
            listName = "Medium list";
        }
        // Try to allocate from the large blocks list
        else if (size <= 200 && !listLarge.isEmpty()) {
            allocatedBlock = allocateFromList(listLarge, size);
            listName = "Large list";
        }

        // Check if no suitable block was found in any list
        if (allocatedBlock == null) {
            return "Out of memory: No suitable block found for " + size + " bytes.";
        }

        // If the allocated block is larger than needed, return the remaining memory
        // back to the same list
        if (allocatedBlock.getSize() > size) {
            int remainingSize = allocatedBlock.getSize() - size;
            MemoryBlock remainingBlock = new MemoryBlock(remainingSize);

            // Return the remaining block to the same list from which it was allocated
            if (listName.equals("Small list")) {
                listSmall.add(remainingBlock);
            } else if (listName.equals("Medium list")) {
                listMedium.add(remainingBlock);
            } else {
                listLarge.add(remainingBlock);
            }

            System.out.println("Remaining " + remainingSize + " bytes returned to the " + listName);
        }

        // Register the task and display the current running tasks
        tasks.add(new Task(taskIdCounter++, size, listName)); // Add the task with its memory usage and list name
        return "\nAllocation successful: Allocated " + size + " bytes.\nStored: " + listName + "\n#########################################";
    }

    // Helper method to allocate memory from a list
    private MemoryBlock allocateFromList(ArrayList<MemoryBlock> list, int requestSize) {
        for (int i = 0; i < list.size(); i++) {
            MemoryBlock block = list.get(i);

            // Check if the block is large enough
            if (block.getSize() >= requestSize) {
                // Remove the block from the list
                list.remove(i);

                // If there’s remaining memory, split it and return the remainder to the same
                // list
                if (block.getSize() > requestSize) {
                    int remainingSize = block.getSize() - requestSize;
                    MemoryBlock remainingBlock = new MemoryBlock(remainingSize);

                    // Add the remaining block back to the same list
                    list.add(i, remainingBlock); // Add the remaining block back to the same list
                }

                // Return the allocated block
                return new MemoryBlock(requestSize);
            }
        }
        return null;
    }

    // Method to free memory and return it to the appropriate list
    public void free(MemoryBlock block, String listName) {
        ArrayList<MemoryBlock> targetList = null;

        // Determine the correct list based on the listName
        if (listName.equals("Small list")) {
            targetList = listSmall;
        } else if (listName.equals("Medium list")) {
            targetList = listMedium;
        } else if (listName.equals("Large list")) {
            targetList = listLarge;
        }

        // If the target list is found, insert the block in the correct order
        if (targetList != null) {
            // Add the block back while maintaining the order
            int insertIndex = 0;
            // Find the correct position for the block
            for (int i = 0; i < targetList.size(); i++) {
                if (block.getSize() < targetList.get(i).getSize()) {
                    insertIndex = i;
                    break;
                } else {
                    insertIndex = i + 1; // If we are at the last element, insert at the end
                }
            }

            // Insert the block at the determined index
            targetList.add(insertIndex, block);
            System.out.println("Freed " + block.getSize() + " bytes and returned to " + listName);

            // Sort the list to ensure it's in the correct order
            sortMemoryList(targetList);
        }
    }

    // Helper method to sort the memory block list by size
    private void sortMemoryList(ArrayList<MemoryBlock> list) {
        // Sort the list in ascending order by block size
        list.sort((block1, block2) -> Integer.compare(block1.getSize(), block2.getSize()));
    }

    // Method to free a task by task ID
    public void freeTaskById(int taskId) {
        Task taskToFree = null;

        // Find the task in the list
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                taskToFree = task;
                break;
            }
        }

        if (taskToFree != null) {
            // Remove the task from the running tasks
            tasks.remove(taskToFree);
            System.out.println("Task " + taskId + " - memory usage " + taskToFree.getMemoryUsage() + " freed.");

            // Free the memory block and return it to the appropriate list
            int taskMemorySize = taskToFree.getMemoryUsage();
            String taskListName = taskToFree.getListName();
            MemoryBlock blockToReturn = new MemoryBlock(taskMemorySize);

            free(blockToReturn, taskListName); // Free memory and return it to the correct list
        } else {
            System.out.println("Task ID not found!");
        }
    }

    // Method to print the state of the free lists
    public void printState() {
        System.out.println("\n******* Current memory state *******");
        // Print each list with block sizes
        System.out.println("Small list " + listToString(listSmall));
        System.out.println("Medium list " + listToString(listMedium));
        System.out.println("Large list " + listToString(listLarge));

        // Display the running tasks and their memory usage
        System.out.println("\n******* Current running tasks *******");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    // Helper method to convert a list of memory blocks to a string representation
    private String listToString(ArrayList<MemoryBlock> list) {
        if (list.isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getSize());
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // initialize the memory lists
    public void initialize() {
        listSmall.add(new MemoryBlock(20));
        listSmall.add(new MemoryBlock(30));
        listMedium.add(new MemoryBlock(50));
        listMedium.add(new MemoryBlock(80));
        listLarge.add(new MemoryBlock(100));
        listLarge.add(new MemoryBlock(200));
    }
}
