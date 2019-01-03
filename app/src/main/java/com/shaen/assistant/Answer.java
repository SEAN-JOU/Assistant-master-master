package com.shaen.assistant;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by NB004 on 2018/6/1.
 */

public class Answer {


    int randomInt = new Random().nextInt(2)+1;


    public Answer() {

    }

    public static Answer getIntence(){
        return  new Answer();
    }

    public  String sentence(String s, HashMap<String,String> hashMap) {


        for (Map.Entry<String,String> entry : hashMap.entrySet()) {
            if (s.contains(String.valueOf(entry.getKey()))) {

                return (String) entry.getValue();


//                if (randomInt == 1)
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//                        return (String) entry.getValue().get(0);
//                    } else {
//                        return (String) entry.getValue().get(0);
//                    }
//                else {
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//                        return (String) entry.getValue().get(1);
//                    } else {
//                        return (String) entry.getValue().get(1);
//                    }}
}}
            return aaa(randomInt);
        //        else if (s.contains("哈囉".trim())) {
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//                return "哈囉";
//            } else {
//                return "哈囉";
//            }
//        }

//        else if (s.contains("好".trim())) {
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//                return "不好";
//            } else {
//                return "不好";
//            }
//        }
//        else if (s.length() == 1) {
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//                return s+"屁唷";
//            } else {
//                return s+"屁唷";
//            }
//        }
    }

    public static String aaa(int sss) {
    if (sss == 1)
        return "你名義老爸在你背後他很火";
    else {
        return "你在共蝦米";
    }}






}
