package ru.albemuth.tentura.util;

import jcuda.Pointer;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.JCudaDriver;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public class TestSorting {

    public static int N = 40000000;
    public static int SEGMENTS = 5000;
    public static int ROWS = 5000;
    public static int COLS = 8000;

    @BeforeClass
    public static void init() {
        JCudaDriver.setExceptionsEnabled(true);
        JCudaDriver.cuInit(0);
        CUdevice device = new CUdevice();
        JCudaDriver.cuDeviceGet(device, 0);
        CUcontext context = new CUcontext();
        JCudaDriver.cuCtxCreate(context, 0, device);
    }

    private boolean[] booleanArray(int length) {
        boolean[] array = new boolean[length];
        for (int i = 0; i < length; i++) {
            array[i] = Math.random() < 0.5;
        }
        return array;
    }

    private boolean[] booleanArray(byte[] array) {
        boolean[] booleanArray = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            booleanArray[i] = array[i] != 0;
        }
        return booleanArray;
    }

    private byte[] byteArray(int length) {
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = (byte) (Math.random() * Byte.MAX_VALUE);
        }
        return array;
    }

    private byte[] byteArray(boolean[] array) {
        byte[] byteArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            byteArray[i] = array[i] ? (byte)1 : 0;
        }
        return byteArray;
    }

    private short[] shortArray(int length) {
        short[] array = new short[length];
        for (int i = 0; i < length; i++) {
            array[i] = (short) (Math.random() * Short.MAX_VALUE);
        }
        return array;
    }

    private int[] intArray(int length) {
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * Integer.MAX_VALUE);
        }
        return array;
    }

    private long[] longArray(int length) {
        long[] array = new long[length];
        for (int i = 0; i < length; i++) {
            array[i] = (long) (Math.random() * Long.MAX_VALUE);
        }
        return array;
    }

    private float[] floatArray(int length) {
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = (float) (Math.random() * Float.MAX_VALUE);
        }
        return array;
    }

    private double[] doubleArray(int length) {
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = Math.random() * Double.MAX_VALUE;
        }
        return array;
    }

    private int sizeOfItem(Object array) {
        Class itemClass = array.getClass().getComponentType();
        int sizeOf = 0;
        if (boolean.class.equals(itemClass)) {
            sizeOf = 1;
        } else if (byte.class.equals(itemClass)) {
            sizeOf = 1;
        } else if (short.class.equals(itemClass)) {
            sizeOf = 2;
        } else if (int.class.equals(itemClass)) {
            sizeOf = 4;
        } else if (long.class.equals(itemClass)) {
            sizeOf = 8;
        } else if (float.class.equals(itemClass)) {
            sizeOf = 4;
        } else if (double.class.equals(itemClass)) {
            sizeOf = 8;
        }
        return sizeOf;
    }

    private Pointer pointer(boolean[] array) {
        return Pointer.to(byteArray(array));
    }

    private Pointer pointer(byte[] array) {
        return Pointer.to(array);
    }

    private Pointer pointer(short[] array) {
        return Pointer.to(array);
    }

    private Pointer pointer(int[] array) {
        return Pointer.to(array);
    }

    private Pointer pointer(long[] array) {
        return Pointer.to(array);
    }

    private Pointer pointer(float[] array) {
        return Pointer.to(array);
    }

    private Pointer pointer(double[] array) {
        return Pointer.to(array);
    }

    private CUdeviceptr devicePtr(long size) {
        CUdeviceptr ptr = new CUdeviceptr();
        JCudaDriver.cuMemAlloc(ptr, size);
        return ptr;
    }

    private void copy2device(CUdeviceptr devicePtr, boolean[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    private void copy2device(CUdeviceptr devicePtr, byte[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    private void copy2device(CUdeviceptr devicePtr, short[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    private void copy2device(CUdeviceptr devicePtr, int[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    private void copy2device(CUdeviceptr devicePtr, long[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    private void copy2device(CUdeviceptr devicePtr, float[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    private void copy2device(CUdeviceptr devicePtr, double[] data) {
        Pointer hostPtr = pointer(data);
        JCudaDriver.cuMemcpyHtoD(devicePtr, hostPtr, data.length * sizeOfItem(data));
    }

    private void copy2host(boolean[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
        byte[] bytes = hostPtr.getByteBuffer(0, result.length).array();
        for (int i = 0; i < result.length; i++) {
            result[i] = bytes[i] > 0;
        }
    }

    private void copy2host(byte[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    private void copy2host(short[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    private void copy2host(int[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    private void copy2host(long[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    private void copy2host(float[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    private void copy2host(double[] result, CUdeviceptr devicePtr) {
        Pointer hostPtr = pointer(result);
        JCudaDriver.cuMemcpyDtoH(hostPtr, devicePtr, result.length * sizeOfItem(result));
    }

    @Test
    public void testSortBooleanKeysWithContext() throws Exception {
        boolean[] keys = booleanArray(N);
//        boolean[] keys = booleanArray(10);
        System.out.println("keys created: " + keys.length);
//        for (boolean b: keys) {
//            System.out.print(b + " ");
//        }
//        System.out.println();
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.BOOLEAN, keys.length, 1);
        System.out.println("Keys copied to device");
        Sorting.sort(keysPtr, Datatype.BOOLEAN, keys.length, context);
        System.out.println("keys sort complete");
        boolean[] sortedKeys = new boolean[keys.length];
        copy2host(sortedKeys, keysPtr);
        System.out.println("keys copied to host");
//        for (boolean b: sortedKeys) {
//            System.out.print(b + " ");
//        }
//        System.out.println();
        byte[] bytes = byteArray(keys);
        Arrays.sort(bytes);
        boolean[] hostSortedKeys = booleanArray(bytes);
        System.out.println("host keys sort complete");
        assertArrayEquals(byteArray(sortedKeys), byteArray(hostSortedKeys));
    }

    @Test
    public void testSortByteKeysWithContext() throws Exception {
//        byte[] keys = byteArray(N);
        byte[] keys = byteArray(5);
        for (byte b: keys) {
            System.out.print(b + " ");
        }
        System.out.println();
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.BYTE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.BYTE, keys.length, context);
        byte[] sortedKeys = new byte[keys.length];
        copy2host(sortedKeys, keysPtr);
        for (byte b: sortedKeys) {
            System.out.print(b + " ");
        }
        System.out.println();
        byte[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
    }

    @Test
    public void testSortShortKeysWithContext() throws Exception {
//        byte[] keys = byteArray(N);
        short[] keys = shortArray(10);
        for (short b: keys) {
            System.out.print(b + " ");
        }
        System.out.println();
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.SHORT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.SHORT, keys.length, context);
        short[] sortedKeys = new short[keys.length];
        copy2host(sortedKeys, keysPtr);
        for (short b: sortedKeys) {
            System.out.print(b + " ");
        }
        System.out.println();
        short[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
    }

    @Test
    public void testSortIntKeysWithContext() throws Exception {
        int[] keys = intArray(N);
//        int[] keys = intArray(10);
//        for (int k: keys) {
//            System.out.print(k + " ");
//        }
//        System.out.println();
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.INT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.INT, keys.length, context);
        int[] sortedKeys = new int[keys.length];
        copy2host(sortedKeys, keysPtr);
//        for (int k: sortedKeys) {
//            System.out.print(k + " ");
//        }
//        System.out.println();
        int[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
    }

    @Test
    public void testSortLongKeysWithContext() throws Exception {
//        long[] keys = longArray(N);
        long[] keys = longArray(10);
//        for (int k: keys) {
//            System.out.print(k + " ");
//        }
//        System.out.println();
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.LONG, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.LONG, keys.length, context);
        long[] sortedKeys = new long[keys.length];
        copy2host(sortedKeys, keysPtr);
//        for (long k: sortedKeys) {
//            System.out.print(k + " ");
//        }
//        System.out.println();
        long[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys);
    }

    @Test
    public void testSortFloatKeysWithContext() throws Exception {
//        float[] keys = floatArray(N);
        float[] keys = floatArray(10);
//        for (float k: keys) {
//            System.out.print(k + " ");
//        }
//        System.out.println();
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.FLOAT, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.FLOAT, keys.length, context);
        float[] sortedKeys = new float[keys.length];
        copy2host(sortedKeys, keysPtr);
//        for (float k: sortedKeys) {
//            System.out.print(k + " ");
//        }
//        System.out.println();
        float[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
    }

    @Test
    public void testSortDoubleKeysWithContext() throws Exception {
//        double[] keys = doubleArray(N);
        double[] keys = doubleArray(10);
        for (double k: keys) {
            System.out.print(k + " ");
        }
        System.out.println();
        long size = keys.length * sizeOfItem(keys);
        CUdeviceptr keysPtr = devicePtr(size);
        copy2device(keysPtr, keys);
        KeySortContext context = Sorting.keySortContext(Datatype.DOUBLE, keys.length, 1);
        Sorting.sort(keysPtr, Datatype.DOUBLE, keys.length, context);
        double[] sortedKeys = new double[keys.length];
        copy2host(sortedKeys, keysPtr);
        for (double k: sortedKeys) {
            System.out.print(k + " ");
        }
        System.out.println();
        double[] hostSortedKeys = keys.clone();
        Arrays.sort(hostSortedKeys);
        assertArrayEquals(sortedKeys, hostSortedKeys, 0);
    }
}
