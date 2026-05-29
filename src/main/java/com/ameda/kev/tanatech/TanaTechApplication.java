package com.ameda.kev.tanatech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.*;

@SpringBootApplication
public class TanaTechApplication {

/**
 * Duplicate Finder
 *  Write a Java Application that accepts a list/array of integers
 *   Identifies and prints duplicate values
* */

    public static void main(String[] args) {
        SpringApplication.run(TanaTechApplication.class, args);
        int[] input = {1, 2, 3, 4, 2, 5, 1};
        DuplicateFinder.findDuplicates(input);
    }

    /**
     * Hashset and LinkedHashset are data structures that stores unique  values only.
     * The two data structures behave differently on how they order the elements. Internally uses a hash table.
     * Hashset data structure does not guarantee order as elements they may come in different order than the one added with.
     *  Hashset focuses on speed and not ordering.
     *  LinkedHashset, elements come out in the exact order you added them. Internal uses Linked List and hashtable
     *
    * */

    public class DuplicateFinder {
        public static void findDuplicates(int[] arr) {
            Set<Integer> seen = new HashSet<>();
            Set<Integer> duplicates = new LinkedHashSet<>();
            // LinkedHashSet keeps insertion order for nicer output
            for (int num : arr) {
                if (seen.contains(num)) {
                    duplicates.add(num);
                } else {
                    seen.add(num);
                }
            }

            // Print result
            System.out.println(formatOutput(duplicates));
        }


        /**
         *  print the result and format as expected.
         *  Use of StringBuilder for memory consumption and instead of having many + concatenation marks.
         *  Then at the end what am doing am trying to remove the trailing ,
        * */
        private static String formatOutput(Set<Integer> duplicates) {
            StringBuilder sb = new StringBuilder();
            for (Integer num : duplicates) {
                sb.append(num).append(",");
            }
            // remove last comma if needed
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }

            return sb.toString();
        }
    }
}
