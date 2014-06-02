/**   
 * License Agreement for OpenSearchServer
 *
 * Copyright (C) 2008-2013 Emmanuel Keller / Jaeksoft
 * 
 * http://www.open-search-server.com
 * 
 * This file is part of OpenSearchServer.
 *
 * OpenSearchServer is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * OpenSearchServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OpenSearchServer. 
 *  If not, see <http://www.gnu.org/licenses/>.
 **/

package com.jaeksoft.searchlib.index;

import java.io.IOException;
import java.util.Collection;

import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.request.AbstractSearchRequest;
import com.jaeksoft.searchlib.schema.Schema;
import com.jaeksoft.searchlib.webservice.query.document.IndexDocumentResult;

public interface WriterInterface {

	public void deleteAll() throws SearchLibException, IOException;

	public int deleteDocument(Schema schema, String field, String value)
			throws SearchLibException, IOException;

	public int deleteDocuments(Schema schema, String field,
			Collection<String> values) throws SearchLibException, IOException;

	public int deleteDocuments(AbstractSearchRequest query)
			throws SearchLibException, IOException;

	public void addUpdateInterface(UpdateInterfaces updateInterface)
			throws SearchLibException;

	public boolean updateDocument(Schema schema, IndexDocument document)
			throws SearchLibException, IOException;

	public int updateDocuments(Schema schema,
			Collection<IndexDocument> documents) throws SearchLibException,
			IOException;

	public int updateIndexDocuments(Schema schema,
			Collection<IndexDocumentResult> documents)
			throws SearchLibException;

	public void optimize() throws SearchLibException, IOException;

	public boolean isOptimizing();

	public void mergeData(WriterInterface source) throws SearchLibException,
			IOException;

	public boolean isMerging();
}
