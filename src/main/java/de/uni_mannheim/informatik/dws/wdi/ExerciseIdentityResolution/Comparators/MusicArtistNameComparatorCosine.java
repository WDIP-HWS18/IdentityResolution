package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import java.util.List;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Artist;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.EqualsSimilarity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;
import info.debatty.java.stringsimilarity.*;

public class MusicArtistNameComparatorCosine implements Comparator<Music, Attribute> {

	private static final long serialVersionUID = 1L;
	private Cosine sim = new Cosine();
	
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Music record1,
			Music record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
		List<Artist> s1 = record1.getArtists();
		List<Artist> s2 = record2.getArtists();
		String x = s1.toString();
		String x2 = s2.toString();
    	
    	double similarity = sim.similarity(x, x2);
    	
		if(this.comparisonLog != null){
			this.comparisonLog.setComparatorName(getClass().getName());
		
			this.comparisonLog.setRecord1Value(x);
			this.comparisonLog.setRecord2Value(x2);
    	
			this.comparisonLog.setSimilarity(Double.toString(similarity));
		}
		
		return similarity;
	}

	@Override
	public ComparatorLogger getComparisonLog() {
		return this.comparisonLog;
	}

	@Override
	public void setComparisonLog(ComparatorLogger comparatorLog) {
		this.comparisonLog = comparatorLog;
	}

}
