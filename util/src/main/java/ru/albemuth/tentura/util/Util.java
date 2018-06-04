package ru.albemuth.tentura.util;

import jcuda.CudaException;
import jcuda.LibUtils;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class Util {

    public static final int SUCCESS = 0;

    private static boolean initialized = false;
    private static boolean exceptionsEnabled = false;

    static {
        initialize();
    }

    private Util() {
    }

    public static void setExceptionsEnabled(boolean enabled) {
        exceptionsEnabled = enabled;
    }

    protected static int checkResult(int result) {
        if (exceptionsEnabled && result != SUCCESS) {
            throw new CudaException(stringFor(result));
        }
        return result;
    }

    private static void initialize() {
        assertInit();
    }


    public static void assertInit() {
        if (!initialized) {
            String libraryBaseName = "tentura-util-" + Util.getVersion();
            String libraryName = LibUtils.createPlatformLibraryName(libraryBaseName);
            LibUtils.loadLibrary(libraryName);
            initialized = true;
        }
    }

    public static String getVersion() {
        return "0.0.1";
    }

    public static String stringFor(int result) {
        switch (result) {
            case SUCCESS: return "SORTING_SUCCESS";
        }
        return "INVALID Result: " + result;
    }
}
