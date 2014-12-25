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

    public boolean compare(int value) {
        return this.value == value;
    }

    public static IntentTag fromValue(int value) {
        IntentTag[] tagArray = IntentTag.values();

        for (int i = 0; i < tagArray.length; i++) {
            if (tagArray[i].compare(value))
                return tagArray[i];
        }

        return null;
    }

}
