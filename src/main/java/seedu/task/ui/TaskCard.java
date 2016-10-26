package seedu.task.ui;

import javafx.fxml.FXML;
import seedu.task.ui.CheckTaskAttributes;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.task.model.task.ReadOnlyTask;

public class TaskCard extends UiPart {

    private static final String FXML = "TaskListCard.fxml";
    public static final String EMPTY_TASK_OBJECT_STRING = "";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label dueDate;
    @FXML
    private Label importance;
    @FXML
    private Label dueTime;
    @FXML
    private Label tags;

    private ReadOnlyTask task;
    private int displayedIndex;

    public TaskCard() {

    }

    public static TaskCard load(ReadOnlyTask task, int displayedIndex) {
        TaskCard card = new TaskCard();
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {

        CheckTaskAttributes checkTask = new CheckTaskAttributes(task);

        name.setText("Task: " + task.getName().fullName);
        id.setText(displayedIndex + ". ");

        //Checks if task has a start date or an end date and displays the appropriate text output on the card
        if (checkTask.startDateExists() && checkTask.endDateExists()) {
            dueDate.setManaged(true);
            dueDate.setText("Starts on " + task.getEventStart().getStartDate().toString() + " and to be completed by "
                    + task.getDeadLine().getDueDate().toString());

        } else if (checkTask.endDateExists()) {
            dueDate.setManaged(true);
            dueDate.setText("Task to be completed by Date: " + task.getDeadLine().getDueDate().toString());

        } else if (checkTask.startDateExists()){
            dueDate.setManaged(true);
            dueDate.setText("Task to start on Date: " + task.getEventStart().getStartDate().toString());

        } else
            dueDate.setManaged(false);

        //Checks if task has a start or an end time and displays the appropriate text output on the card
        if (checkTask.startTimeExists() && checkTask.endTimeExists()) {
            dueTime.setManaged(true);
            dueTime.setText("Starts at time " + task.getEventStart().getStartTime().value + " and ends at time "
                    + task.getDeadLine().getDueTime().value + "hours");
        }

        else if (checkTask.endTimeExists()) {
            dueTime.setManaged(true);
            dueTime.setText("Ends at time " + task.getDeadLine().getDueTime().value + "hours");
        }

        else if (checkTask.startTimeExists()) {
            dueTime.setManaged(true);
            dueTime.setText("Starts at time " + task.getEventStart().getStartTime().value + "hours");
        }

        else
            dueTime.setManaged(false);

        //Checks if the task's importance level has been set and displays the appropriate text output on the card
        if (!task.getImportance().value.toString().equals(EMPTY_TASK_OBJECT_STRING)) {
            importance.setManaged(true);
            importance.setText("Importance: " + task.getImportance().value);

        } else
            importance.setManaged(false);



        tags.setText(task.tagsString());

    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
