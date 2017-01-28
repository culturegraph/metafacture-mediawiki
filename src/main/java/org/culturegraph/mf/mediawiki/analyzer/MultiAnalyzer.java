/*
 *  Copyright 2013 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.mediawiki.analyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.culturegraph.mf.commons.ResourceUtil;
import org.culturegraph.mf.commons.reflection.ObjectFactory;
import org.culturegraph.mf.framework.MetafactureException;
import org.culturegraph.mf.framework.ObjectPipe;
import org.culturegraph.mf.framework.StreamPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.culturegraph.mf.framework.helpers.DefaultStreamPipe;
import org.culturegraph.mf.mediawiki.converter.WikiTextParser;
import org.culturegraph.mf.mediawiki.type.WikiPage;
import org.culturegraph.mf.plumbing.ObjectTee;

/**
 * A convenience module for managing multiple mediawiki analyzers. The
 * MultiAnalyzer asks all analyzers for the wiki page representation
 * they require (using the {@link Analyzer}-interface) and sets up the
 * required {@link WikiTextParser}s. The output of the analyses are
 * returned as a single record for each input page.
 *
 * @see Analyzer
 *
 * @author Christoph Böhme
 *
 */
@Description("A convenience module for managing multiple mediawiki analyzers.")
@In(WikiPage.class)
@Out(StreamReceiver.class)
public final class MultiAnalyzer implements ObjectPipe<WikiPage, StreamReceiver> {

	private static final String PROPERTIES_LOCATION = "analyzer.properties";
	private static final String USER_PROPERTIES_LOCATION = "analyzer-user.properties";

	private static final ObjectFactory<Analyzer> ANALYZER_FACTORY = new ObjectFactory<Analyzer>();

	private static final Pattern CONSTRUCTOR_PATTERN = Pattern.compile("^\\s*([^(]*)\\((.*)\\)\\s*$");

	private String configFile;
	private String parserConfig = WikiTextParser.DEFAULT_CONFIG;

	private final ObjectTee<WikiPage> tee = new ObjectTee<WikiPage>();
	private final Map<WikiTextParser.ParseLevel, ObjectTee<WikiPage>> parsers = new HashMap<>();
	private final StreamPipe<StreamReceiver> merger =  new StripRecordBounderies();
	private StreamReceiver receiver;

	static {
		try {
			ANALYZER_FACTORY.loadClassesFromMap(ResourceUtil.loadProperties(PROPERTIES_LOCATION), Analyzer.class);
		} catch (IOException e) {
			throw new MetafactureException("Could not load properties", e);
		}
		try {
			ANALYZER_FACTORY.loadClassesFromMap(ResourceUtil.loadProperties(USER_PROPERTIES_LOCATION), Analyzer.class);
		} catch (IOException e) {
			// user properties are not mandatory
		}
	}

	public MultiAnalyzer(final String configFile) throws IOException {
		this.configFile = configFile;
	}

	public void setParserConfig(final String parserConfig) {
		this.parserConfig = parserConfig;
	}

	public String getParserConfig() {
		return parserConfig;
	}

	public void addAnalyzer(final Analyzer analyzer) {
		if (analyzer.wikiTextOnly()) {
			tee.addReceiver(analyzer);
		} else {
			ObjectTee<WikiPage> parserTee = parsers.get(analyzer.requiredParseLevel());
			if (parserTee == null) {
				final WikiTextParser parser;
				try {
					parser = new WikiTextParser(parserConfig);
				} catch (IOException e) {
					throw new MetafactureException(e);
				}
				parser.setParseLevel(analyzer.requiredParseLevel());
				parserTee = new ObjectTee<>();

				tee.addReceiver(parser);
				parser.setReceiver(parserTee);

				parsers.put(analyzer.requiredParseLevel(), parserTee);
			}

			parserTee.addReceiver(analyzer);
		}

		analyzer.setReceiver(merger);
	}

	@Override
	public <R extends StreamReceiver> R setReceiver(final R receiver) {
		merger.setReceiver(receiver);
		this.receiver = receiver;
		return receiver;
	}

	@Override
	public void process(final WikiPage page) {
		if (configFile != null) {
			init();
			configFile = null;
		}
		receiver.startRecord(Long.toString(page.getPageId()));
		tee.process(page);
		receiver.endRecord();
	}

	@Override
	public void resetStream() {
		merger.resetStream();
	}

	@Override
	public void closeStream() {
		merger.closeStream();
	}

	private void init() {
		final List<String> lines = new LinkedList<String>();
		try {
			ResourceUtil.loadTextFile(configFile, lines);
		} catch(IOException e) {
			throw new MetafactureException(e);
		}

		for (String line : lines) {
			final Matcher matcher = CONSTRUCTOR_PATTERN.matcher(line);
			final String name;
			final Object[] args;
			if (matcher.matches()) {
				name = matcher.group(1).trim();
				args = new String[] { matcher.group(2) };
			} else {
				name = line.trim();
				args = new String[0];
			}
			final Analyzer analyzer = ANALYZER_FACTORY.newInstance(name, args);

			addAnalyzer(analyzer);
		}
	}

	/**
	 * @author Markus Michael Geipel strips start and end record
	 */
	protected static final class StripRecordBounderies extends DefaultStreamPipe<StreamReceiver> {
		@Override
		public void endEntity() {
			getReceiver().endEntity();
		}

		@Override
		public void startEntity(final String name) {
			getReceiver().startEntity(name);
		}

		@Override
		public void literal(final String name, final String value) {
			getReceiver().literal(name, value);
		}
	}

}
