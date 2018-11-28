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
import de.uni_mannheim.informatik.dws.winter.similarity.string.JaccardOnNGramsSimilarity;

import java.util.List;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Artist;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;

/**
 * {@link Comparator} for {@link Music}s based on the {@link Music#getArtistName()}
 * value and their {@link LevenshteinSimilarity} value.
 *
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class MusicArtistNameComparatorJaccardOnTrigrams implements Comparator<Music, Attribute> {

    private static final long serialVersionUID = 1L;
    private JaccardOnNGramsSimilarity sim = new JaccardOnNGramsSimilarity(3);

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

        double similarity = sim.calculate(x, x2);

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
