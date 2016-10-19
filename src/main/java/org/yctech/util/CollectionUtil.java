package org.yctech.util;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 17:24
 */
public class CollectionUtil {
    /**
     * Generate a collection from enumeration and return it.
     */
    public static Collection toCollection(Enumeration _e) {
        List result = new ArrayList();
        if (_e != null) {
            while (_e.hasMoreElements()) {
                Object object = _e.nextElement();
                result.add(object);
            }
        }
        return result;
    }


    public static List getList(Enumeration _e) {
        return new ArrayList(toCollection(_e));
    }


    public static Set getSet(Enumeration _e) {
        return new HashSet(toCollection(_e));
    }

    /**
     * get intersection of two collection
     *
     * @param c1
     * @param c2
     * @return if no intersection return collection size is 0
     */
    public static Collection getInterSection(Collection c1, Collection c2) {
        if (c1 == null || c2 == null) {
            return null;
        }
        Collection newCollection = new ArrayList();
        Iterator it = c1.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (c2.contains(obj)) {
                newCollection.add(obj);
            }
        }
        return newCollection;
    }


    /*
    public static List getList(Enumeration _e) {
        List result = new ArrayList();
        if(_e != null) {
            while(_e.hasMoreElements()) {
                Object object = _e.nextElement();
                result.add(object);
            }
        }
        return result;
    }
    */

    /*
    public static Set getSet(Enumeration _e) {
        Set result = new HashSet();
        if(_e != null) {
            while(_e.hasMoreElements()) {
                Object object = _e.nextElement();
                result.add(object);
            }
        }
        return result;
    }
    */


    /**
     * Check whether Collection _one and _another have intersection.
     *
     * @return If the two Collection instance have intersection, i.e.
     *         at least one item included in both _one and _another Collection
     *         instance, return true; else return false.
     */
    public static boolean hasIntersection(Collection _one,
                                          Collection _another) {
        for (Iterator it = _another.iterator(); it.hasNext();) {
            Object item = it.next();
            if (_one.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasIntersection(List _one, List _another) {
        return hasIntersection(_one, _another);
    }


    public static boolean hasIntersection(Set _one, Set _another) {
        return hasIntersection(_one, _another);
    }

    /*
    public static boolean hasIntersection(List _one, List _another) {
        for(Iterator it = _another.iterator(); it.hasNext();) {
            Object item = it.next();
            if(_one.contains(item)) {
                return true;
            }
        }
        return false;
    }
    */

    /*
    public static boolean hasIntersection(Set _one, Set _another) {
        for(Iterator it = _another.iterator(); it.hasNext();) {
            Object item = it.next();
            if(_one.contains(item)) {
                return true;
            }
        }
        return false;
    }
    */


    public static List getIntersection(List _one, List _another) {
        List intersection = new ArrayList();
        for (Iterator it = _another.iterator(); it.hasNext();) {
            Object item = it.next();
            if (_one.contains(item)) {
                intersection.add(item);
            }
        }
        return intersection;
    }


    public static Set getIntersection(Set _one, Set _another) {
        Set intersection = new HashSet();
        for (Iterator it = _another.iterator(); it.hasNext();) {
            Object item = it.next();
            if (_one.contains(item)) {
                intersection.add(item);
            }
        }
        return intersection;
    }

    /**
     * 在_augend上加上_addend中的条目。_augend发生变化。
     */
    public static void add(Set _augend, Set _addend) {
        for (Iterator it = _addend.iterator(); it.hasNext();) {
            Object item = it.next();
            _augend.add(item);
        }
    }


    /**
     * Get a collection, item is string value of element of _collection.
     * If one element of _collection is null, the string value is "null".
     */
    public static Collection toString(Collection _collection) {
        ArrayList strings = new ArrayList();
        for (Iterator it = _collection.iterator(); it.hasNext();) {
            Object value = it.next();
            if (value == null) {
                strings.add("null");
            } else {
                strings.add(value.toString());
            }
        }
        return strings;
    }


    /**
     * 判断两个Collection对象是否相同，即_one包含_another，并且_another
     * 包含_one。
     * 注意，两个Collection对象相同，并不表示一致(equals)。
     */
    public static boolean isSame(Collection _one, Collection _another) {
        if (_one == null || _another == null) {
            throw new IllegalArgumentException("Parameter both _one and _another can't be null.");
        }

        if (_one.containsAll(_another) && _another.containsAll(_one)) {
            return true;
        }
        return false;
    }
}
