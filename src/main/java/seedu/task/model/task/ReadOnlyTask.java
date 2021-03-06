package seedu.task.model.task;

import seedu.task.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in the task manager.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    TaskName getName();
    Deadline getDeadline();
    EventStart getEventStart();
    Importance getImportance();


    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getDeadline().getDueDate().equals(this.getDeadline().getDueDate())
                && other.getDeadline().getDueTime().equals(this.getDeadline().getDueTime())
                && other.getEventStart().getStartDate().equals(this.getEventStart().getStartDate())
                && other.getEventStart().getStartTime().equals(this.getEventStart().getStartTime())
                && other.getImportance().equals(this.getImportance()));
    }

    /**
     * Formats the task as text, showing all details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Start Date: ")
                .append(getEventStart().getStartDate())
                .append(" Start Time: ")
                .append(getEventStart().getStartTime())
                .append(" End Date: ")
                .append(getDeadline().getDueDate())
                .append(" End Time: ")
                .append(getDeadline().getDueTime())
                .append(" Importance: ")
                .append(getImportance())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Returns a string representation of this Task's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }

}
