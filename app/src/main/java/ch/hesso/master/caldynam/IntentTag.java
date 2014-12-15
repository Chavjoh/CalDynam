package ch.hesso.master.caldynam;

public enum IntentTag {

    TAKE_PICTURE(401);

    private final int value;

    private IntentTag(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }

}
