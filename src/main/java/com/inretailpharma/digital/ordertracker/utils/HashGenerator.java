package com.inretailpharma.digital.ordertracker.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import com.inretailpharma.digital.ordertracker.exception.HashException;

public class HashGenerator {
	
	private HashGenerator() {
		
	}

    private static final String ALGORITHM_FOR_HASHING = "MD5";

    /**
     * Generates a hash for the given value.
     */
    public static String hash(String valueToHash) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance(ALGORITHM_FOR_HASHING);
        } catch (NoSuchAlgorithmException e) {
            throw new HashException("MD5 Algorithm is not set. Perhaps JCE is not set.", e);
        }

        byte[] bytesOfDigest = md.digest(valueToHash.getBytes(StandardCharsets.UTF_8));

        return DatatypeConverter.printHexBinary(bytesOfDigest).toUpperCase();
    }
}
