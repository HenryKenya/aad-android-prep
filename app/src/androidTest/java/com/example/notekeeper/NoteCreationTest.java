package com.example.notekeeper;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    static DataManager dataManager;

    @BeforeClass
    public static void classSetUp() {
        dataManager = DataManager.getInstance();
    }

    // creating NoteListActivity for testing
    @Rule
    public ActivityTestRule<NoteListActivity> noteListActivityActivityRule
            = new ActivityTestRule(NoteListActivity.class);

    @Test
    public void createNewNote() {
        final CourseInfo course = dataManager.getCourse("java_lang");
        final String noteTitle = "Test note title";
        final String noteText = "This is the body of our test note";

        onView(withId(R.id.fab)).perform(click()); // launches NoteActivity

        onView(withId(R.id.spinner_courses)).perform(click()); // click on spinner to present options
        onData(allOf(instanceOf(CourseInfo.class), equalTo(course))).perform(click()); // make selection on spinner
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(
                containsString(course.getTitle())
        ))); // confirms that right course title is created
        onView(withId(R.id.edit_note_title)).perform(typeText(noteTitle));
        onView(withId(R.id.edit_note_text)).perform(typeText(noteText), closeSoftKeyboard());

        pressBack(); // leave note activity at the end of the test

        int lastNoteIndex = dataManager.getNotes().size() - 1;
        NoteInfo lastNote = dataManager.getNotes().get(lastNoteIndex);

        assertEquals(course, lastNote.getCourse());
        assertEquals(noteTitle, lastNote.getTitle());
        assertEquals(noteText, lastNote.getText());
    }

}