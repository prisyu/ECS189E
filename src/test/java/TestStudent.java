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
public class TestStudent {
    private IAdmin admin;
    private IInstructor instructor;
    private IStudent student;

    @Before
    public void setup() {
        this.admin = new Admin();
        this.admin.createClass("Test", 2017, "Instructor", 15);
        this.instructor = new Instructor();
        this.instructor.addHomework("Instructor", "Test", 2017, "hw1", "hw1 description");
        this.student = new Student();
    }

    @Test
    public void testRegisterForClassSucceeds(){
        this.student.registerForClass("Bob", "Test", 2017);
        assertTrue(this.admin.classExists("Test", 2017));
        assertTrue(this.student.isRegisteredFor("Bob", "Test", 2017));
    }

    @Test
    public void testRegisterForClassInvalidClassFails(){
        this.student.registerForClass("Bob", "FakeClass", 2017);
        assertFalse(this.admin.classExists("FakeClass", 2017));
        assertFalse(this.student.isRegisteredFor("Bob", "FakeClass", 2017));
    }

    @Test
    public void testRegisterForClassInvalidYearFails(){
        this.student.registerForClass("Bob", "Test", 2016);
        assertFalse(this.admin.classExists("Test", 2016));
        assertFalse(this.student.isRegisteredFor("Bob", "Test", 2016));
    }

    @Test
    public void testRegisterForClassMaxCapacityFails(){
        this.admin.createClass("Test", 2017, "Instructor", 1);
        this.student.registerForClass("Bob", "Test", 2017);
        assertFalse(this.student.isRegisteredFor("Bob", "Test", 2017));
    }

    @Test
    public void testDropClassSucceeds(){
        testRegisterForClassSucceeds();

        assertTrue(this.student.isRegisteredFor("Bob", "Test", 2017));
        this.student.dropClass("Bob", "Test", 2017);
        assertTrue(this.admin.classExists("Test", 2017));
        assertTrue(!this.student.isRegisteredFor("Bob", "Test", 2017));
    }

    @Test
    public void testDropClassFails(){
        testRegisterForClassSucceeds();

        assertFalse(this.student.isRegisteredFor("Bobby", "Test", 2017));
        this.student.dropClass("Bobby", "Test", 2017);
        assertTrue(this.admin.classExists("Test", 2017));
        assertFalse(!this.student.isRegisteredFor("Bobby", "Test", 2017));
    }

    @Test
    public void testDropClassInvaliClassFails(){
        testRegisterForClassSucceeds();

        this.student.dropClass("Bob", "FakeClass", 2017);
        assertFalse(this.admin.classExists("FakeClass", 2017));
        assertFalse(!this.student.isRegisteredFor("Bob", "FakeClass", 2017));
    }

    @Test
    public void testDropClassInvaliYearFails(){
        testRegisterForClassSucceeds();

        this.student.dropClass("Bob", "Test", 2016);
        assertFalse(this.admin.classExists("Test", 2016));
        assertFalse(!this.student.isRegisteredFor("Bob", "Test", 2016));
    }

    @Test
    public void testSubmitHomeworkSucceeds(){
        testRegisterForClassSucceeds();

        this.student.submitHomework("Bob", "hw1", "answer1", "Test", 2017);
        assertTrue(this.admin.classExists("Test", 2017));
        assertTrue(this.instructor.homeworkExists("Test", 2017, "hw1"));
        assertTrue(this.student.isRegisteredFor("Bob", "Test", 2017));
        assertTrue(this.student.hasSubmitted("Bob", "hw1", "Test", 2017));
    }

    @Test
    public void testSubmitHomeworkInvalidStudentSFails(){
        testRegisterForClassSucceeds();

        this.student.submitHomework("Bobby", "hw1", "answer1", "Test", 2017);
        assertTrue(this.admin.classExists("Test", 2017));
        assertTrue(this.instructor.homeworkExists("Test", 2017, "hw1"));
        assertFalse(this.student.isRegisteredFor("Bobby", "Test", 2017));
        assertFalse(this.student.hasSubmitted("Bobby", "hw1", "Test", 2017));
    }

    @Test
    public void testSubmitHomeworkInvalidClassFails(){
        testRegisterForClassSucceeds();

        this.student.submitHomework("Bob", "hw1", "answer1", "FakeClass", 2017);
        assertFalse(this.admin.classExists("FakeClass", 2017));
        assertFalse(this.instructor.homeworkExists("FakeClass", 2017, "hw1"));
        assertFalse(this.student.isRegisteredFor("Bob", "FakeClass", 2017));
        assertFalse(this.student.hasSubmitted("Bob", "hw1", "FakeClass", 2017));
    }

    @Test
    public void testSubmitHomeworkInvalidYearFails(){
        testRegisterForClassSucceeds();

        this.student.submitHomework("Bob", "hw1", "answer1", "Test", 2018);
        assertFalse(this.admin.classExists("Test", 2018));
        assertFalse(this.instructor.homeworkExists("Test", 2018, "hw1"));
        assertFalse(this.student.isRegisteredFor("Bob", "Test", 2018));
        assertFalse(this.student.hasSubmitted("Bob", "hw1", "Test", 2018));
    }

    @Test
    public void testSubmitHomeworkInvalidHWFails(){
        testRegisterForClassSucceeds();

        this.student.submitHomework("Bob", "DNEhw", "answer", "Test", 2017);
        assertTrue(this.admin.classExists("Test", 2017));
        assertFalse(this.instructor.homeworkExists("Test", 2017, "DNEhw"));
        assertTrue(this.student.isRegisteredFor("Bob", "Test", 2017));
        assertFalse(this.student.hasSubmitted("Bob", "DNEhw", "Test", 2017));
    }

}

