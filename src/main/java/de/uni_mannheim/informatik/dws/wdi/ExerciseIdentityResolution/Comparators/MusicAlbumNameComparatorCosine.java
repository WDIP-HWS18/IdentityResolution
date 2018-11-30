package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.EqualsSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;
import info.debatty.java.stringsimilarity.*;

public class MusicAlbumNameComparatorCosine implements Comparator<Music, Attribute> {

	private static final long serialVersionUID = 1L;
	private Cosine sim = new Cosine();
	
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Music record1,
			Music record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
    	String s1 = record1.getAlbumName();
		String s2 = record2.getAlbumName();

		double similarity = 0;

		try {
			similarity = sim.similarity(s1, s2);
		} catch (NullPointerException e) {
			similarity = 0;
		}
    	
    	
		if(this.comparisonLog != null){
			this.comparisonLog.setComparatorName(getClass().getName());
		
			this.comparisonLog.setRecord1Value(s1);
			this.comparisonLog.setRecord2Value(s2);
    	
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
