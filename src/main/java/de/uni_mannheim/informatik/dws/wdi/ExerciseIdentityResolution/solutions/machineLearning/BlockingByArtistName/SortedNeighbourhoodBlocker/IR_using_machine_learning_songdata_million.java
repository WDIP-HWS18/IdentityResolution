package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.solutions.machineLearning.BlockingByArtistName.SortedNeighbourhoodBlocker;

import java.io.File;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.*;
import org.apache.logging.log4j.Logger;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MusicBlockingKeyBySongNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.MusicBlockingKeyByArtistNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Music;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.MusicXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_using_machine_learning_songdata_million {

    /*
     * Logging Options:
     * 		default: 	level INFO	- console
     * 		trace:		level TRACE     - console
     * 		infoFile:	level INFO	- console/file
     * 		traceFile:	level TRACE	- console/file
     *
     * To set the log level to trace and write the log to winter.log and console,
     * activate the "traceFile" logger as follows:
     *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
     *
     */

    // songdata <-> million
    // Precision: -
    // Recall:-
    // F1: -
    // correspondences: -
    

    private static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main( String[] args ) throws Exception
    {
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Music, Attribute> dataSong = new HashedDataSet<>();
        new MusicXMLReader().loadFromXML(new File("data/input/songdata.xml"), "/music/music", dataSong);
        HashedDataSet<Music, Attribute> dataArtist = new HashedDataSet<>();
        new MusicXMLReader().loadFromXML(new File("data/input/million14.xml"), "/music/music", dataArtist);

        // create a matching rule
        String options[] = new String[] { "-S" };
        String modelType = "SimpleLogistic"; // use a logistic regression
        WekaMatchingRule<Music, Attribute> matchingRule = new WekaMatchingRule<>(0.5, modelType, options);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000);

        // add comparators
        matchingRule.addComparator(new MusicArtistNameComparatorCosine());
        matchingRule.addComparator(new MusicArtistNameComparatorEqualSimilarity());
        matchingRule.addComparator(new MusicArtistNameComparatorJaccard());
        matchingRule.addComparator(new MusicArtistNameComparatorJaroWinkler());
        matchingRule.addComparator(new MusicArtistNameComparatorLevenshtein());
        matchingRule.addComparator(new MusicArtistNameComparatorLowerCaseJaccard());
        matchingRule.addComparator(new MusicArtistNameComparatorTrigrams());
        matchingRule.addComparator(new MusicSongNameComparatorCosine());
        matchingRule.addComparator(new MusicSongNameComparatorEqual());
        matchingRule.addComparator(new MusicSongNameComparatorJaccard());
        matchingRule.addComparator(new MusicSongNameComparatorJaroWinkler());
        matchingRule.addComparator(new MusicSongNameComparatorLevenshtein());
        matchingRule.addComparator(new MusicSongNameComparatorLowerCaseJaccard());
        matchingRule.addComparator(new MusicSongNameComparatorTrigrams());

        // load the training set
        MatchingGoldStandard gsTraining = new MatchingGoldStandard();
        gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_songdata_million_train.csv"));

        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Music, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(dataSong, dataArtist, null, matchingRule, gsTraining);
        System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));

        // create a blocker (blocking strategy)
        SortedNeighbourhoodBlocker<Music, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MusicBlockingKeyByArtistNameGenerator(), 12);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Music, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Music, Attribute>> correspondences = engine.runIdentityResolution(
                dataSong, dataArtist, null, matchingRule,
                blocker);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/songdata_million_correspondences_machine_learning.csv"), correspondences);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(
                "data/goldstandard/gs_songdata_million_test.csv"));

        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Music, Attribute> evaluator = new MatchingEvaluator<Music, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);

        // print the evaluation result
        System.out.println("songdata <-> million");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
}
