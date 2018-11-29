package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.EqualsSimilarity;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.DeviationSimilarity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;

public class MusicDateComparatorDeviationSimilarity implements Comparator<Music, Attribute> {

	private static final long serialVersionUID = 1L;
	private DeviationSimilarity sim = new DeviationSimilarity();
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
		
		double similarity = sim.calculate(x1, x2);

		if (similarity==0.5) similarity = 0;
    	
    	
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
