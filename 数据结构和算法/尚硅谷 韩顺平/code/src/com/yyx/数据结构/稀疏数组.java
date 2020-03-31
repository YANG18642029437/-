package com.yyx.数据结构;

import java.io.*;
import java.util.Arrays;

public class 稀疏数组 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int[][] arr = new int[11][11];
        arr[1][2] = 1;
        arr[2][3] = 2;
        int num = 0;
        for (int[] ints : arr) {
            for (int anInt : ints) {
                if (anInt != 0){
                    num ++;
                }
            }
            System.out.println();
        }
        int[][] result = new int[num+1][3];
        result[0][0] = 11;
        result[0][1] = 11;
        result[0][2] = num;
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] != 0){
                    index++;
                    result[index][0] = i;
                    result[index][1] = j;
                    result[index][2] = arr[i][j];
                }
            }
        }

        for (int[] ints : result) {
            System.out.println(Arrays.toString(ints));
        }

        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream("1.data"));

        obj.writeObject(result);


        ObjectInputStream stream = new ObjectInputStream(new FileInputStream("1.data"));
        Object read = stream.readObject();
        result = (int[][])read;

        int[][] arr2 = new int[result[0][0]][result[0][1]];
        for (int i = 1; i < result.length; i++) {
            arr2[result[i][0]][result[i][1]] = result[i][2];
        }
        for (int i = 0; i < arr2.length; i++) {
            for (int j = 0; j < arr2[i].length; j++) {
                System.out.print(arr2[i][j]+"\t");
            }
            System.out.println();
        }
    }
}
