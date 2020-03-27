package url_shortener.app.common;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class IDConverter {
    public static final IDConverter INSTANCE = new IDConverter();

    private IDConverter() {
        initializeCharToIndexTable();
        initializeIndexToCharTable();
    }

    // Create two tables to assist in coverting base10 to base62 (and vice-versa)
    private static HashMap<Character, Integer> charToIndexTable;
    private static List<Character> indexToCharTable;

    /**
     * Initializes a Hashmap that will hold a series of numbers as a value, with chars
     * ranging from a-z, A-Z, 0-9 as the key
     *
     */
    private void initializeCharToIndexTable() {
        // Char acts as the key, the index is the value
        charToIndexTable = new HashMap<>();

        for (int i = 0; i < 26; i++) {
            char c = 'a';
            c += i;
            charToIndexTable.put(c, i);
        }

        // Need to reinitialize char in order to skip unneeded characters
        for (int i = 26; i < 52; i++) {
            char c = 'A';
            c += (i - 26);
            charToIndexTable.put(c, i);
        }

        // Need to reinitialize char in order to skip unneeded characters
        for (int i = 52; i < 62; i++) {
            char c = '0';
            c += (i - 52);
            charToIndexTable.put(c, i);
        }
    }

    /**
     * Initializes an ArrayList that will hold a series of chars, with the index as the key to each char
     *
     */
    private void initializeIndexToCharTable() {
        // Index acts a key for each char
        indexToCharTable = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            char c = 'a';
            c += i;
            indexToCharTable.add(c);
        }

        // Need to reinitialize char in order to skip unneeded characters
        for (int i = 26; i < 52; i++) {
            char c = 'A';
            c += (i - 26);
            indexToCharTable.add(c);
        }

        // Need to reinitialize char in order to skip unneeded characters
        for (int i = 52; i < 62; i++) {
            char c = '0';
            c += (i - 52);
            indexToCharTable.add(c);
        }
    }

    /**
     * Converts a base 10 id into a unique id that's in base62
     *
     * @param  id   a base 10 id that's used as the key in Redis
     * @return      a unique base62 id
     */
    public static String createUniqueID (Long id) {
        List<Integer> base62ID = convertBase10ToBase62(id);
        StringBuilder uniqueURLID = new StringBuilder();

        for (int number: base62ID) {
            uniqueURLID.append(indexToCharTable.get(number));
        }

        // Return the unique url id as a string
        return uniqueURLID.toString();
    }

    /**
     * Converts a base 10 integer into a list of integers that range from 0-62
     *
     * @param  id   a base 10 id that's used as the key in Redis
     * @return      a linked list of digits that will be used to correspond to values ranging from a-z, A-Z, 0-9
     */
    public static List<Integer> convertBase10ToBase62(Long id) {
        List<Integer> integers = new LinkedList<>();

        // Divide the ID by 62 until the quotient is equal to 0
        while (id > 0) {
            int remainder = (int)(id % 62);
            // Appends the remainder to the front of the Linked List
            ((LinkedList<Integer>) integers).addFirst(remainder);
            // Take the quotient and continue to divide by 62
            id /= 62;
        }

        return integers;
    }

    /**
     * Converts a unique string id into a base 10 integer value that is used as the redis key
     *
     * @param  id   a unique id string
     * @return      a base 10 integer value that will be used as a Redis key
     */
    public static Long generateRedisKeyFromUniqueID(String id) {
        List<Character> base62IDs = new ArrayList<>();

        // Splits the ID into a list of characters
        for (int i = 0; i < id.length(); i ++) {
            base62IDs.add(id.charAt(i));
        }

        // Returns a base 10 integer value
        Long redisKey = convertBase62ToBase10(base62IDs);

        return redisKey;
    }

    /**
     * Converts a list of characters that represent a base62 value back into a base 10 integer value
     *
     * @param  ids  a series of characters representing a base62 value
     * @return      a base 10 integer value
     */
    private static Long convertBase62ToBase10(List<Character> ids) {
        long id = 0L;

        // Conversion formula for converting a base62 value into a base10 value
        for (int i = 0, exponent = ids.size() - 1; i < ids.size(); i++, exponent--) {
            int base10 = charToIndexTable.get(ids.get(i));

            id += (base10 * Math.pow(62.0, exponent));
        }

        return id;
    }
}
