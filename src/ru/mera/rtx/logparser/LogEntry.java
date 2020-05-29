package ru.mera.rtx.logparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    // 05-15 15:45:53.721 I/Telecom (  406): CallsManager:
    private final static Pattern ENTRY_PATTERN = Pattern.compile("^"
            + "(\\d?\\d)-(\\d\\d)" // Moth - Date: 08-17
            + "\\s"
            + "(\\d\\d):(\\d\\d):(\\d\\d).(\\d{3})" // 12:43:02.835
            + "\\s"
            + "(.)"  + "/" // Verbosity: I/
            + "(.+?)" // Module name: Unite Service
            + "\\(([ \\d]+)\\)+" // Pid
            + ":\\s"
            + "(.*)" // Msg
            + "$"
    );

    // 05-28 12:49:58.971   777   812 I System.out: Raw recv: (10)10 00 06 2b 00 01 81 42 00 ef
    private final static Pattern ENTRY_PATTERN_NEW = Pattern.compile("^"
            + "(\\d?\\d)-(\\d\\d)" // Moth - Date: 05-28
            + "\\s"
            + "(\\d\\d):(\\d\\d):(\\d\\d).(\\d{3})" // 12:49:58.971
            + "\\s+"
            + "(\\d+)" // 777
            + "\\s+"
            + "(\\d+)" // 812
            + "\\s+"
            + "(\\S)" // I
            + "\\s+"
            + "(\\S+)" // System.out
            + "\\s*:\\s+"
            + "(.+)"  // Msg
            + "$"
    );


    private int mMonth;
    private int mDayOfMonth;
    private int mHour;
    private int mMin;
    private int mSec;
    private int mMsec;
    private String mVerbosity;
    private String mModule;
    private int mPid;
    private String mMsg;

    public boolean setup(String aString) {
        // parseOld(aString);
        return parseNew(aString);
    }

    private boolean parseNew(String aString) {
        Matcher matcher = ENTRY_PATTERN_NEW.matcher(aString);
        if (!matcher.matches()) {
            return false;
        }
        mMonth = Integer.valueOf(matcher.group(1));
        mDayOfMonth = Integer.valueOf(matcher.group(2));
        mHour = Integer.valueOf(matcher.group(3));
        mMin = Integer.valueOf(matcher.group(4));
        mSec = Integer.valueOf(matcher.group(5));
        mMsec = Integer.valueOf(matcher.group(6));

        mPid = Integer.valueOf(matcher.group(7).trim());
        mVerbosity = matcher.group(9);
        mModule = matcher.group(10);
        mMsg = matcher.group(11);

        return true;
    }

    private boolean parseOld(String aString) {
        Matcher matcher = ENTRY_PATTERN.matcher(aString);
        if (!matcher.matches()) {
            return false;
        }
        mMonth = Integer.valueOf(matcher.group(1));
        mDayOfMonth = Integer.valueOf(matcher.group(2));
        mHour = Integer.valueOf(matcher.group(3));
        mMin = Integer.valueOf(matcher.group(4));
        mSec = Integer.valueOf(matcher.group(5));
        mMsec = Integer.valueOf(matcher.group(6));
        mVerbosity = matcher.group(7);
        mModule = matcher.group(8);
        mPid = Integer.valueOf(matcher.group(9).trim());
        mMsg = matcher.group(10);

        return true;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDayOfMonth() {
        return mDayOfMonth;
    }

    public int getHour() {
        return mHour;
    }

    public int getMin() {
        return mMin;
    }

    public int getSec() {
        return mSec;
    }

    public int getMsec() {
        return mMsec;
    }

    public String getVerbosity() {
        return mVerbosity;
    }

    public String getModule() {
        return mModule;
    }

    public int getPid() {
        return mPid;
    }

    public LogEntry setMsg(String aMsg) {
        mMsg = aMsg;
        return this;
    }

    public String getMsg() {
        return mMsg;
    }

    @Override
    public String toString() {
        return String.format("%02d-%02d %02d:%02d:%02d.%03d %s/%s(%5s): %s",
                mMonth, mDayOfMonth,
                mHour, mMin, mSec, mMsec,
                mVerbosity,
                mModule,
                String.valueOf(mPid),
                mMsg);
    }
}
