import api.IAdmin;
import api.core.impl.Admin;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pryu on 3/6/2017.
 */
public class TestAdmin {
    private IAdmin admin;

    @Before
    public void setup() {
        this.admin = new Admin();
    }

    @Test
    public void testMakeClassSucceeds() {
        this.admin.createClass("Test", 2017, "Instructor", 15);
        assertTrue(this.admin.classExists("Test", 2017));
    }

    @Test
    public void testMakeClassInPastFails() {
        this.admin.createClass("Test", 2016, "Instructor", 15);
        assertFalse(this.admin.classExists("Test", 2016));
    }

    @Test
    public void testMakeClassCapacityEqualZeroFails() {
        this.admin.createClass("Test", 2017, "Instructor", 0);
        assertFalse(this.admin.classExists("Test", 2017));
    }

    @Test
    public void testMakeClassCapacityLessThanZeroFails() {
        this.admin.createClass("Test", 2017, "Instructor", -1);
        assertFalse(this.admin.classExists("Test", 2017));
    }

    @Test
    public void testMakeClassDifferentInstructorFails(){
        this.admin.createClass("Test", 2017, "Instructor", 15);
        this.admin.createClass("Test", 2017, "Instructor2", 15);
        assertEquals(this.admin.getClassInstructor("Test", 2017), "Instructor");
    }

    @Test
    public void testInstructor2CoursesSucceeds() {
        this.admin.createClass("Test", 2017, "Instructor", 15);
        this.admin.createClass("Test1", 2017, "Instructor", 15);
        this.admin.createClass("Test", 2018, "Instructor", 15);
        assertTrue(this.admin.classExists("Test", 2017));
        assertTrue(this.admin.classExists("Test1", 2017));
        assertTrue(this.admin.classExists("Test", 2018));
    }

    @Test
    public void testInstructor3CoursesInYearFails() {
        this.admin.createClass("Test", 2017, "Instructor", 15);
        this.admin.createClass("Test1", 2017, "Instructor", 15);
        this.admin.createClass("Test2", 2017, "Instructor", 15);
        assertTrue(this.admin.classExists("Test", 2017));
        assertTrue(this.admin.classExists("Test1", 2017));
        assertFalse(this.admin.classExists("Test2", 2017));
    }

    @Test
    public void testChangeCapacitySucceeds() {
        this.admin.createClass("Test", 2017, "Instructor", 15);
        assertTrue(this.admin.classExists("Test", 2017));
        this.admin.changeCapacity("Test", 2017, 35);
        assertTrue(this.admin.getClassCapacity("Test",2017)>0);
    }

    @Test
    public void testChangeSameCapacitySucceeds() {
        this.admin.createClass("Test", 2017, "Instructor", 15);
        assertTrue(this.admin.classExists("Test", 2017));
        this.admin.changeCapacity("Test", 2017, 15);
        assertTrue(this.admin.getClassCapacity("Test",2017)>0);
    }

    @Test
    public void testChangeCapacityToZeroFails() {
        this.admin.createClass("Test", 2017, "Instructor", 15);
        assertTrue(this.admin.classExists("Test", 2017));
        this.admin.changeCapacity("Test", 2017, 0);
        assertFalse(this.admin.getClassCapacity("Test",2017)>0);
    }

    @Test
    public void testChangeCapacityLessThanZeroFails() {
        this.admin.createClass("Test", 2017, "Instructor", 15);
        assertTrue(this.admin.classExists("Test", 2017));
        this.admin.changeCapacity("Test", 2017, -1);
        assertFalse(this.admin.getClassCapacity("Test",2017)>0);
    }

}
