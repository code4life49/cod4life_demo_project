package com.ijoin.ihpas;

import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1(){
        String str = "       ";
        String str1="";
        str = str.toUpperCase();
        int count=0;
        String[] arr = str.split("");
        for(int i=0;i<arr.length;i++){
            if(arr[i].equals(str1)){
                count++;
            }
        }
        System.out.println(count);

    }
}