package algorithms;

import metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DSUTest {

    @Test
    void testInitialComponents() {
        Metrics metrics = new Metrics();
        DSU dsu = new DSU(5, metrics);

        assertEquals(5, dsu.components());
    }

    @Test
    void testUnion() {
        Metrics metrics = new Metrics();
        DSU dsu = new DSU(5, metrics);

        dsu.union(0, 1);

        assertEquals(dsu.find(0), dsu.find(1));
        assertEquals(4, dsu.components());
    }

    @Test
    void testSeveralUnions() {
        Metrics metrics = new Metrics();
        DSU dsu = new DSU(6, metrics);

        dsu.union(0, 1);
        dsu.union(1, 2);
        dsu.union(3, 4);

        assertEquals(dsu.find(0), dsu.find(2));
        assertEquals(dsu.find(3), dsu.find(4));

        assertNotEquals(dsu.find(0), dsu.find(3));

        assertEquals(3, dsu.components());
    }

    @Test
    void testDuplicateUnion() {
        Metrics metrics = new Metrics();
        DSU dsu = new DSU(3, metrics);

        assertTrue(dsu.union(0, 1));
        assertFalse(dsu.union(0, 1));

        assertEquals(2, dsu.components());
    }
}