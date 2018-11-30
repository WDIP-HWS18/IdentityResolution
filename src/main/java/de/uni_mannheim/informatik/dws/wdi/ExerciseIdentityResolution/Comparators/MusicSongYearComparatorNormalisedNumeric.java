package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import java.util.List;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Artist;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.NormalisedNumericSimilarity;

public class MusicSongYearComparatorNormalisedNumeric implements Comparator<Music, Attribute> {

	private static final long serialVersionUID = 1L;
	private NormalisedNumericSimilarity sim = new NormalisedNumericSimilarity();

	private ComparatorLogger comparisonLog;

	@Override
	public double compare(Music record1, Music record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {

		String s1 = record1.getSongYear();
		String s2 = record2.getSongYear();

		double x = 0;
		double x2 = 0;

		try {
			x = Double.parseDouble(s1);
			x2 = Double.parseDouble(s2);
		} catch (NullPointerException e) {
			x = 0.0;
			x2 = 0.0;
		}

		double similarity = 0;

		if (x != 0.0 || x2 != 0.0) {
			similarity = sim.calculate(x, x2);
		}
			

		if (this.comparisonLog != null) {
			this.comparisonLog.setComparatorName(getClass().getName());

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
