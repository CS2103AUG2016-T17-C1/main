package seedu.task.model.task;

import java.util.Objects;

import seedu.task.commons.util.CollectionUtil;
import seedu.task.model.tag.UniqueTagList;

/**
 * Represents a Task in the task manager.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private TaskName taskName;
    private Deadline deadline;
    private EventStart eventStart;
    private Importance importance;

    private boolean isTaskCompleted;

	private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(TaskName taskName, EventStart eventStart,Deadline deadline, Importance importance, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(taskName,eventStart.getStartDate(),eventStart.getStartTime(), deadline.getDueDate(), deadline.getDueTime(), importance, tags);
        this.taskName = taskName;
        this.eventStart = eventStart;
        this.deadline = deadline;
        this.importance = importance;
        this.isTaskCompleted = false;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(),source.getEventStart(), source.getDeadline(), source.getImportance(), source.getTags());
    }

    public Task(EventStart eventStart, Deadline deadline, Importance importance, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(eventStart.getStartDate(), eventStart.getStartTime(),deadline.getDueDate(), deadline.getDueTime(), importance, tags);
        this.eventStart = eventStart;
        this.deadline = deadline;
        this.importance = importance;
        this.tags = new UniqueTagList(tags);
        // TODO Auto-generated constructor stub
    }
    
    //@@author A0127720M
    public void markAsCompleted() {
    	this.isTaskCompleted = true;
    }
    
    public boolean isTaskCompleted() {
		return isTaskCompleted;
	}

    //@@author
    
    
    @Override
    public TaskName getName() {
        return taskName;
    }
//
//    @Override
//    public Date getDueDate() {
//        return getDeadLine().getDueDate();
//    }
//
//    @Override
//    public Time getDueTime() {
//        return getDeadLine().getDueTime();
//    }

    @Override
    public Importance getImportance() {
        return importance;
    }


    public void setName(TaskName name) {
        this.taskName = name;
    }

    public void setDueDate(Date date) {
        this.getDeadline().setDueDate(date);
    }

    public void setDueTime(Time time) {
        this.getDeadline().setDueTime(time);
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    @Override
    public UniqueTagList getTags() {
        return tags;
    }

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(taskName, getDeadline(), importance, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public EventStart getEventStart() {
        return eventStart;
    }

    public void setEventStart(EventStart eventStart) {
        this.eventStart = eventStart;
    }

    public void setDeadLine(Deadline deadline) {
        this.deadline = deadline;
    }

    public void setStartDate(Date startDate) {
        this.getEventStart().setStartDate(startDate);
    }

    public void setStartTime(Time startTime) {
        this.getEventStart().setStartTime(startTime);
    }

}