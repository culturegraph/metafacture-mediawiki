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
package org.culturegraph.mf.mediawiki.analyzers;

import org.culturegraph.mf.framework.ObjectPipe;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.mediawiki.WikiTextParser.ParseLevel;
import org.culturegraph.mf.mediawiki.objects.WikiPage;

/**
 * Modules which process WikiPage objects should implement this interface
 * to inform users about the representations of the wiki page (text,
 * pre-processed AST, ...) the modules expect to find in the WikiPage
 * objects.
 *
 * @see MultiAnalyzer
 *
 * @author Christoph Böhme
 *
 */
public interface Analyzer
		extends ObjectPipe<WikiPage, StreamReceiver> {

	boolean wikiTextOnly();

	ParseLevel requiredParseLevel();

}
