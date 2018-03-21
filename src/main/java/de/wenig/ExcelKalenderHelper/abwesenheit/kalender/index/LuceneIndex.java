package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.index;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;

import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Abwesenheit;
import de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data.Person;

public class LuceneIndex {
	
	
	public static final CharArraySet ENGLISH_STOP_WORDS_SET_WO_AT;
	
	static {
		final List<String> stopWords = Arrays.asList(
		      "a", "an", "and", "are", "as", "be", "but", "by",
		      "for", "if", "in", "into", "is", "it",
		      "no", "not", "of", "on", "or", "such",
		      "that", "the", "their", "then", "there", "these",
		      "they", "this", "to", "was", "will", "with"
		    );
		    final CharArraySet stopSet = new CharArraySet(stopWords, false);
		ENGLISH_STOP_WORDS_SET_WO_AT = CharArraySet.unmodifiableSet(stopSet);
	}
	
	private Log log = LogFactory.getLog(LuceneIndex.class);
	
	private Map<Integer, Person> personIndex = new HashMap<Integer, Person>();

	private final Analyzer analyzer = new StandardAnalyzer(ENGLISH_STOP_WORDS_SET_WO_AT);
	private Directory directory = new RAMDirectory();

	private static final String FIELD_NAME = "name";
	private static final String FIELD_VORNAME = "vorname";
	private static final String FIELD_FIRMA = "firma";
	private static final String FIELD_ORGANISATION = "organisation";
	private static final String FIELD_TEAM = "team";
	private static final String FIELD_ID = "id";

	private static final String SPEZIAL_ALLE = "ALLE";

	public void clearIndex() {
		directory = new RAMDirectory();
	}

	public void buildLuceneIndices(HashMap<Person, List<Abwesenheit>> abwesenheiten) {

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		try {
			IndexWriter iwriter = new IndexWriter(directory, config);
			for (Person p : abwesenheiten.keySet()) {
				Document doc = new Document();
				doc.add(new Field(FIELD_NAME, p.getName(), TextField.TYPE_STORED));
				doc.add(new Field(FIELD_VORNAME, p.getVorname(), TextField.TYPE_STORED));
				doc.add(new Field(FIELD_FIRMA, p.getFirma().getName(), TextField.TYPE_STORED));
				doc.add(new Field(FIELD_ORGANISATION, p.getOrganisation().getName(), TextField.TYPE_STORED));
				doc.add(new Field(FIELD_TEAM, p.getTeam().getName(), TextField.TYPE_STORED));
				doc.add(new Field(FIELD_ID, String.valueOf(p.getId()), TextField.TYPE_STORED));
				iwriter.addDocument(doc);

				personIndex.put(p.getId(), p);
			}
			iwriter.close();
		} catch(LockObtainFailedException ex){
			log.info("Lucene index kann nicht geschrieben werden, da aktuell gelockt", ex);
		}
		catch (IOException ioex) {
			log.error("Fehler beim bauen des Lucene-Index", ioex);
		}

	}

	public List<Person> suchePersonen(String suchString) {
		List<Person> result = new LinkedList<Person>();
		try {
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
					new String[] { FIELD_NAME, FIELD_VORNAME, FIELD_FIRMA, FIELD_ORGANISATION, FIELD_TEAM }, analyzer);
			queryParser.setDefaultOperator(Operator.AND);
			Query query = queryParser.parse(suchString.concat("*"));
			ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;
			for (ScoreDoc doc : hits) {
				Document hitDoc = isearcher.doc(doc.doc);
				int id = Integer.parseInt(hitDoc.get(FIELD_ID));
				Person p = personIndex.get(id);
				result.add(p);
			}

		} catch (IOException | ParseException ioException) {
			log.error(ioException);
		}
		return result;
	}

	public List<Person> suchePersonen(String firma, String organisation, String team) {
		List<Person> result = new LinkedList<Person>();
		if (SPEZIAL_ALLE.equalsIgnoreCase(team) && SPEZIAL_ALLE.equalsIgnoreCase(organisation)
				&& SPEZIAL_ALLE.equalsIgnoreCase(firma)) {
			for (Person p : personIndex.values()) {
				result.add(p);
			}
		} else {

			try {
				DirectoryReader ireader = DirectoryReader.open(directory);
				IndexSearcher isearcher = new IndexSearcher(ireader);

				List<String> queryParts = new LinkedList<>();
				if (!SPEZIAL_ALLE.equalsIgnoreCase(firma)) {
					queryParts.add(FIELD_FIRMA + ":" + firma);
				}
				if (!SPEZIAL_ALLE.equalsIgnoreCase(organisation)) {
					queryParts.add(FIELD_ORGANISATION + ":" + organisation);
				}
				if (!SPEZIAL_ALLE.equalsIgnoreCase(team)) {
					queryParts.add(FIELD_TEAM + ":" + team);
				}

				QueryParser queryParser = new QueryParser(FIELD_NAME, analyzer);
				String special = StringUtils.join(queryParts, " AND ");
				Query query = queryParser.parse(special);

				ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;
				for (ScoreDoc doc : hits) {
					Document hitDoc = isearcher.doc(doc.doc);
					int id = Integer.parseInt(hitDoc.get(FIELD_ID));
					Person p = personIndex.get(id);
					result.add(p);
				}

			} catch (IOException | ParseException ioException) {
				// TODO take care
				ioException.printStackTrace();
			}
		}
		return result;
	}
	
	public Person getPersonById(int id){
		return personIndex.get(id);
	}
}
