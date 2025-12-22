import com.qut.cab302_project_pomodora.model.StudyTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private static final int FIXED_USER_ID = 1;
    private static final int STUDY_PLAN_ID_FOR_USER = 101;
    private StudyTask task;

    @BeforeEach
    public void setUp() {
        task = new StudyTask(1, STUDY_PLAN_ID_FOR_USER, 1, "Read Chapter 1", "Read and summarise chapter 1", "Pending");
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(1, task.getId());
        assertEquals(STUDY_PLAN_ID_FOR_USER, task.getStudyPlanId());
        assertEquals(1, task.getTaskNumber());
        assertEqualsIgnoreSpaces("Read Chapter 1", task.getTitle());
        assertEqualsIgnoreSpaces("Read and summarise chapter 1", task.getDescription());
        assertEqualsIgnoreSpaces("Pending", task.getStatus());
    }

    @Test
    public void testSetId() {
        task.setId(2);
        assertEquals(2, task.getId());
    }

    @Test
    public void testSetTaskNumber() {
        task.setTaskNumber(2);
        assertEquals(2, task.getTaskNumber());
    }

    @Test
    public void testSetTitle() {
        task.setTitle("Review Chapter 1");
        assertEqualsIgnoreSpaces("Review Chapter 1", task.getTitle());
    }

    @Test
    public void testSetDescription() {
        task.setDescription("Summarise key points of chapter 1");
        assertEqualsIgnoreSpaces("Summarise key points of chapter 1", task.getDescription());
    }

    @Test
    public void testSetStatusInProgress() {
        task.setStatus("In Progress");
        assertEqualsIgnoreSpaces("In Progress", task.getStatus());
    }

    @Test
    public void testSetStatusIncomplete() {
        task.setStatus("INCOMPLETE");
        assertEqualsIgnoreSpaces("INCOMPLETE", task.getStatus());
    }



    @Test
    public void testToString() {
        String expected = "Task{id=1, studyPlanId=" + STUDY_PLAN_ID_FOR_USER + ", title='Read Chapter 1', status='Pending}";
        assertEqualsIgnoreSpaces(expected, task.toString());
    }


    private void assertEqualsIgnoreSpaces(String expected, String actual) {
        String cleanedExpected = expected.replaceAll("\\s+", "");
        String cleanedActual = actual.replaceAll("\\s+", "");
        assertEquals(cleanedExpected, cleanedActual);
    }


}

