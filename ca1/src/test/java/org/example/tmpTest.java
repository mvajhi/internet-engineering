package org.example;

import org.junit.Assert;
import org.junit.Test;

public class tmpTest {
    @Test
    public void t1() {
        tmp tmp = new tmp();
        Assert.assertEquals(3, tmp.add(1, 2));
    }

    @Test
    public void t2() {
        tmp tmp = new tmp();
        Assert.assertEquals(5, tmp.add(2, 3));
    }

    @Test
    public void t3() {
        tmp tmp = new tmp();
        Assert.assertEquals(6, tmp.add(3, 4));
    }

    @Test
    public void t4() {
        tmp tmp = new tmp();
        Assert.assertEquals(7, tmp.add(3, 4));
    }
}
