package com.github.fppt.jedismock.comparisontests.sortedsets;

import com.github.fppt.jedismock.comparisontests.ComparisonBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.KeyedZSetElement;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ComparisonBase.class)
public class TestBZPopMin {

    private static final String ZSET_KEY_1 = "myzset";
    private static final String ZSET_KEY_2 = "ztmp";

    @BeforeEach
    public void setUp(Jedis jedis) {
        jedis.flushDB();

    }

    @TestTemplate
    public void testBZPopMinFromSingleExistingSortedSet(Jedis jedis) {
        jedis.zadd(ZSET_KEY_1, 1, "b");
        jedis.zadd(ZSET_KEY_1, 0, "a");
        jedis.zadd(ZSET_KEY_1, 2, "c");
        KeyedZSetElement result = jedis.bzpopmin(0, ZSET_KEY_2, ZSET_KEY_1, "aaa");
        KeyedZSetElement expected = new KeyedZSetElement(ZSET_KEY_1, "a", 0.0);

        assertEquals(expected, result);
    }

    @TestTemplate
    public void testBZPopMinFromMultiplyExistingSortedSet(Jedis jedis) {
        jedis.zadd(ZSET_KEY_1, 0, "a");
        jedis.zadd(ZSET_KEY_1, 1, "b");
        jedis.zadd(ZSET_KEY_1, 2, "c");

        jedis.zadd(ZSET_KEY_2, 3, "d");
        jedis.zadd(ZSET_KEY_2, 4, "e");
        jedis.zadd(ZSET_KEY_2, 5, "f");
        KeyedZSetElement result = jedis.bzpopmin(0, ZSET_KEY_2, ZSET_KEY_1, "aaa");
        KeyedZSetElement expected = new KeyedZSetElement(ZSET_KEY_2, "d", 3.0);

        assertEquals(expected, result);
    }

    @TestTemplate
    public void testBZPopMinFromEmptySortedSetAndTimeOut(Jedis jedis) {
        long timeout = 1;
        long startTime = System.currentTimeMillis();
        assertThrows(NullPointerException.class, () ->
               jedis.bzpopmin(timeout, ZSET_KEY_2, ZSET_KEY_1, "aaa")
        );
        long finishTime = System.currentTimeMillis();
        assertTrue(finishTime - startTime >= timeout * 1000);
    }
}
