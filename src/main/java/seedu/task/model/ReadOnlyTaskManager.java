package seedu.task.model;


import java.util.List;

import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.UniqueMarkedTaskList;
import seedu.task.model.task.UniqueTaskList;

/**
 * Unmodifiable view of an Task Manager
 */
public interface ReadOnlyTaskManager {

    UniqueTagList getUniqueTagList();

    UniqueTaskList getUniqueTaskList();

    /**
     * Returns an unmodifiable view of tasks list
     */
    List<ReadOnlyTask> getTaskList();

    /**
     * Returns an unmodifiable view of tags list
     */
    List<Tag> getTagList();
    
    //@@author A0127720M
	UniqueMarkedTaskList getUniqueMarkedList();
	
	/**
     * Returns an unmodifiable view of unmarked tasks list
     */
    List<ReadOnlyTask> getMarkedTaskList();
    //@@author
}
