package com.inretailpharma.digital.ordertracker.utils;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomCodeGenerator {
	
	private RandomCodeGenerator() {
		
	}

    private static final String CODE_FORMAT = "%04d";
    private static final long START_NUMBER = 1_111;
    private static final long END_NUMBER = 9_999L;

    /**
     * Generates a code with the format 423234 (six numbers padded with zeroes).
     */
    public static String nextCode() {
        long nextLong = nextLong();
        return String.format(CODE_FORMAT, nextLong);
    }

    static long nextLong() {
        return RandomUtils.nextLong(START_NUMBER, END_NUMBER);
    }
}
