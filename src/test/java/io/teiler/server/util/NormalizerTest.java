package io.teiler.server.util;

import org.junit.Assert;
import org.junit.Test;

public class NormalizerTest {
    
    private static final String GROUP_ID_LETTERS_UPPERCASE = "TEST";
    private static final String GROUP_ID_LETTERS_LOWERCASE = "test";
    
    private static final String GROUP_ID_NUMBERS_ONLY = "123";
    
    private static final String GROUP_ID_LETTERS_UPPERCASE_AND_NUMBERS = "TEST123";
    private static final String GROUP_ID_LETTERS_LOWERCASE_AND_NUMBERS = "test123";

    @Test
    public void testNormalizeGroupIdLettersOnlyUppercase() {
        Assert.assertEquals(
                GROUP_ID_LETTERS_LOWERCASE,
                Normalizer.normalizeGroupId(GROUP_ID_LETTERS_UPPERCASE));
    }
    
    @Test
    public void testNormalizeGroupIdLettersOnlyLowercase() {
        Assert.assertEquals(
                GROUP_ID_LETTERS_LOWERCASE,
                Normalizer.normalizeGroupId(GROUP_ID_LETTERS_LOWERCASE));
    }
    
    @Test
    public void testNormalizeGroupIdNumbersOnly() {
        Assert.assertEquals(
                GROUP_ID_NUMBERS_ONLY,
                Normalizer.normalizeGroupId(GROUP_ID_NUMBERS_ONLY));
    }
    
    @Test
    public void testNormalizeGroupIdLettersUppercaseAndNumbers() {
        Assert.assertEquals(
                GROUP_ID_LETTERS_LOWERCASE_AND_NUMBERS,
                Normalizer.normalizeGroupId(GROUP_ID_LETTERS_UPPERCASE_AND_NUMBERS));
    }
    
    @Test
    public void testNormalizeGroupIdLettersLowercaseAndNumbers() {
        Assert.assertEquals(
                GROUP_ID_LETTERS_LOWERCASE_AND_NUMBERS,
                Normalizer.normalizeGroupId(GROUP_ID_LETTERS_LOWERCASE_AND_NUMBERS));
    }
    
    
}
