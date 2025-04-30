import com.qut.cab302_project_pomodora.model.StudyPlan;

import com.qut.cab302_project_pomodora.model.Task;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



class StudyPlanTest{

    @Test
    public void testDefaultConstructorAndSetters() {
        StudyPlan plan = new StudyPlan();
        plan.setId(1);
        plan.setUserId(42);
        plan.setTitle("Test Plan");
        plan.setDescription("A plan for testing");
        plan.setStatus("ACTIVE");
        plan.setParticipantCount(5);

        assertEquals(1, plan.getId());
        assertEquals(42, plan.getUserId());
        assertEquals("Test Plan", plan.getTitle());
        assertEquals("A plan for testing", plan.getDescription());
        assertEquals("ACTIVE", plan.getStatus());
        assertEquals(5, plan.getParticipantCount());
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
    public void testSetAndGetTasks() {
        Task task1 = new Task(); // Assuming Task has a no-arg constructor
        Task task2 = new Task();
        List<Task> taskList = List.of(task1, task2);

        StudyPlan plan = new StudyPlan();
        plan.setTasks(taskList);

        assertEquals(2, plan.tasks().size());
        assertSame(task1, plan.tasks().get(0));
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
}
