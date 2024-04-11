package com.kandaovr.meeting.kotlinDemo;

import kotlin.text.Charsets;

public class Maintest {
    public static void main(String[] args) {
        String str="45d4a4sdadasdsfgfg11231213132121131213";
        byte[] byteString = str.getBytes(Charsets.UTF_8);


        for (byte a:byteString){
            System.out.println(new String(new byte[]{a}));
        }
    }
}
