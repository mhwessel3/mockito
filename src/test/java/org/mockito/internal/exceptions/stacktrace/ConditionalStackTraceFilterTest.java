/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.stacktrace;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockito.internal.configuration.ConfigurationAccess;
import org.mockitoutil.TestBase;

import static org.mockitoutil.Conditions.onlyThoseClassesInStackTrace;
import static org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter.filter;

public class ConditionalStackTraceFilterTest extends TestBase {


    @Test
    public void shouldNotFilterWhenConfigurationSaysNo() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(false);

        Throwable t = new TraceBuilder().classes(
                "org.test.MockitoSampleTest",
                "org.mockito.Mockito"
        ).toThrowable();

        filter(t);

        Assertions.assertThat(t).has(onlyThoseClassesInStackTrace("org.mockito.Mockito", "org.test.MockitoSampleTest"));
    }

    @Test
    public void shouldFilterWhenConfigurationSaysYes() {
        ConfigurationAccess.getConfig().overrideCleansStackTrace(true);

        Throwable t = new TraceBuilder().classes(
                "org.test.MockitoSampleTest",
                "org.mockito.Mockito"
        ).toThrowable();

        filter(t);

        Assertions.assertThat(t).has(onlyThoseClassesInStackTrace("org.test.MockitoSampleTest"));
    }
}
