/*
 * Copyright 2023 IBM Corporation and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an \"AS IS\" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package testify.util;

import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public enum Assertions {
    ;
    public static AssertionFailedError failf(String format, Object... params) {
        throw new AssertionFailedError(String.format(format, params));
    }

    public static <T extends Throwable> T assertThrows(Class<T> type, Executable executable) {
        return org.junit.jupiter.api.Assertions.assertThrows(type, executable);
    }

    public static <T extends Throwable> T assertThrows(Class<? extends Throwable> type, Class<T> causeType, Executable executable) {
        Throwable cause = assertThrows(type,executable).getCause();
        assertThat(cause, is(instanceOf(causeType)));
        return causeType.cast(cause);
    }

    public static <T extends Throwable> T assertThrows(Class<? extends Throwable> type, Class<? extends Throwable> type2, Class<T> causeType, Executable executable) {
        Throwable cause = assertThrows(type,type2,executable).getCause();
        assertThat(cause, is(instanceOf(causeType)));
        return causeType.cast(cause);
    }

    public static <T extends Throwable> T assertThrowsExactly(Class<T> type, Executable executable) {
        return org.junit.jupiter.api.Assertions.assertThrowsExactly(type, executable);
    }

    public static <T extends Throwable> T assertThrowsExactly(Class<? extends Throwable> type, Class<T> causeType, Executable executable) {
        Throwable cause = assertThrowsExactly(type,executable).getCause();
        assertSame(causeType, notNull(cause).getClass());
        return causeType.cast(cause);
    }

    public static <T extends Throwable> T assertThrowsExactly(Class<? extends Throwable> type, Class<? extends Throwable> type2, Class<T> causeType, Executable executable) {
        Throwable cause = assertThrowsExactly(type,type2,executable).getCause();
        assertSame(causeType, notNull(cause).getClass());
        return causeType.cast(cause);
    }

    private static <T> T notNull(T t) {
        assertNotNull(t);
        return t;
    }
}
