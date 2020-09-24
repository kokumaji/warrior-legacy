package com.kokumaji.Warrior.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LoreFormatter {

    ArrayList<String> formattedLore;

    public LoreFormatter(String... lore) {
        this.formattedLore = format(lore);
    }

    public ArrayList<String> GetFormatted() {
        return formattedLore;
    }

    public static List<String> splitString(String msg, int lineSize) {
        List<String> res = new ArrayList<String>();

        Pattern p = Pattern.compile("\\b.{1," + (lineSize - 1) + "}\\b\\W?");
        Matcher m = p.matcher(msg);

        while (m.find()) {
            res.add(m.group());
        }

        return res;
    }

    public static ArrayList<String> format(String[] input) {
        ArrayList<String> out = new ArrayList<String>();
        int longest = 0;

        for(String s : input) {
            String[] line = s.split(" ");
            if(line[0].endsWith(":")) {
                if(line[0].length() > longest) longest = line[0].length();
            }
        }

        input = checkLength(input);
        
        for(String s : input) {
            int end = 0;
            if(s.indexOf(" ") < 0) {
                end = s.length();
            } else {
                end = s.indexOf(" ");
            }
            String prefix = s.substring(0, end);
            String rest = "none";
            if(prefix.length() != s.length()) {
                rest = s.substring(prefix.length() + 1, s.length());
            }

            if(prefix.endsWith(":")) {
                int dif = longest - prefix.length();
                if(dif == 0) dif = 1;

                String spaces = "";
                for(int i = 0; i < (dif +1); i++) {
                    spaces += " ";
                }
                prefix = spaces + prefix.replace("_", " ") + " ";

                out.add(prefix + rest);
            } else {

                String spaces = "";
                for(int i = 0; i < (longest +1); i++) {
                    spaces += " ";
                }
                s = spaces + s;
                out.add(s);
            }

        }
        
        return out;
    }

    private static String[] checkLength(String[] input) {
        String[] out = input;
        
        int i = 0;
        for(String s : input) {
            int a = 35;
            if(s.length() > a) {
                List<String> split = splitString(s, a);
                ArrayList<String> a1 = new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(input, 0, i)));
                a1.addAll(split);

                String[] a2 = Arrays.copyOfRange(input, i + 1, input.length);
                a1.addAll(Arrays.asList(a2));

                out = a1.toArray(new String[a1.size()]);
            }
            i++;
        }

        return out;
    }
}
