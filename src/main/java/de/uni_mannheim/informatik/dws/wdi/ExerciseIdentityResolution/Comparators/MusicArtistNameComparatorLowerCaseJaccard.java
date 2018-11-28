/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

import java.util.List;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Artist;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;

/**
 * {@link Comparator} for {@link Music}s based on the
 * {@link Music#getArtistName()} values, and their
 * {@link TokenizingJaccardSimilarity} similarity, with a lower casing
 * beforehand.
 *
 * @author Robert Meusel (robert@dwslab.de)
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class MusicArtistNameComparatorLowerCaseJaccard implements Comparator<Music, Attribute> {

    private static final long serialVersionUID = 1L;
    TokenizingJaccardSimilarity sim = new TokenizingJaccardSimilarity();

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(
            Music record1,
            Music record2,
            Correspondence<Attribute, Matchable> schemaCorrespondences) {

        // preprocessing
    	List<Artist> s1 = record1.getArtists();
		List<Artist> s2 = record2.getArtists();
		String x = s1.toString();
		String x2 = s2.toString();

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());
            this.comparisonLog.setRecord1Value(x);
            this.comparisonLog.setRecord2Value(x2);
        }

        if (x != null) {
            x = x.toLowerCase();
        } else {
            x = "";
        }

        if (x2 != null) {
            x2 = x2.toLowerCase();
        } else {
            x2 = "";
        }

        // calculate similarity
        double similarity = sim.calculate(x, x2);

        // postprocessing
        int postSimilarity = 0;
        if (similarity <= 0.3) {
            postSimilarity = 0;
        }

        postSimilarity *= similarity;

        if(this.comparisonLog != null){
            this.comparisonLog.setRecord1PreprocessedValue(x);
            this.comparisonLog.setRecord2PreprocessedValue(x2);

            this.comparisonLog.setSimilarity(Double.toString(similarity));
            this.comparisonLog.setPostprocessedSimilarity(Double.toString(postSimilarity));
        }

        return postSimilarity;
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