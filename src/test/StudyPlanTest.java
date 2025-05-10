import com.qut.cab302_project_pomodora.model.StudyPlan;

import com.qut.cab302_project_pomodora.model.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class StudyPlanTest{
    private static final int USER_ID = 1;
    private static final int USER_ID_TWO = 4;
    private static final String TITLE = "Advanced Calculus Problem Solving Task";
    private static final String TITLE_TWO = "Networks and Security Exam Preparation";
    private static final String DESCRIPTION = "I want to prepare for my Advanced Calculus problem solving task. I will study the content and then attempt the practice questions. Then I will do the problem solving task";
    private static final String DESCRIPTION_TWO = "I want to study for my Networks and Security exam. I need to study 4 chapters of content for the exam: Routing, Switching, Network Security and Network Protocols. I will study each chapter and then do the practice exam.";
    private static final String STATUS = "IN PROGRESS";
    private static final String STATUS_TWO = "NOT STARTED";
    private static final int PARTICIPANT_COUNT = 5;
    private static final int PARTICIPANT_COUNT_TWO = 10;

    private StudyPlan studyPlan;
    private StudyPlan studyPlanTwo;

    
    @BeforeEach
    public void setUp() {
        studyPlan = new StudyPlan(USER_ID, TITLE, DESCRIPTION, STATUS);
        studyPlanTwo = new StudyPlan(USER_ID_TWO, TITLE_TWO, DESCRIPTION_TWO, STATUS_TWO);
    }

    //TODO: Split these up into separate tests that test each setter individually
    @Test
    public void testSetters() {

        studyPlanTwo.setId(3);
        studyPlanTwo.setUserId(42);
        studyPlanTwo.setTitle("Test Plan");
        studyPlanTwo.setDescription("A plan for testing");
        studyPlanTwo.setStatus("ACTIVE");
        studyPlanTwo.setParticipantCount(5);

        assertEquals(3, studyPlanTwo.getId());
        assertEquals(42, studyPlanTwo.getUserId());
        assertEquals("Test Plan", studyPlanTwo.getTitle());
        assertEquals("A plan for testing", studyPlanTwo.getDescription());
        assertEquals("ACTIVE", studyPlanTwo.getStatus());
        assertEquals(5, studyPlanTwo.getParticipantCount());
    }

    @Test
    public void testParameterizedConstructor() {
        StudyPlan plan = new StudyPlan(7, "Study", "Study description", "PENDING");

        assertEquals(7, plan.getUserId());
        assertEquals("Study", plan.getTitle());
        assertEquals("Study description", plan.getDescription());
        assertEquals("PENDING", plan.getStatus());
    }
    
    @Test
    public void testToStringIncludesKeyFields() {
        StudyPlan plan = new StudyPlan();
        plan.setId(10);
        plan.setUserId(20);
        plan.setTitle("ToStringTest");
        plan.setStatus("DONE");
        plan.setParticipantCount(3);

        String result = plan.toString();
        assertTrue(result.contains("id=10"));
        assertTrue(result.contains("userId=20"));
        assertTrue(result.contains("title='ToStringTest'"));
        assertTrue(result.contains("status='DONE'"));
        assertTrue(result.contains("participantCount=3"));
    }

    @Test
    public void testEmptyTaskListByDefault() {
        StudyPlan plan = new StudyPlan();
        assertNull(plan.tasks(), "Tasks should be null by default unless initialized.");
    }
    
    @Test
    public void testUpdateStudyPlan() {
        
    }
}
