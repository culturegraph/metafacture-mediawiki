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
package org.culturegraph.mf.mediawiki.sink;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.culturegraph.mf.exceptions.MetafactureException;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.mediawiki.type.WikiPage;

import de.fau.cs.osr.ptk.common.AstPrinter;

/**
 * Writes out the AST of a {@link WikiPage}.
 * 
 * @author Christoph Böhme
 *
 */
public final class AstWriter implements ObjectReceiver<WikiPage> {

	private final Writer writer;
	
	public AstWriter() {
		this(new OutputStreamWriter(System.out));
	}
	
	public AstWriter(final Writer writer) {
		this.writer = writer;
	}
	
	@Override
	public void resetStream() {
		throw new UnsupportedOperationException("Cannot reset PrintAst");
	}

	@Override
	public void closeStream() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new MetafactureException(e);
		}
	}

	@Override
	public void process(final WikiPage page) {
		try {
			writer.append(AstPrinter.print(page.getWikiAst().getPage()));
		} catch (IOException e) {
			throw new MetafactureException(e);
		}
	}

}
