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
package org.culturegraph.mf.mediawiki.example;

import java.io.IOException;

import org.culturegraph.mf.formeta.FormetaEncoder;
import org.culturegraph.mf.formeta.formatter.FormatterStyle;
import org.culturegraph.mf.io.ObjectStdoutWriter;
import org.culturegraph.mf.io.ResourceOpener;
import org.culturegraph.mf.javaintegration.MapToStream;
import org.culturegraph.mf.mediawiki.analyzers.TemplateExtractor;
import org.culturegraph.mf.mediawiki.WikiTextParser;
import org.culturegraph.mf.mediawiki.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.WikiXmlHandler;
import org.culturegraph.mf.statistics.Histogram;
import org.culturegraph.mf.xml.XmlDecoder;

/**
 * Prints a histogram of the templates used in wikipedia.
 *
 * @author Christoph Böhme
 *
 */
public final class CountTemplates {

	private CountTemplates() {
		// Nothing to do
	}

	public static void main(final String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: CountTemplates WIKI-XML-DUMP TEMPLATE-NAME-PATTERN");
		}

		final String wikiDump = args[0];
		final String templateNamePattern = args[1];

		final ResourceOpener opener = new ResourceOpener();
		final XmlDecoder xmlDecoder = new XmlDecoder();
		final WikiXmlHandler xmlHandler = new WikiXmlHandler();
		xmlHandler.setIncludeNamespaceIds("0");
		final WikiTextParser wikiParser = new WikiTextParser();
		wikiParser.setParseLevel(ParseLevel.PREPROCESS);
		final TemplateExtractor templateExtactor = new TemplateExtractor();
		templateExtactor.setNamePattern(templateNamePattern);
		final Histogram histogram = new Histogram();
		histogram.setCountEntities(true);

		opener.setReceiver(xmlDecoder)
				.setReceiver(xmlHandler)
				.setReceiver(wikiParser)
				.setReceiver(templateExtactor)
				.setReceiver(histogram);

		opener.process(wikiDump);
		opener.closeStream();

		final MapToStream mapToStream = new MapToStream();
		final FormetaEncoder encoder = new FormetaEncoder();
		encoder.setStyle(FormatterStyle.MULTILINE);
		final ObjectStdoutWriter<String> writer = new ObjectStdoutWriter<String>();

		mapToStream
			.setReceiver(encoder)
			.setReceiver(writer);

		mapToStream.process(histogram.getHistogram());
		mapToStream.closeStream();
	}

}
