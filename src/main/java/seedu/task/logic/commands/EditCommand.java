package seedu.task.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.DueDate;
import seedu.task.model.task.DueTime;
import seedu.task.model.task.Importance;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskName;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";



    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": edits the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";

    public final int targetIndex;
    private final Task toEdit;

    public EditCommand(String string, String taskName, String dueDate, String dueTime, String importance, Set<String> tags) throws IllegalValueException {
        System.out.println("Target index"+string);
        this.targetIndex = Integer.parseInt(string);

        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.toEdit = new Task(
                new TaskName(taskName),
                new DueDate(dueDate),
                new DueTime(dueTime),
                new Importance(importance),
                new UniqueTagList(tagSet)
        );
    }


    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToEdit = lastShownList.get(targetIndex - 1);


        try {
            model.editTask(taskToEdit,this.toEdit);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, taskToEdit));
    }


}