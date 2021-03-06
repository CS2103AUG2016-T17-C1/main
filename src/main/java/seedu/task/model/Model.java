package seedu.task.model;

import java.util.Set;

import javafx.collections.ObservableList;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskManager newData);

    /** Returns the TaskManager */
    ReadOnlyTaskManager getTaskManager();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    void deleteMarkedTask(ReadOnlyTask taskToDelete) throws TaskNotFoundException;
    
    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;

    /** Edits the given task. */
    void editTask(ReadOnlyTask target, Task task ) throws UniqueTaskList.TaskNotFoundException;


    /** Undo changes made to the Task List
     * @return */
    boolean undoTask();


    /** Re-do changes made to the Task List
     * @return */
    boolean redoTask();

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();
    
    UnmodifiableObservableList<ReadOnlyTask> getFilteredMarkedTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

  //@@author A0127720M
    /** Marks the given task 
     * @throws DuplicateTaskException */
	void markTask(ReadOnlyTask taskToMark) throws TaskNotFoundException, DuplicateTaskException;
  //@@author	

	
	





}
