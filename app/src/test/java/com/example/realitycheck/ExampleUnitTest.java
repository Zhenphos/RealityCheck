package com.example.realitycheck;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //Todo: we need to format some testing so that we can figure out exactly how we want things to run.
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        System.out.println("passed");
    }

    /*@Test
    public void userCheck(){
        User user = new User("matt","matt",new ArrayList<Post>(),new ArrayList<User>() , new ArrayList<User>(),new ArrayList<User>());
    }*/
}