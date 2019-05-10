package com.project.spender;

import java.util.ArrayList;
import java.util.List;

public class ScanResult {

    private final String fn;
    private final String fd;
    private final String fp;
    private final String date;
    private final String sum;

    public List<String> parseNumbers(String content) {
        List<String> res = new ArrayList<>();
        System.out.println(content);
        for (String i : content.split("[&|=|a-z]")) {
            if (i.length() != 0) {
                if (i.contains(".")) {
                    res.add(i.replace(".", ""));
                } else {
                    res.add(i);
                }
            }
        }
        return res;
    }

    public String getDate() {
        return date;
    }

    public String getFd() {
        return fd;
    }

    public String getFn() {
        return fn;
    }

    public String getFp() {
        return fp;
    }

    public String getSum() {
        return sum;
    }

    public ScanResult(String result) {
        List<String> resultNumbers = parseNumbers(result);
        fn = resultNumbers.get(2);
        fd = resultNumbers.get(3);
        fp = resultNumbers.get(4);
        date = resultNumbers.get(0);
        sum = resultNumbers.get(1);
    }
}
