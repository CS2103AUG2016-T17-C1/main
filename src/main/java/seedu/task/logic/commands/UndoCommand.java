package seedu.task.logic.commands;


/**
 * Lists all tasks in the task manager to the user.
 */
//@@author A0142360U
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    
    //@@author A0127720M
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undoes previous command. \n"
            + "Parameters: not required.\n"
            + "Example: " + COMMAND_WORD +"\n"
    		+ "The Undo command can be executed as many times as needed to the point of application launch.";
    
  //@@author A0142360U
    public static final String MESSAGE_SUCCESS = "Your changes are undone";
    public static final String MESSAGE_FAIL = "No more changes can be undone";


    public UndoCommand() {}

    @Override
    public CommandResult execute() {
        boolean success = model.undoTask();
        if (success)
            return new CommandResult(MESSAGE_SUCCESS);
        return new CommandResult(MESSAGE_FAIL);
    }
}
