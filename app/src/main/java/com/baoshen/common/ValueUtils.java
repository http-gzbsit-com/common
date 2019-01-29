package com.baoshen.common;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Shute on 2018/8/30.
 */
public class ValueUtils {

    public static double max(@NotNull double[] array) {
        assert array != null && array.length > 0 : "数据必须有成员";
        double max = array[0];
        for (double item : array) {
            if (item > max) max = item;
        }
        return max;
    }
    public static int max(@NotNull List<Integer> list) {
        assert list != null && list.size() > 0 :"列表必须有成员";
        Integer max = list.get(0);
        for (Integer item : list) {
            if (item > max) max = item;
        }
        return max;
    }
    public static int max(int[] array) {
        assert array != null && array.length > 0:"数组必须有成员";
        int max = array[0];
        for (int item : array) {
            if (item > max) max = item;
        }
        return max;
    }

    public static void sort(List<Double> list)
    {
        Collections.sort(list,new Comparator<Double>() {
            public int compare(Double d1,Double d2)
            {
                return Double.compare(d1,d2);
            }
        });
    }

    public static  <T> List<T> toList(T... array)
    {
        List<T> list = new ArrayList();
        for (T item :array) list.add(item);
        return list;
    }

    public static List<Double> toList(double[] array)
    {
        List<Double> list = new ArrayList();
        for (double item :array) list.add(item);
        return list;
    }
    public static List<Byte> toList(byte[] array)
    {
        List<Byte> list = new ArrayList();
        for (byte item :array) list.add(item);
        return list;
    }
    public static <T> List<T> paged(List<T> list,int skip,int count) {
        assert skip >= 1 : "参数skip取值范围大于0";
        assert list != null && list.size() >= skip + count :"skip过长";
        List<T> newList = new ArrayList();
        int max = skip + count;
        for (int i = skip; i < max; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }
    public static <T> T[] toArray(List<T> list,Class<T> type) {
        T[] array = (T[]) Array.newInstance(type, list.size());
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(0);
        }
        return array;
    }

    public static double sum(Double[] array)
    {
        double total = 0;
        for (Double item:array) {
            total+=item;
        }
        return total;
    }
    public static int sum(int[] array)
    {
        int total = 0;
        for (int item:array) {
            total+=item;
        }
        return total;
    }

    public static byte[] ChangeDigitRate(byte[] src,int dstDigit,int srcDigit)
    {
        assert (srcDigit > 0 && srcDigit < 9) :"srcDigit取值范围不正确";
        assert (dstDigit > 0 && dstDigit < 9) :"dstDigit取值范围不正确";
        int[] ints = new int[(int)Math.ceil(((double)src.length * srcDigit) / dstDigit)];

        int srcIndex = 0;//读到src的第几个byte
        int srcRemainBits = srcDigit;//src剩余多少位没有被读
        int dstRemainBits = dstDigit;//dst剩余多少位没有被写
        int dstIndex = 0;
        while (srcIndex < src.length)
        {
            if (srcRemainBits > 0)
            {
                int dstItem = ints[dstIndex];//这里只用用int，java byte的设计有些奇怪

                int srcItem = src[srcIndex]& 0xFF;
                int readBits = srcRemainBits > dstRemainBits ? dstRemainBits : srcRemainBits;
                dstItem <<= readBits;
                dstItem |= (((srcItem >> srcRemainBits - readBits) << (32 - readBits)) >>> (32 - readBits));
                srcRemainBits -= readBits;
                dstRemainBits -= readBits;

                ints[dstIndex] = dstItem;
                if (dstRemainBits == 0)
                {
                    dstIndex++;
                    dstRemainBits = dstDigit;
                    continue;
                }
            }
            else
            {
                srcIndex++;
                srcRemainBits = srcDigit;
            }
        }
        byte[] dst = new byte[ints.length];
        for(int i=0;i<dst.length;i++) dst[i] = (byte)ints[i];

        int mol = src.length * srcDigit % dstDigit;
        //原数据最后一小节数据不够长度，为了使数据位对齐，要额外做位移
        if (mol != 0)
        {
            byte last = dst[dst.length - 1];
            last <<= (dstDigit - mol);
            dst[dst.length - 1] = last;
        }

        return dst;
    }

    public static int getLength(byte[][] planar,int dimension) {
        if(dimension==0) return planar.length;
        if(dimension ==1) return planar[0].length;
        throw new UnsupportedClassVersionError();
    }
    public static int getLength(boolean[][] planar,int dimension) {
        if(dimension==0) return planar.length;
        if(dimension ==1) return planar[0].length;
        throw new UnsupportedClassVersionError();
    }

    public static byte[][] createPlanar(int rows,int cols){
        byte[][] planar = new byte[rows][];
        for (int i=0;i<rows;i++){
            planar[i] = new byte[cols];
        }
        return planar;
    }
    public static boolean[][] createPlanarOfBoolean(int rows,int cols){
        boolean[][] planar = new boolean[rows][];
        for (int i=0;i<rows;i++){
            planar[i] = new boolean[cols];
        }
        return planar;
    }

    //目前不支持unsigned long
    public static long toLong(@NotNull byte[] bytes){
        assert bytes!=null && bytes.length<9 :"参数长度不正确";
        long value=0;
        for (int i = 0; i < bytes.length; i++)
        {
            value <<= 8;
            value |= (0x00ff & bytes[i]);
        }
        return value;
    }
}
