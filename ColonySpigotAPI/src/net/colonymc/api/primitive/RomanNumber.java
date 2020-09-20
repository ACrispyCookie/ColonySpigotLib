package net.colonymc.api.primitive;

import java.util.TreeMap;

public class RomanNumber {

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public final static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }
    
    public final static int romanToInt(String s) {
        if(s == null){
            return 0;
        }
        
        int length = s.length();
        int sum = 0;
        int pre = 0;
        
        for(int i = length - 1; i >= 0; i--){
            int cur = romanTable(s.charAt(i));
            
            if(i == length - 1){
                sum = sum + cur;
            }else{
               if(cur < pre){
                   sum = sum - cur;
               }else{
                   sum = sum + cur;
               }
            }
            pre = cur;
        }
        
        return sum;
    }

    public static int romanTable(char c){
        int num = 0;
        switch(c){
            case 'I':
                num = 1;
                break;
            case 'V':
                num = 5;
                break;
             case 'X':
                num = 10;
                break;
             case 'L':
                 num = 50;
                 break;
             case 'C':
                 num = 100;
                 break;
             case 'D':
                 num = 500;
                 break;
             case 'M':
                 num = 1000;
                 break;
             default:
                 num = 0;
                 break;
        }
        return num;
    }

}
