package ro.pub.cs.systems.eim.practicaltest02;

public class Key {
    String keyName;
    long timeStamp;

    public Key(String keyName, long timeStamp) {
        this.keyName = keyName;
        this.timeStamp = timeStamp;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }
}

