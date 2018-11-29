package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.EqualsSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;

public class MusicDateComparator10Years implements Comparator<Music, Attribute> {

	private static final long serialVersionUID = 1L;
	private EqualsSimilarity<String> sim = new EqualsSimilarity<String>();
	public double y;
	public double similarity;
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Music record1,
			Music record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
    	String s1 = record1.getSongYear();
		String s2 = record2.getSongYear();
    	double x1 = Double.parseDouble(s1);
    	double x2 = Double.parseDouble(s2);
    	if(x1==x2) {
    		similarity = sim.calculate(s1, s2);
    	} else {
    		if(x1>x2) {
        		y = x1-x2;
        	} else {
        		y = x2-x1;
        	}
    	}
    	if(y>10) {
    		similarity = 0;
    	} else {
    		similarity = y/100;
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
