package seedu.task.logic;

import javafx.collections.ObservableList;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.commands.CommandResult;
import seedu.task.model.task.ReadOnlyTask;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws IllegalValueException 
     */
    CommandResult execute(String commandText) throws IllegalValueException;

    /** Returns the filtered list of tasks */
    ObservableList<ReadOnlyTask> getFilteredTaskList();
    
  //@@author A0127720M
    /** Returns the filtered list of marked tasks */
    ObservableList<ReadOnlyTask> getFilteredMarkedTaskList();
  //@@author  
    

}
