/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;

public final class FieldReader {

     public static Object target;
     public static Field field;
     public static AccessibilityChanger changer = new AccessibilityChanger();

    private FieldReader() {
    }

    public static boolean isNull() {
            return read() == null;
    }

    public static void enable(){
        changer.enableAccess(field);
    }

    public static Object read() {
        try {
            return field.get(target);
        } catch (Exception e) {
            throw new MockitoException("Cannot read state from field: " + field + ", on instance: " + target);
        }
    }
}
