/**   
 * License Agreement for OpenSearchServer
 *
 * Copyright (C) 2013-2014 Emmanuel Keller / Jaeksoft
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

package com.jaeksoft.searchlib.webservice.stopwords;

import javax.ws.rs.core.UriInfo;

import com.jaeksoft.searchlib.Client;
import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.analysis.stopwords.StopWordsManager;
import com.jaeksoft.searchlib.webservice.AbstractDirectoryImpl;
import com.jaeksoft.searchlib.webservice.CommonListResult;
import com.jaeksoft.searchlib.webservice.CommonResult;

public class StopWordsImpl implements RestStopWords {

	private class StopWordsDirectoryImpl extends
			AbstractDirectoryImpl<String, StopWordsManager> {

		@Override
		protected StopWordsManager getManager(Client client)
				throws SearchLibException {
			return client.getStopWordsManager();
		}
	}

	@Override
	public CommonListResult list(UriInfo uriInfo, String index, String login,
			String key) {
		return new StopWordsDirectoryImpl().list(uriInfo, index, login, key);
	}

	@Override
	public CommonResult set(UriInfo uriInfo, String index, String login,
			String key, String name, String content) {
		return new StopWordsDirectoryImpl().set(uriInfo, index, login, key,
				name, content);
	}

	@Override
	public String get(UriInfo uriInfo, String index, String login, String key,
			String name) {
		return new StopWordsDirectoryImpl().get(uriInfo, index, login, key,
				name);
	}

	@Override
	public CommonResult exists(UriInfo uriInfo, String index, String login,
			String key, String name) {
		return new StopWordsDirectoryImpl().exists(uriInfo, index, login, key,
				name);
	}

	@Override
	public CommonResult delete(UriInfo uriInfo, String index, String login,
			String key, String name) {
		return new StopWordsDirectoryImpl().delete(uriInfo, index, login, key,
				name);
	}

}
