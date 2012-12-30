package com.migrate.service;

import com.migrate.webdata.model.GenericMap;
import com.migrate.webdata.model.PersistentSchema;
import com.migrate.webdata.model.PropertyIndex;
import com.migrate.storage.impl.JsonHelper;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


@Component("luceneIndexService")
public class LuceneIndexService {
	private static org.apache.log4j.Logger log = Logger.getLogger(LuceneIndexService.class);
	private static final String ID = "id";
	private static final String DATA = "data";
	private static final String DIR_ROOT = "/tmp/migrate/";
	
	@Autowired
	@Qualifier(value = "schemaService")
	private SchemaService schemaService;
	
	public void updateIndex(GenericMap data) throws IOException
    {
		String indexName = data.getBucket();
		PersistentSchema persistentSchema = schemaService.getSchema(indexName);
		if (persistentSchema == null) {
			return;
		}
		List<PropertyIndex> indexList = persistentSchema.getIndexList();
		Map<String, Object> map = getValueMap(indexList, data);
		updateIndexDo(indexName, map);
	}

	public void deleteIndex(String indexName, String id) throws IOException
    {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,analyzer);
	    iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		Directory dir = FSDirectory.open(new File(DIR_ROOT + indexName));
		IndexWriter writer = new IndexWriter(dir, iwc);
		writer.deleteDocuments(new Term(ID, id));
		writer.close();

	}

	private Map<String, Object> getValueMap(List<PropertyIndex> indexList, GenericMap data)
			throws IOException
    {
		Map<String, Object> map = new HashMap<String, Object>();
		for (PropertyIndex pIndex : indexList) {
			String fieldName = pIndex.getFieldName();
			map.put(fieldName, data.get(fieldName));
		}
		map.put(DATA, new String(JsonHelper.writeValueAsByte(data)));
		map.put(ID, data.getId());
		return map;
	}
	
	private void updateIndexDo(String indexName, Map<String, Object>valueMap) 
			throws CorruptIndexException, IOException
    {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,analyzer);
	    iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		Directory dir = FSDirectory.open(new File(DIR_ROOT + indexName));
		IndexWriter writer = new IndexWriter(dir, iwc);
		Document doc = createDoc(valueMap);
		writer.updateDocument(new Term(ID, valueMap.get(ID).toString()), doc);
		writer.close();		
	}
		
	private Document createDoc(Map<String, Object>valueMap)
    {
		Document doc = new Document();
		for ( Entry<String, Object> entry : valueMap.entrySet()) {
			String fieldName = entry.getKey();
			Field.Index fi = Field.Index.ANALYZED;
			log.info(" ************ filedName " + fieldName );
			log.info(" ************ " +  " object type: " + entry.getValue());

			if (fieldName.equals(ID)) {
				fi = Field.Index.NOT_ANALYZED;
			} else if (fieldName.equals(DATA)) {
				fi = Field.Index.NOT_ANALYZED;
			} 
			Field field = new Field(fieldName ,entry.getValue().toString(), Field.Store.YES, fi);
			doc.add(field);			
		}
		return doc;
	}

	public List<GenericMap> search(String indexName, String queryStr) throws ParseException, IOException
    {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
		
	    Query q = new QueryParser(Version.LUCENE_35, "firstName", analyzer).parse(queryStr);
	    try {
		    int hitsPerPage = 30;
		    Directory dir = FSDirectory.open(new File(DIR_ROOT + indexName));
		    log.info("lucene dir: " + DIR_ROOT + indexName);
		    IndexReader reader = IndexReader.open(dir);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    List<GenericMap> ret = collectResult(hits, searcher);
		    searcher.close();
			return ret;
	    } catch (IndexNotFoundException e) {
	    	return new ArrayList<GenericMap>();
	    }
	}
	
	private List<GenericMap>  collectResult (ScoreDoc[] hits, IndexSearcher searcher ) 
			throws CorruptIndexException, IOException
    {
		List<GenericMap> ret = new ArrayList<GenericMap>();
		log.info("Found " + hits.length + " hits.");
	    for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);		      
		      log.info(" ********* json:" + d.get(DATA));
		      ret.add(JsonHelper.readValue(d.get(DATA).getBytes(), GenericMap.class));
		      log.info((i + 1) + ". " + d.get(ID) +  "  data" + d.get(DATA));
	    }
	    return ret;
	}
}
	
