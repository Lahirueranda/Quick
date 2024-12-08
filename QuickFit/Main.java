package QuickFit;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        QuickFitMemoryAllocator allocator = new QuickFitMemoryAllocator();

        // Initialize the free memory lists with some blocks
        allocator.initialize();
        allocator.printState();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Show the menu with options
            System.out.println("\nSelect an option:");
            System.out.println("1 - Memory Allocation");
            System.out.println("2 - Process End (Free Memory Block)");
            System.out.println("0 - Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Memory Allocation
                    System.out.println("\nEnter memory size to allocate (in bytes):");
                    int size = scanner.nextInt();
                    if (size > 0) {
                        // Perform memory allocation
                        String allocationResult = allocator.allocate(size);
                        System.out.println(allocationResult);
                    }
                    break;

                case 2:
                    // Process End (Free Memory Block)
                    System.out.println("\nEnter the task number to free (corresponding memory size will be freed):");
                    int taskId = scanner.nextInt();
                    allocator.freeTaskById(taskId); // Free the memory block associated with the task
                    break;

                case 0:
                    // Exit the program
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice, please select again.");
                    break;
            }

            // Print the current memory state
            allocator.printState();
        }
    }
}
