import api.IAdmin;
import api.IInstructor;
import api.IStudent;
import api.core.impl.Admin;
import api.core.impl.Instructor;
import api.core.impl.Student;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pryu on 3/6/2017.
 */

public class TestInstructor {
    private IAdmin admin;
    private IInstructor instructor;
    private IStudent student;

    @Before
    public void setup() {
        this.admin = new Admin();
        this.admin.createClass("Test", 2017, "Instructor", 15);
        this.instructor = new Instructor();
        this.student = new Student();
        this.student.registerForClass("Alan", "Test", 2017);
    }

    @Test
    public void testAddHomeworkSucceeds() {
        this.instructor.addHomework("Instructor", "Test", 2017, "hw1", "homework 1");
        assertTrue(this.admin.classExists("Test", 2017));
        assertTrue(this.admin.getClassInstructor("Test", 2017) == "Instructor");
        assertTrue(this.instructor.homeworkExists("Test", 2017, "hw1"));
    }

    @Test
    public void testAddHomeworkInvalidInstructorFails() {
        this.instructor.addHomework("FakeInstructor", "Test", 2017, "hw1", "homework 1");
        assertTrue(this.admin.classExists("Test", 2017));
        assertFalse(this.admin.getClassInstructor("Test", 2017) == "FakeInstructor");
        assertFalse(this.instructor.homeworkExists("Test", 2017, "hw1"));
    }

    @Test
    public void testAddHomeworkInvalidClassFails() {
        this.instructor.addHomework("Instructor", "FakeClass", 2017, "hw1", "homework 1");
        assertFalse(this.admin.classExists("FakeClass", 2017));
        assertTrue(this.admin.getClassInstructor("Test", 2017) == "Instructor");
        assertFalse(this.instructor.homeworkExists("FakeClass", 2017, "hw1"));
    }

    @Test
    public void testAddHomeworkInvalidYearFails() {
        this.instructor.addHomework("Instructor", "Test", 2016, "hw1", "homework 1");
        assertFalse(this.admin.classExists("Test", 2016));
        assertEquals(this.admin.getClassInstructor("Test", 2016), "Instructor");
        assertFalse(this.instructor.homeworkExists("Test", 2016, "hw1"));
    }

    @Test
    public void testAssignGradeSucceeds() {
        testAddHomeworkSucceeds();
        this.student.submitHomework("Alan", "hw1", "answer", "Test", 2017);

        this.instructor.assignGrade("Instructor", "Test", 2017, "hw1", "Alan", 100);
        assertTrue(this.student.hasSubmitted("Alan", "hw1", "Test", 2017));
        assertTrue(this.admin.classExists("Test", 2017));
        assertEquals(this.admin.getClassInstructor("Test", 2017), "Instructor");
        assertTrue(this.instructor.homeworkExists("Test", 2017, "hw1"));
        assertTrue(this.instructor.getGrade("Test", 2017, "hw1", "Alan") >= 0);
    }

    @Test
    public void testAssignGradeInvalidInstructorFails() {
        testAddHomeworkSucceeds();
        this.student.submitHomework("Alan", "hw1", "answer", "Test", 2017);

        this.instructor.assignGrade("FakeInstructor", "Test", 2017, "hw1", "Alan", 100);
        assertTrue(this.student.hasSubmitted("Alan", "hw1", "Test", 2017));
        assertTrue(this.admin.classExists("Test", 2017));
        assertEquals(this.admin.getClassInstructor("Test", 2017), "FakeInstructor");
        assertFalse(this.instructor.homeworkExists("Test", 2017, "hw1"));
        assertFalse(this.instructor.getGrade("Test", 2017, "hw1", "Alan") >= 0);
    }

    @Test
    public void testAssignGradeInvalidClassFails() {
        testAddHomeworkSucceeds();
        this.student.submitHomework("Alan", "hw1", "answer", "Test", 2017);

        this.instructor.assignGrade("Instructor", "FakeClass", 2017, "hw1", "Alan", 100);
        assertTrue(this.student.hasSubmitted("Alan", "hw1", "Test", 2017));
        assertFalse(this.admin.classExists("FakeClass", 2017));
        assertEquals(this.admin.getClassInstructor("FakeClass", 2017), "Instructor");
        assertFalse(this.instructor.homeworkExists("FakeClass", 2017, "hw1"));
        assertFalse(this.instructor.getGrade("FakeClass", 2017, "hw1", "Alan") >= 0);
    }

    @Test
    public void testAssignGradeInvalidYearFails() {
        testAddHomeworkSucceeds();
        this.student.submitHomework("Alan", "hw1", "answer", "Test", 2017);

        this.instructor.assignGrade("Instructor", "Test", 2016, "hw1", "Alan", 100);
        assertTrue(this.student.hasSubmitted("Alan", "hw1", "Test", 2017));
        assertFalse(this.admin.classExists("Test", 2016));
        assertEquals(this.admin.getClassInstructor("Test", 2016), "Instructor");
        assertFalse(this.instructor.homeworkExists("Test", 2016, "hw1"));
        assertFalse(this.instructor.getGrade("Test", 2016, "hw1", "Alan") >= 0);
    }

    @Test
    public void testAssignGradeInvalidHWFails() {
        testAddHomeworkSucceeds();
        this.student.submitHomework("Alan", "hw1", "answer", "Test", 2017);

        this.instructor.assignGrade("Instructor", "Test", 2017, "hw2", "Alan", 100);
        assertFalse(this.instructor.homeworkExists("Test", 2017, "hw2"));
        assertFalse(this.student.hasSubmitted("Alan", "hw2", "Test", 2017));
        assertTrue(this.admin.classExists("Test", 2017));
        assertEquals(this.admin.getClassInstructor("Test", 2017), "Instructor");

        //assertFalse(this.instructor.getGrade("Test", 2017, "hw2", "Alan") >= 0);
    }

    @Test
    public void testAssignGradeStudentDidNotSubmitFails(){
        testAddHomeworkSucceeds();

        this.instructor.assignGrade("Instructor", "Test", 2017, "hw1", "Alan", 100);
        assertFalse(this.student.hasSubmitted("Alan", "hw1", "Test", 2017));
        assertTrue(this.admin.classExists("Test", 2017));
        assertEquals(this.admin.getClassInstructor("Test", 2017), "Instructor");
        assertTrue(this.instructor.homeworkExists("Test", 2017, "hw1"));
        assertFalse(this.instructor.getGrade("Test", 2017, "hw1", "Alan") >= 0);
    }

}
