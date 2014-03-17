/**   
 * License Agreement for OpenSearchServer
 *
 * Copyright (C) 2008-2014 Emmanuel Keller / Jaeksoft
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.lucene.search.Similarity;
import org.json.JSONException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.util.StringUtils;
import com.jaeksoft.searchlib.util.XPathParser;
import com.jaeksoft.searchlib.util.XmlWriter;

public class IndexConfig {

	private int searchCache;

	private int filterCache;

	private int fieldCache;

	private int termVectorCache;

	private URI remoteURI;

	private String keyField;

	private String keyMd5RegExp;

	private String similarityClass;

	private int maxNumSegments;

	public IndexConfig(Node node) throws URISyntaxException {
		maxNumSegments = 1;
		searchCache = XPathParser.getAttributeValue(node, "searchCache");
		filterCache = XPathParser.getAttributeValue(node, "filterCache");
		fieldCache = XPathParser.getAttributeValue(node, "fieldCache");
		if (fieldCache == 0)
			fieldCache = XPathParser.getAttributeValue(node, "documentCache");
		termVectorCache = XPathParser
				.getAttributeValue(node, "termVectorCache");
		String s = XPathParser.getAttributeString(node, "remoteURI");
		remoteURI = StringUtils.isEmpty(s) ? null : new URI(s);
		keyField = XPathParser.getAttributeString(node, "keyField");
		keyMd5RegExp = XPathParser.getAttributeString(node, "keyMd5RegExp");
		setSimilarityClass(XPathParser.getAttributeString(node,
				"similarityClass"));
		maxNumSegments = XPathParser.getAttributeValue(node, "maxNumSegments");
		if (maxNumSegments == 0)
			maxNumSegments = 1;
	}

	public void writeXmlConfig(XmlWriter xmlWriter) throws SAXException {
		xmlWriter.startElement("index", "searchCache",
				Integer.toString(searchCache), "filterCache",
				Integer.toString(filterCache), "fieldCache",
				Integer.toString(fieldCache), "termVectorCache",
				Integer.toString(termVectorCache), "remoteURI",
				remoteURI != null ? remoteURI.toString() : null, "keyField",
				keyField, "keyMd5RegExp", keyMd5RegExp, "similarityClass",
				similarityClass, "maxNumSegments",
				Integer.toString(maxNumSegments));
		xmlWriter.endElement();
	}

	/**
	 * @return the searchCache
	 */
	public int getSearchCache() {
		return searchCache;
	}

	/**
	 * @param searchCache
	 *            the searchCache to set
	 */
	public void setSearchCache(int searchCache) {
		this.searchCache = searchCache;
	}

	/**
	 * @return the filterCache
	 */
	public int getFilterCache() {
		return filterCache;
	}

	/**
	 * @param filterCache
	 *            the filterCache to set
	 */
	public void setFilterCache(int filterCache) {
		this.filterCache = filterCache;
	}

	/**
	 * @return the documentCache
	 */
	public int getFieldCache() {
		return fieldCache;
	}

	/**
	 * @param documentCache
	 *            the documentCache to set
	 */
	public void setFieldCache(int fieldCache) {
		this.fieldCache = fieldCache;
	}

	/**
	 * @return the termVectorCache
	 */
	public int getTermVectorCache() {
		return termVectorCache;
	}

	/**
	 * @param termVectorCache
	 *            the termVectorCache to set
	 */
	public void setTermVectorCache(int termVectorCache) {
		this.termVectorCache = termVectorCache;
	}

	/**
	 * @return the remoteURI
	 */
	public URI getRemoteURI() {
		return remoteURI;
	}

	/**
	 * @param remoteURI
	 *            the remoteURI to set
	 */
	public void setRemoteURI(URI remoteURI) {
		this.remoteURI = remoteURI;
	}

	/**
	 * @return the keyField
	 */
	public String getKeyField() {
		return keyField;
	}

	/**
	 * @param keyField
	 *            the keyField to set
	 */
	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}

	/**
	 * @return the keyMd5RegExp
	 */
	public String getKeyMd5RegExp() {
		return keyMd5RegExp;
	}

	/**
	 * @param keyMd5RegExp
	 *            the keyMd5RegExp to set
	 */
	public void setKeyMd5RegExp(String keyMd5RegExp) {
		this.keyMd5RegExp = keyMd5RegExp;
	}

	/**
	 * @param similarityClass
	 *            the similarityClass to set
	 */
	public void setSimilarityClass(String similarityClass) {
		this.similarityClass = similarityClass;
	}

	/**
	 * @return the similarityClass
	 */
	public String getSimilarityClass() {
		return similarityClass;
	}

	/**
	 * @return the maxNumSegments
	 */
	public int getMaxNumSegments() {
		return maxNumSegments;
	}

	/**
	 * @param maxNumSegments
	 *            the maxNumSegments to set
	 */
	public void setMaxNumSegments(int maxNumSegments) {
		this.maxNumSegments = maxNumSegments;
	}

	public Similarity getNewSimilarityInstance() throws SearchLibException {
		if (similarityClass == null)
			return null;
		try {
			return (Similarity) Class.forName(similarityClass).newInstance();
		} catch (InstantiationException e) {
			throw new SearchLibException(e);
		} catch (IllegalAccessException e) {
			throw new SearchLibException(e);
		} catch (ClassNotFoundException e) {
			throw new SearchLibException(e);
		}
	}

	public IndexAbstract getNewIndex(File configDir,
			boolean createIndexIfNotExists) throws IOException,
			URISyntaxException, SearchLibException, JSONException {
		return new IndexLucene(configDir, this, createIndexIfNotExists);
	}

}
