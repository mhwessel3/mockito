/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class LenientCopyTool {

    public static boolean disableAccessForTest = false;

    private LenientCopyTool(){
        throw new UnsupportedOperationException();
    }

    public static <T> void copyToMock(T from, T mock) {
        copy(from, mock, from.getClass());
    }

    public static <T> void copyToRealObject(T from, T to) {
        copy(from, to, from.getClass());
    }

    private static <T> void copy(T from, T to, Class<?> fromClazz) {
        while (fromClazz != Object.class) {
            copyValues(from, to, fromClazz);
            fromClazz = fromClazz.getSuperclass();
        }
    }

    private static <T> void copyValues(T from, T mock, Class<?> classFrom) {
        Field[] fields = classFrom.getDeclaredFields();

        for (Field field : fields) {
            // ignore static fields
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                AccessibilityChanger.enableAccess(field);
                //Only used for testing, is there a better solution than this?
                if(disableAccessForTest){
                    AccessibilityChanger.safelyDisableAccess(field);
                }
                FieldCopier.copyValue(from, mock, field);
            } catch (Throwable t) {
                //Ignore - be lenient - if some field cannot be copied then let's be it
            } finally {
                AccessibilityChanger.safelyDisableAccess(field);
            }
        }
    }
}
