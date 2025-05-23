package com.kyle.kyleaiagent.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Haoran Wang
 * @date 2025/5/13 15:37
 */

public class test {
    public static void main(String[] args) {
//        int[][] intervals = {{1,3},{2,6},{8,10},{15,18}};
//        int m = intervals.length;
//        int n = intervals[0].length;
//        Arrays.sort(intervals, (i1, i2)-> i1[0] - i2[0]);
//        List<int[]> list = new ArrayList<>();
//        for(int i=0;i<m;i++){
//            int L = intervals[i][0];
//            int R = intervals[i][1];
//            if(list.size()==0 || list.get(list.size()-1)[1] < L){
//                list.add(new int[]{L,R});
//            }else{
//                list.get(list.size()-1)[1] = Math.max(list.get(list.size()-1)[1], R);
//            }
//        }
//        int[][] array = list.toArray(new int[list.size()][]);
//        for (int i = 0; i < array.length; i++) {
//            for (int ints : array[i]) {
//                System.out.println(ints);
//            }
//        }
        String s = "ababcbacadefegdehijhklij";
        int len = s.length();
        int[] startPosition = new int[26];
        int[] endPosition = new int[26];
        for (int i = 0; i < len; i++) {
            int i1 = startPosition[s.charAt(i) - 'a'];
            if (i1 != 0) {
                continue;
            }
            startPosition[s.charAt(i) - 'a'] = i + 1;
        }
        for (int i = 0; i < len; i++) {
            // 统计最后出现的位置
            endPosition[s.charAt(i) - 'a'] = i + 1;
        }
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            int L = startPosition[s.charAt(i) - 'a'];
            int R = endPosition[s.charAt(i) - 'a'];
            if (list.isEmpty() || list.getLast()[1] < L) {
                list.add(new int[]{L, R});
            } else {
                list.get(list.size() - 1)[1] = Math.max(list.get(list.size() - 1)[1], R);
            }
        }
        List<Integer> resList = new ArrayList<>();
        for (int[] arr : list) {
            resList.add(arr[1]-arr[0] + 1);
        }
        System.out.println(resList);
    }
}
