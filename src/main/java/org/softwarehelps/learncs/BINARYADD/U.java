package org.softwarehelps.learncs.BINARYADD;

import java.util.*;

public class U {
     public static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     public static long atol (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Long.valueOf(s).longValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     public static String[] copy (String array[]) {
          String[] newone = new String[array.length];
          for (int i=0; i<array.length; i++)
               newone[i] = new String(array[i]);
          return newone;
     }

     public static String[] tokenize(String s) {
          StringTokenizer st = new StringTokenizer(s);
          String[] tokens = new String[st.countTokens()];
          int i=0;
          while (st.hasMoreTokens()) {
               String token = st.nextToken();
               tokens[i++] = token;
          }
          return tokens;
     }

     public static String[] tokenize(String s, String delim) {
          StringTokenizer st = new StringTokenizer(s, delim);
          String[] tokens = new String[st.countTokens()];
          int i=0;
          while (st.hasMoreTokens()) {
               String token = st.nextToken();
               tokens[i++] = token;
          }
          return tokens;
     }

     public static String detokenize(String[] tokens) {
          String s = "";
          for (int i=0; i<tokens.length; i++)
               if (tokens[i] != null)
                    s += tokens[i] + " ";
          return s;
     }

     public static boolean equals (String[] list1, String[] list2) {
          if (list1.length != list2.length)
               return false;
          for (int i=0; i<list1.length; i++)
               if (list1[i] != null && list2[i] != null)
                    if (list1[i].equals(list2[i]))
                         return false;
          return true;
     }

     public static void sleep (long milliseconds) {
          try {
               Thread.sleep(milliseconds);
          } catch (InterruptedException ie) {}
     }

     public static int power(int n, int power) {
          if (power < 0)
               return 0;
          int result = 1;
          for (int i=0; i<power; i++)
               result *= n;
          return result;
     }

     public static boolean isBinary(String s) {
          for (int i=0; i<s.length(); i++) {
               char ch = s.charAt(i);
               if (ch != '0' && ch != '1')
                    return false;
          }
          return true;
     }

     // This one goes from base 10 to the new base in "base"

     public static String convert (int n, int base) {
          String s = "";
          while (n > 0) {
               int rem = n % base;
               n = n / base;
               s = Character.forDigit(rem, base) + s;
          }
          return s.toUpperCase();
     }

     // This one goes to base 10, given that "s" in in "base".

     public static int deconvert (String s, int base) {
          int power = 1;
          int result = 0;

          while (s.length() > 0) {
               char ch = s.charAt(s.length()-1);
               result += Character.digit(ch, base) * power;
               s = s.substring(0,s.length()-1);
               power *= base;
          }

          return result;
     }

}
