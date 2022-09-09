package homework_2;

import org.junit.Assert;
import org.junit.Test;

public class TestComplexExamples {

    @Test
    public void testFuzzySearch() {
        Assert.assertTrue(ComplexExamples.fuzzySearch("car", "ca6$$#_rtwheel"));   // true
        Assert.assertTrue(ComplexExamples.fuzzySearch("cwhl", "cartwheel")); // true
        Assert.assertTrue(ComplexExamples.fuzzySearch("cwhee", "cartwheel")); // true
        Assert.assertTrue(ComplexExamples.fuzzySearch("cartwheel", "cartwheel")); // true
        Assert.assertFalse(ComplexExamples.fuzzySearch("cwheeel", "cartwheel")); // false
        Assert.assertFalse(ComplexExamples.fuzzySearch("lw", "cartwheel")); // false

        Assert.assertTrue(ComplexExamples.fuzzySearch("", "cartwheel")); // true
        Assert.assertFalse(ComplexExamples.fuzzySearch("lw", "")); // false
    }
}