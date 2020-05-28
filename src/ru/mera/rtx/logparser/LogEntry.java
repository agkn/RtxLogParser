package ru.mera.rtx.logparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
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
