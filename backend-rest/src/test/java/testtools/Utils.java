/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testtools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
/**
 *
 * @author marcin
 */
public class Utils {

    public static <T> void assertAllDifferent(Collection<T> collection) {
        ArrayList<T> array = new ArrayList<T>(collection);
        int expected_size = array.size();
        for (int i = 0; i < expected_size; ++i) {
            for (int j = 0; j < expected_size; ++j) {
                if (i != j) {
                    T sa = array.get(i);
                    T sb = array.get(j);
                    assertNotSame(sb, sa);
                }
            }
        }
    }

    public static boolean containsIgnoreCase(List<String> list, String item) {
        for (String listitem : list) {
            if (listitem.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }
    
    public static void assertAllContained(List<String> set_elements, String ...items) {
        for (String item : items) {
            assertTrue(containsIgnoreCase(set_elements, item));
        }
    }
    
}
