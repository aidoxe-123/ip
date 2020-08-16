import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    // list storage
    private static ArrayList<Task> tasks = new ArrayList<>();

    // list of commands
    private static String exitCmd = "bye";
    private static String doneCmd = "done";
    private static String deleteCmd = "delete";
    private static String todoCmd = Task.Command.TODO.getCmd();
    private static String deadlineCmd = Task.Command.DEADLINE.getCmd();
    private static String eventCmd = Task.Command.EVENT.getCmd();
    private static String deadlineTimeSpecifier = Task.Command.DEADLINE.getTimeSpecifier();
    private static String eventTimeSpecifier = Task.Command.EVENT.getTimeSpecifier();

    // list of messages
    private static String exitMsg = "Bye human. See you again soon!";
    private static String doneMsg = "Good job bud! I've marked this task as done:";
    private static String deleteMsg = "Noted. I've deleted this task:";
    private static String showTasksMsg = "Here are the task(s) in your lists:";
    private static String addSuccessfulMsg = "Got it. I've added this task:";
    private static String horizontalLine = "________________________________________";
    private static String cmdReq = "Your command: ";
    private static String lazyHumanBash = "You have nothing in your list. Find something to do you human!";
    private static String unrecognizedCmdMsg = "I don't understand a single word you say, human\n"
            + "Speak robot language pls";
    private static String invalidIdxMsg = "Invalid index. Don't you know how to count?";

    private static String taskReport() {
        return "You have " + tasks.size() + " tasks in your list";
    }

    // logo and greeting
    private static String logo = " ____        _        \n"
            + "|  _ \\ _   _| | _____ \n"
            + "| | | | | | | |/ / _ \\\n"
            + "| |_| | |_| |   <  __/\n"
            + "|____/ \\__,_|_|\\_\\___|\n";
    private static String greeting = "Hello human, I'm Duke the supporting chatbot \n"
            + "What can I do for you?";

    // main
    public static void main(String[] args) {
        System.out.println(logo + "\n" + horizontalLine);
        System.out.println(greeting + "\n" + horizontalLine);
        System.out.println(cmdReq);

        // getting command
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String[] inputSplitted = input.split("\\s+", 2);
        while (!input.equals(exitCmd)) {
            System.out.println(horizontalLine);
            try {
                if (input.equals("list")) { // command is "list"
                    if (tasks.size() == 0) {
                        System.out.println(lazyHumanBash);
                    } else {
                        System.out.println(showTasksMsg);
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + ". " + tasks.get(i).toString());
                        }
                    }
                } else if (inputSplitted[0].equals(deleteCmd)) { // command is "delete [index]"
                    try {
                        int idx = Integer.parseInt(inputSplitted[1]) - 1;
                        System.out.println(deleteMsg);
                        System.out.println(tasks.remove(idx));
                        System.out.println(taskReport());
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new InvalidIndexException();
                    }
                } else if (inputSplitted[0].equals(doneCmd)) { // command is "done [index]"
                    try {
                        int idx = Integer.parseInt(inputSplitted[1]) - 1;
                        tasks.set(idx, tasks.get(idx).markAsDone());
                        System.out.println(doneMsg);
                        System.out.println(tasks.get(idx));
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new InvalidIndexException();
                    }
                } else if (inputSplitted[0].equals(todoCmd)) { // command is "todo [description]"
                    if (inputSplitted.length == 1 || inputSplitted[1].equals("")) {
                        throw new InadequateCommandException("todo",
                                new String[] {"description"});
                    } else {
                        Task newTask = new ToDo(inputSplitted[1]);
                        tasks.add(newTask);
                        System.out.println(addSuccessfulMsg);
                        System.out.println(newTask);
                        System.out.println(taskReport());
                    }
                } else if (inputSplitted[0].equals(deadlineCmd) // command is "deadline [description] /by [time]"
                        || inputSplitted[0].equals(eventCmd)) { // or "event [description] / at [time]"
                    String type, timeSpecifier;
                    boolean isDeadline;
                    if (inputSplitted[0].equals(deadlineCmd)) {
                        type = "deadline";
                        timeSpecifier = deadlineTimeSpecifier;
                        isDeadline = true;
                    } else {
                        type = "event";
                        timeSpecifier = eventTimeSpecifier;
                        isDeadline = false;
                    }
                    if (inputSplitted.length == 1) {
                        String[] missing = {"description", "time"};
                        throw new InadequateCommandException(type, missing);
                    } else {
                        String content = inputSplitted[1];
                        String[] split2Test = content.split("\\s+");
                        int timeIdx = content.indexOf(" " + timeSpecifier);
                        if (split2Test.length == 0 ||
                                (split2Test.length == 1 &&
                                        (split2Test[0].equals(timeSpecifier) || split2Test[0].equals(""))
                                )
                        ) {
                            String[] missing = {"description", "time"};
                            throw new InadequateCommandException(type, missing);
                        }
                        if (timeIdx == 0 || content.indexOf(timeSpecifier) == 0) {
                            throw new InadequateCommandException(type,
                                    new String[] {"description"});
                        }
                        if (timeIdx == -1 || timeIdx + 5 >= content.length()) {
                            throw new InadequateCommandException(type,
                                    new String[] {"time"});
                        }
                        String description = content.substring(0, timeIdx);
                        String time = content.substring(timeIdx + 5, content.length());
                        if (time.split("\\s+").length == 0) {
                            throw new InadequateCommandException(type,
                                    new String[] {"time"});
                        }
                        Task newTask = isDeadline
                                ? new Deadline(description, time)
                                : new Event(description, time);
                        tasks.add(newTask);
                        System.out.println(addSuccessfulMsg);
                        System.out.println(newTask);
                        System.out.println(taskReport());
                    }
                } else { // any other commands
                    throw new IncorrectCommandException();
                }
            } catch (InadequateCommandException e) {
                System.out.println(e.getMessage());
            } catch (InvalidIndexException e) {
                System.out.println(invalidIdxMsg);
            } catch (IncorrectCommandException e) {
                System.out.println(unrecognizedCmdMsg);
            }
            System.out.println(horizontalLine);
            System.out.println(cmdReq);
            input = sc.nextLine();
            inputSplitted = input.split("\\s+", 2);
        }

        // exit
        System.out.println(horizontalLine + "\n" +  exitMsg + "\n" + horizontalLine);
    }
}
