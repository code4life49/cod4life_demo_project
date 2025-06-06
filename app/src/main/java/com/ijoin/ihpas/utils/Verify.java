package com.ijoin.ihpas.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Verify {

    private final static int B1 = 13;
    private final static int B2 = 61;
    private final static int WORDS_SIZE = 8;
    private final static int SIZE = 4;

    private static Verify verify;

    private Verify() {
    }


    public static Verify getInstance() {

        if (verify == null) {
            synchronized (Verify.class) {
                if (verify == null) {
                    verify = new Verify();
                }
            }
        }
        return verify;
    }


    /**
     * 验证回调
     *
     * @param words
     * @return
     */
    public boolean verifySuccess(String words) {

        return verify.verify(words);
    }

    /**
     * 验证码验证
     * 2020年07月01日停止使用 01126060
     * * @param words
     *
     * @return
     */
    private boolean verify(String words) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse("2020-07-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();
        if (words.length() == WORDS_SIZE && !words.startsWith("00")) {


            if ((System.currentTimeMillis() > time) && "01126060".equals(words)) {
                return false;
            }
            int[] arrays = new int[SIZE];
            for (int i = 0; i < SIZE; i++) {
                arrays[i] = Integer.valueOf(words.substring(2 * i, 2 * i + 2));
            }

            int x1 = arrays[0] ^ arrays[1] ^ arrays[2] ^ arrays[3];
            int x2 = arrays[0] ^ arrays[2];

            if (x1 == B1 && x2 == B2) {
                return true;
            }
        }

        return false;
    }

}
