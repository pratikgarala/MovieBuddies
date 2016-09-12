package com.example.thatsmepratik.moviebuddies;

import android.app.Application;
import android.test.ApplicationTestCase;
import org.testng.annotations.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
//        assertThat(EmailValidator.isValidEmail("name@email.com"), is(true));
    }


}