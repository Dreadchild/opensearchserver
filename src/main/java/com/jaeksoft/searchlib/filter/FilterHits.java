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

package com.jaeksoft.searchlib.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;

import com.jaeksoft.searchlib.result.ResultSearchSingle;
import com.jaeksoft.searchlib.result.collector.docsethit.DocSetHitBaseCollector.FilterHitsCollector;
import com.jaeksoft.searchlib.result.collector.docsethit.DocSetHitBaseCollector.FilterHitsCollector.Segment;
import com.jaeksoft.searchlib.util.Timer;
import com.jaeksoft.searchlib.util.bitset.BitSetFactory;
import com.jaeksoft.searchlib.util.bitset.BitSetInterface;
import com.jaeksoft.searchlib.webservice.query.search.SearchQueryAbstract.OperatorEnum;

public class FilterHits extends Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7434283983275758714L;

	protected final HashMap<IndexReader, BitSetInterface> docSetMap;
	protected final TreeMap<Integer, BitSetInterface> docBaseMap;
	private final boolean isNegative;

	public FilterHits(boolean isNegative) {
		docSetMap = new HashMap<IndexReader, BitSetInterface>();
		docBaseMap = new TreeMap<Integer, BitSetInterface>();
		this.isNegative = isNegative;
	}

	public FilterHits(FilterHitsCollector collector, boolean isNegative,
			Timer timer) {
		this(isNegative);
		Timer t = new Timer(timer, "FilterHits - copy segments");
		for (Segment segment : collector.segments) {
			BitSetInterface docSet = segment.docBitSet.clone();
			if (isNegative)
				docSet.flip(0, docSet.size());
			docSetMap.put(segment.indexReader, docSet);
			docBaseMap.put(segment.docBase, docSet);
		}
		t.end(null);
	}

	public FilterHits(ResultSearchSingle result, boolean negative, Timer timer) {
		this(result.getDocSetHits().getFilterHitsCollector(), negative, timer);
	}

	final void operate(FilterHits sourceFilterHits, OperatorEnum operator) {
		if (docSetMap.isEmpty()) {
			for (Map.Entry<IndexReader, BitSetInterface> entry : sourceFilterHits.docSetMap
					.entrySet())
				docSetMap.put(entry.getKey(), (BitSetInterface) entry
						.getValue().clone());
		} else {
			for (Map.Entry<IndexReader, BitSetInterface> entry : sourceFilterHits.docSetMap
					.entrySet()) {
				switch (operator) {
				case AND:
					docSetMap.get(entry.getKey()).and(entry.getValue());
					break;
				case OR:
					docSetMap.get(entry.getKey()).or(entry.getValue());
					break;
				}
			}
		}
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		return BitSetFactory.INSTANCE.getDocIdSet(docSetMap.get(reader));
	}

	final public void fastRemove(int doc) {
		Integer floorKey = docBaseMap.floorKey(doc);
		BitSetInterface bitSet = docBaseMap.get(floorKey);
		doc -= floorKey;
		if (isNegative)
			bitSet.set(doc);
		else
			bitSet.clear(doc);
	}
}
