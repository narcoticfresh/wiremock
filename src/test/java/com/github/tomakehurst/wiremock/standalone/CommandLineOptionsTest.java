/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.standalone;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.github.tomakehurst.wiremock.common.ProxySettings;
import org.junit.Test;

public class CommandLineOptionsTest {

	@Test
	public void returnsVerboseTrueWhenOptionPresent() {
		CommandLineOptions options = new CommandLineOptions("--verbose");
		assertThat(options.verboseLoggingEnabled(), is(true));
	}
	
	@Test
	public void returnsVerboseFalseWhenOptionNotPresent() {
		CommandLineOptions options = new CommandLineOptions("");
		assertThat(options.verboseLoggingEnabled(), is(false));
	}

	@Test
	public void returnsRecordMappingsTrueWhenOptionPresent() {
		CommandLineOptions options = new CommandLineOptions("--record-mappings");
		assertThat(options.recordMappingsEnabled(), is(true));
	}
	
	@Test
	public void returnsRecordMappingsFalseWhenOptionNotPresent() {
		CommandLineOptions options = new CommandLineOptions("");
		assertThat(options.recordMappingsEnabled(), is(false));
	}
	
	@Test
     public void setsPortNumberWhenOptionPresent() {
        CommandLineOptions options = new CommandLineOptions("--port", "8086");
        assertThat(options.portNumber(), is(8086));
    }

    @Test
    public void setsHttpsPortNumberWhenOptionPresent() {
        CommandLineOptions options = new CommandLineOptions("--https-port", "8443");
        assertThat(options.httpsEnabled(), is(true));
        assertThat(options.httpsPortNumber(), is(8443));
    }
	
	@Test(expected=Exception.class)
	public void throwsExceptionWhenPortNumberSpecifiedWithoutNumber() {
		new CommandLineOptions("--port");
	}
	
	@Test
	public void setsProxyAllRootWhenOptionPresent() {
		CommandLineOptions options = new CommandLineOptions("--proxy-all", "http://someotherhost.com/site");
		assertThat(options.specifiesProxyUrl(), is(true));
		assertThat(options.proxyUrl(), is("http://someotherhost.com/site"));
	}
	
	@Test(expected=Exception.class)
	public void throwsExceptionWhenProxyAllSpecifiedWithoutUrl() {
		new CommandLineOptions("--proxy-all");
	}
	
	@Test
	public void returnsBrowserProxyingEnabledWhenOptionSet() {
		CommandLineOptions options = new CommandLineOptions("--enable-browser-proxying");
		assertThat(options.browserProxyingEnabled(), is(true));
	}
	
	@Test
	public void setsAll() {
		CommandLineOptions options = new CommandLineOptions("--verbose", "--record-mappings", "--port", "8088", "--proxy-all", "http://somewhere.com");
		assertThat(options.verboseLoggingEnabled(), is(true));
		assertThat(options.recordMappingsEnabled(), is(true));
		assertThat(options.portNumber(), is(8088));
		assertThat(options.specifiesProxyUrl(), is(true));
		assertThat(options.proxyUrl(), is("http://somewhere.com"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void returnsHelpText() {
		CommandLineOptions options = new CommandLineOptions("--help");
		assertThat(options.helpText(), allOf(containsString("verbose")));
	}

    @Test
    public void returnsCorrectlyParsedProxyViaParameter() {
        CommandLineOptions options = new CommandLineOptions("--proxy-via", "somehost.mysite.com:8080");
        assertThat(options.proxyVia().host(), is("somehost.mysite.com"));
        assertThat(options.proxyVia().port(), is(8080));
    }

    @Test
    public void returnsNoProxyWhenNoProxyViaSpecified() {
        CommandLineOptions options = new CommandLineOptions();
        assertThat(options.proxyVia(), is(ProxySettings.NO_PROXY));
    }
}
