package seedu.address.logic;

import com.google.common.eventbus.Subscribe;

import seedu.task.commons.core.Config;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.events.model.TaskManagerChangedEvent;
import seedu.task.commons.events.ui.JumpToListRequestEvent;
import seedu.task.commons.events.ui.ShowHelpRequestEvent;
import seedu.task.logic.Logic;
import seedu.task.logic.LogicManager;
import seedu.task.logic.commands.*;
import seedu.task.model.TaskManager;
import seedu.task.model.Model;
import seedu.task.model.ModelManager;
import seedu.task.model.ReadOnlyTaskManager;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.*;
import seedu.task.storage.StorageManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.task.commons.core.Messages.*;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;
    private Config config;

    // These are for checking the correctness of the events raised
    private ReadOnlyTaskManager latestSavedTaskManager;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskManagerChangedEvent abce) {
        latestSavedTaskManager = new TaskManager(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setup() {
        model = new ModelManager();
        String tempTaskManagerFile = saveFolder.getRoot().getPath() + "TempTaskManager.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempTaskManagerFile, tempPreferencesFile), config);
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskManager = new TaskManager(model.getTaskManager()); // last
                                                                          // saved
                                                                          // assumed
                                                                          // to
                                                                          // be
                                                                          // up
                                                                          // to
                                                                          // date
                                                                          // before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    //@@author A0139284X
    
    @Test
    public void execute_undoAtStartOfApplication() throws Exception {
        assertCommandBehavior(UndoCommand.COMMAND_WORD, UndoCommand.MESSAGE_FAIL);
    }
    
    @Test
    public void execute_undo_multipleUndoAtStartOfApplication() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1));
        model.addTask(helper.generateTask(2));
        model.addTask(helper.generateTask(3));
        
        assertCommandBehavior(UndoCommand.COMMAND_WORD + " 4", String.format(UndoCommand.MESSAGE_SUCCESS, 3));
    }
    
    @Test
    public void execute_help_addCommand() throws Exception {
        assertCommandBehavior("help " + AddCommand.COMMAND_WORD, AddCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_editCommand() throws Exception {
        assertCommandBehavior("help " + EditCommand.COMMAND_WORD, EditCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_deleteCommand() throws Exception {
        assertCommandBehavior("help " + DeleteCommand.COMMAND_WORD, DeleteCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_deleteMarkedCommand() throws Exception {
        assertCommandBehavior("help " + DeleteMarkedCommand.COMMAND_WORD, DeleteMarkedCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_markCommand() throws Exception {
        assertCommandBehavior("help " + MarkCommand.COMMAND_WORD, MarkCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_undoCommand() throws Exception {
        assertCommandBehavior("help " + UndoCommand.COMMAND_WORD, UndoCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_redoCommand() throws Exception {
        assertCommandBehavior("help " + RedoCommand.COMMAND_WORD, RedoCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_selectCommand() throws Exception {
        assertCommandBehavior("help " + SelectCommand.COMMAND_WORD, SelectCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_clearCommand() throws Exception {
        assertCommandBehavior("help " + ClearCommand.COMMAND_WORD, ClearCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_clearMarkedCommand() throws Exception {
        assertCommandBehavior("help " + ClearMarkedCommand.COMMAND_WORD, ClearMarkedCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_findCommand() throws Exception {
        assertCommandBehavior("help " + FindCommand.COMMAND_WORD, FindCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_listCommand() throws Exception {
        assertCommandBehavior("help " + ListCommand.COMMAND_WORD, ListCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_exitCommand() throws Exception {
        assertCommandBehavior("help " + ExitCommand.COMMAND_WORD, ExitCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_helpCommand() throws Exception {
        assertCommandBehavior("help " + HelpCommand.COMMAND_WORD, HelpCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_bareCommand() throws Exception {
        assertCommandBehavior("help " + BareCommand.COMMAND_WORD, BareCommand.MESSAGE_USAGE);
    }
    
    @Test
    public void execute_help_changeDirectoryCommand() throws Exception {
        assertCommandBehavior("help " + ChangeDirectoryCommand.COMMAND_WORD, ChangeDirectoryCommand.MESSAGE_USAGE);
    }
    
    //@@author
    
    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'address book' and the 'last shown list' are expected to be
     * empty.
     * 
     * @see #assertCommandBehavior(String, String, ReadOnlyTaskManager, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new TaskManager(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's
     * state are as expected:<br>
     * - the internal address book data are same as those in the
     * {@code expectedAddressBook} <br>
     * - the backing list shown by UI matches the {@code shownList} <br>
     * - {@code expectedAddressBook} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
            ReadOnlyTaskManager expectedTaskManager, List<? extends ReadOnlyTask> expectedShownList) throws Exception {

        // Execute the command
        CommandResult result = logic.execute(inputCommand);

        // Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredTaskList());

        // Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedTaskManager, model.getTaskManager());
        assertEquals(expectedTaskManager, latestSavedTaskManager);
    }

    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
    }

    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }
/*
    @Test
    public void execute_clear() throws Exception {
        model.resetData(new TaskManager());
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1));
        model.addTask(helper.generateTask(2));
        model.addTask(helper.generateTask(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new TaskManager(), Collections.emptyList());
    }
*/
    // @@author A0139284X
    @Test
    public void execute_add_FloatTasks() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.floating();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded), expectedAB, expectedAB.getTaskList());

    }

    // @@author

    @Test
    public void execute_add_invalidTaskData() throws Exception {
        assertCommandBehavior("add []\\[;] d/11125678 e/valid@e.mail i/valid, address",
                TaskName.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior("add Valid TaskName d/not_numbers e/valid@e.mail i/valid, address",
                Date.MESSAGE_DATE_CONSTRAINTS);
        assertCommandBehavior("add Valid TaskName d/31125678 e/notAnEmail i/valid, address",
                Time.MESSAGE_TIME_CONSTRAINTS);
        assertCommandBehavior("add Valid TaskName d/11115678 e/valid@e.mail i/valid, address t/invalid_-[.tag",
                Tag.MESSAGE_TAG_CONSTRAINTS);

    }

    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded), expectedAB, expectedAB.getTaskList());

    }

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeAdded);

        // setup starting state
        model.addTask(toBeAdded); // person already in internal address book

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded), AddCommand.MESSAGE_DUPLICATE_TASK, expectedAB,
                expectedAB.getTaskList());

    }

    @Test
    public void execute_list_showsAllTasks() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        TaskManager expectedAB = helper.generateTaskManager(2);
        List<? extends ReadOnlyTask> expectedList = expectedAB.getTaskList();

        // prepare address book state
        helper.addToModel(model, 2);

        assertCommandBehavior("list", ListCommand.MESSAGE_SUCCESS, expectedAB, expectedList);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single person in the shown list, using visible index.
     * 
     * @param commandWord
     *            to test assuming it targets a single person in the last shown
     *            list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage)
            throws Exception {
        assertCommandBehavior(commandWord, expectedMessage); // index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); // index
                                                                     // should
                                                                     // be
                                                                     // unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); // index
                                                                     // should
                                                                     // be
                                                                     // unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); // index
                                                                    // cannot be
                                                                    // 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single person in the shown list, using visible index.
     * 
     * @param commandWord
     *            to test assuming it targets a single person in the last shown
     *            list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> personList = helper.generateTaskList(2);

        // set AB state to 2 persons
        model.resetData(new TaskManager());
        for (Task p : personList) {
            model.addTask(p);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getTaskManager(), personList);
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    @Test
    public void execute_select_jumpsToCorrectPerson() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threePersons = helper.generateTaskList(3);

        TaskManager expectedAB = helper.generateTaskManager(threePersons);
        helper.addToModel(model, threePersons);

        assertCommandBehavior("select 2", String.format(SelectCommand.MESSAGE_SELECT_TASK_SUCCESS, 2), expectedAB,
                expectedAB.getTaskList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getFilteredTaskList().get(1), threePersons.get(1));
    }

    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_delete_removesCorrectPerson() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threeTasks = helper.generateTaskList(3);

        TaskManager expectedAB = helper.generateTaskManager(threeTasks);
        expectedAB.removeTask(threeTasks.get(1));
        helper.addToModel(model, threeTasks);

        assertCommandBehavior("delete 2", String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, threeTasks.get(1)),
                expectedAB, expectedAB.getTaskList());
    }

    @Test
    public void execute_find_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find ", expectedMessage);
    }

    @Test
    public void execute_find_canMatchPartialWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task tFull1 = helper.generateTaskWithName("bla bla KEY bla");
        Task tFull2 = helper.generateTaskWithName("bla KEY bla bceofeia");
        Task tNotPartial = helper.generateTaskWithName("KE Y");
        Task tPartial = helper.generateTaskWithName("KEYKEYKEY sduauo");

        List<Task> fourTasks = helper.generateTaskList(tNotPartial, tFull1, tPartial, tFull2);
        TaskManager expectedAB = helper.generateTaskManager(fourTasks);
        List<Task> expectedList = helper.generateTaskList(tFull1, tPartial, tFull2);
        helper.addToModel(model, fourTasks);

        assertCommandBehavior("find KEY", Command.getMessageForTaskListShownSummary(expectedList.size()), expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = helper.generateTaskWithName("bla bla KEY bla");
        Task p2 = helper.generateTaskWithName("bla KEY bla bceofeia");
        Task p3 = helper.generateTaskWithName("key key");
        Task p4 = helper.generateTaskWithName("KEy sduauo");

        List<Task> fourPersons = helper.generateTaskList(p3, p1, p4, p2);
        TaskManager expectedAB = helper.generateTaskManager(fourPersons);
        List<Task> expectedList = fourPersons;
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("find KEY", Command.getMessageForTaskListShownSummary(expectedList.size()), expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generateTaskWithName("bla bla KEY bla");
        Task pTarget2 = helper.generateTaskWithName("bla rAnDoM bla bceofeia");
        Task pTarget3 = helper.generateTaskWithName("key key");
        Task p1 = helper.generateTaskWithName("sduauo");

        List<Task> fourPersons = helper.generateTaskList(pTarget1, p1, pTarget2, pTarget3);
        TaskManager expectedAB = helper.generateTaskManager(fourPersons);
        List<Task> expectedList = helper.generateTaskList(pTarget1, pTarget2, pTarget3);
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("find key rAnDoM", Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedAB, expectedList);
    }

    // @@author A0139284X

    @Test
    public void execute_edit_changeTaskName() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();
        Task afterEdit = helper.adamChangeTaskName();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(afterEdit);

        // set AB state to 1 person
        model.resetData(new TaskManager());
        model.addTask(toBeAdded);
        
        model.editTask(toBeAdded, afterEdit);

        // execute command and verify result
        assertCommandBehavior("edit 1 Not Adam Brown", String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, afterEdit), expectedAB,
                expectedAB.getTaskList());

    }
    
    @Test
    public void execute_edit_changeDeadlineTaskToFloating() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.deadlineTask();
        Task afterEdit = helper.floating();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(afterEdit);

        // set AB state to 1 person
        model.resetData(new TaskManager());
        model.addTask(toBeAdded);
        
        model.editTask(toBeAdded, afterEdit);

        // execute command and verify result
        assertCommandBehavior("edit 1 sd/- d/- i/-", String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, afterEdit), expectedAB,
                expectedAB.getTaskList());

    }

    @Test
    public void execute_edit_changeStartAndEndTime() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();
        Task afterEdit = helper.adamChangeTime();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(afterEdit);

        // set AB state to 1 person
        model.resetData(new TaskManager());
        model.addTask(toBeAdded);
        
        model.editTask(toBeAdded, afterEdit);

        // execute command and verify result
        assertCommandBehavior("edit 1 st/0900 e/1000", String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, afterEdit), expectedAB,
                expectedAB.getTaskList());

    }
    
    @Test
    public void execute_undo_addCommandToEmptyList() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();

        // set AB state to 1 person
        model.resetData(new TaskManager());
        model.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(UndoCommand.COMMAND_WORD, String.format(UndoCommand.MESSAGE_SUCCESS, 1), new TaskManager(),
                Collections.emptyList());

    }

    @Test
    public void execute_undo_addCommandToNonEmptyList() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();
        TaskManager expectedAB = new TaskManager();
        List<Task> personList = helper.generateTaskList(2);

        for (Task p : personList) {
            expectedAB.addTask(p);
        }

        // set AB state to 2 persons
        model.resetData(new TaskManager());
        for (Task p : personList) {
            model.addTask(p);
        }

        model.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(UndoCommand.COMMAND_WORD, String.format(UndoCommand.MESSAGE_SUCCESS, 1), expectedAB,
                expectedAB.getTaskList());

    }

    @Test
    public void execute_undo_deleteCommand() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeDeleted = helper.adam();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeDeleted);

        // set AB state to 1 person
        model.resetData(new TaskManager());
        model.addTask(toBeDeleted);
        
        model.deleteTask(toBeDeleted);

        // execute command and verify result
        assertCommandBehavior(UndoCommand.COMMAND_WORD, String.format(UndoCommand.MESSAGE_SUCCESS, 1), expectedAB,
                expectedAB.getTaskList());

    }
    
    @Test
    public void execute_undo_clearCommand() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeDeleted = helper.adam();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeDeleted);

        // set AB state to 1 person
        model.resetData(new TaskManager());
        model.addTask(toBeDeleted);
        
        model.resetData(new TaskManager());

        // execute command and verify result
        assertCommandBehavior(UndoCommand.COMMAND_WORD, String.format(UndoCommand.MESSAGE_SUCCESS, 1), expectedAB,
                expectedAB.getTaskList());

    }

    @Test
    public void execute_undo_editCommand() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeEdited = helper.adam();
        Task afterEdit = helper.adamChangeTaskName();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeEdited);

        // set AB state to 1 person
        model.resetData(new TaskManager());
        model.addTask(toBeEdited);
        
        model.editTask(toBeEdited, afterEdit);

        // execute command and verify result
        assertCommandBehavior(UndoCommand.COMMAND_WORD, String.format(UndoCommand.MESSAGE_SUCCESS, 1), expectedAB,
                expectedAB.getTaskList());
        
    }

    @Test
    public void execute_mark_markFirstTask() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        TaskManager expectedAB = new TaskManager();
        List<Task> personList = helper.generateTaskList(2);
        Task toBeMarked = personList.get(0);

        expectedAB.addTask(personList.get(1));

        // set AB state to 2 persons
        model.resetData(new TaskManager());
        for (Task p : personList) {
            model.addTask(p);
        }

        // execute command and verify result
        assertCommandBehavior(MarkCommand.COMMAND_WORD + " 1", String.format(MarkCommand.MESSAGE_SUCCESS, personList.get(0)), expectedAB,
                expectedAB.getTaskList());
        
    }
    
    //@@author

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {

        Task adam() throws Exception {
            TaskName taskName = new TaskName("Adam Brown");
            Date date = new Date("11112111");
            Time time = new Time("0900");
            EventStart eventStart = new EventStart(new Date("11112111"), new Time("0800"));
            Deadline deadline = new Deadline(date, time);
            Importance importance = new Importance("**");
            Tag tag1 = new Tag("tag1");
            Tag tag2 = new Tag("tag2");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Task(taskName, eventStart, deadline, importance, tags);
        }
        
        Task adamChangeTime() throws Exception {
            TaskName taskName = new TaskName("Adam Brown");
            Date date = new Date("11112111");
            Time time = new Time("1000");
            EventStart eventStart = new EventStart(new Date("11112111"), new Time("0900"));
            Deadline deadline = new Deadline(date, time);
            Importance importance = new Importance("**");
            Tag tag1 = new Tag("tag1");
            Tag tag2 = new Tag("tag2");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Task(taskName, eventStart, deadline, importance, tags);
        }
        
        Task adamChangeTaskName() throws Exception {
            TaskName taskName = new TaskName("Not Adam Brown");
            Date date = new Date("11112111");
            Time time = new Time("2359");
            EventStart eventStart = new EventStart(new Date(""), new Time(""));
            Deadline deadline = new Deadline(date, time);
            Importance importance = new Importance("**");
            Tag tag1 = new Tag("tag1");
            Tag tag2 = new Tag("tag2");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Task(taskName, eventStart, deadline, importance, tags);
        }

        /**
         * @return a floating task.
         */

        Task floating() throws Exception {
            TaskName taskName = new TaskName("Floater");
            Date date = new Date("");
            Time time = new Time("");
            EventStart eventStart = new EventStart(date, time);
            Deadline deadline = new Deadline(date, time);
            Importance importance = new Importance("");
            UniqueTagList tags = new UniqueTagList();
            return new Task(taskName, eventStart, deadline, importance, tags);
        }

        /**
         * @return a floating task.
         */

        Task deadlineTask() throws Exception {
            TaskName taskName = new TaskName("Floater");
            Date date = new Date("03112016");
            Time time = new Time("1700");
            EventStart eventStart = new EventStart(new Date(""), new Time(""));
            Deadline deadline = new Deadline(date, time);
            Importance importance = new Importance("");
            UniqueTagList tags = new UniqueTagList();
            return new Task(taskName, eventStart, deadline, importance, tags);
        }
        
        /**
         * Generates a valid person using the given seed. Running this function
         * with the same parameter values guarantees the returned person will
         * have the same state. Each unique seed will generate a unique Task
         * object.
         *
         * @param seed
         *            used to generate the person data field values
         */
        Task generateTask(int seed) throws Exception {
            return new Task(new TaskName("Task " + seed),
                    new EventStart(new Date("" + (31129989 - Math.abs(seed))), new Time("" + (Math.abs(seed) + 1200))),
                    new Deadline(new Date("" + (31129999 - Math.abs(seed))), new Time("" + (Math.abs(seed) + 1200))),
                    new Importance(new String(new char[seed]).replace("\0", "*")),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1))));
        }

        /** Generates the correct add command based on the person given */
        String generateAddCommand(Task p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");

            cmd.append(p.getName().toString());
            cmd.append(" sd/").append(p.getEventStart().getStartDate().toString());
            cmd.append(" st/").append(p.getEventStart().getStartTime().toString());
            cmd.append(" d/").append(p.getDeadline().getDueDate().toString());
            cmd.append(" e/").append(p.getDeadline().getDueTime().toString());
            cmd.append(" i/").append(p.getImportance());

            UniqueTagList tags = p.getTags();
            for (Tag t : tags) {
                cmd.append(" t/").append(t.tagName);
            }

            return cmd.toString();
        }

        /**
         * Generates an TaskManager with auto-generated persons.
         */
        TaskManager generateTaskManager(int numGenerated) throws Exception {
            TaskManager taskManager = new TaskManager();
            addToTaskManager(taskManager, numGenerated);
            return taskManager;
        }

        /**
         * Generates an TaskManager based on the list of Persons given.
         */
        TaskManager generateTaskManager(List<Task> tasks) throws Exception {
            TaskManager taskManager = new TaskManager();
            addToTaskManager(taskManager, tasks);
            return taskManager;
        }

        /**
         * Adds auto-generated Task objects to the given TaskManager
         * 
         * @param taskManager
         *            The TaskManager to which the Persons will be added
         */
        void addToTaskManager(TaskManager taskManager, int numGenerated) throws Exception {
            addToTaskManager(taskManager, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given TaskManager
         */
        void addToTaskManager(TaskManager taskManager, List<Task> tasksToAdd) throws Exception {
            for (Task p : tasksToAdd) {
                taskManager.addTask(p);
            }
        }

        /**
         * Adds auto-generated Task objects to the given model
         * 
         * @param model
         *            The model to which the Persons will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given model
         */
        void addToModel(Model model, List<Task> tasksToAdd) throws Exception {
            for (Task p : tasksToAdd) {
                model.addTask(p);
            }
        }

        /**
         * Generates a list of Persons based on the flags.
         */
        List<Task> generateTaskList(int numGenerated) throws Exception {
            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                tasks.add(generateTask(i));
            }
            return tasks;
        }

        List<Task> generateTaskList(Task... tasks) {
            return Arrays.asList(tasks);
        }

        /**
         * Generates a Task object with given name. Other fields will have some
         * dummy values.
         */
        Task generateTaskWithName(String name) throws Exception {
            return new Task(new TaskName(name), new EventStart(new Date("25124678"), new Time("0000")),
                    new Deadline(new Date("25125678"), new Time("0000")), new Importance("**"),
                    new UniqueTagList(new Tag("tag")));
        }
        
    }
}
