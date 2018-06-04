package ru.albemuth.tentura.util;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public enum Datatype {

    BOOLEAN(0), BYTE(1), SHORT(2), INT(3), LONG(4), FLOAT(5), DOUBLE(6);

    int type;

    Datatype(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
