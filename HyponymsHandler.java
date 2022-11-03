package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wdn;
    private NGramMap ngram;
    private Map<Double, String> wordCount;
    private TimeSeries series;
    private PriorityQueue<Double> pQueue;

    public HyponymsHandler(WordNet wdn, NGramMap ngram) {

        this.wdn = wdn;
        this.ngram = ngram;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        String result = "";
        pQueue = new PriorityQueue<>(Collections.reverseOrder());
        wordCount = new HashMap<>();
        if (words.isEmpty()) {
            return "[]";
        }
        if (words.size() == 1) {
            if (k == 0) {
                result += wdn.hyponyms(words.get(0));
            } else {
                Set<String> all = wdn.hyponyms(words.get(0));
                TreeSet<String> resArr = new TreeSet<>();
                for (String word: all) {
                    double newCount = countHelper(startYear, endYear, word);
                    if (newCount != 0) {
                        wordCount.put(newCount, word);
                        pQueue.add(newCount);
                    }
                }
                for (int i = 0; i < k; i++) {
                    if (pQueue.isEmpty()) {
                        break;
                    }
                    double count = pQueue.poll();
                    resArr.add(wordCount.get(count));
                }
                result += resArr;
            }
        } else if (words.size() > 1) {
            Set<String> original = wdn.hyponyms(words.get(0));
            for (int i = 1; i < words.size(); i++) {
                Set<String> newSet = wdn.hyponyms(words.get(i));
                original.retainAll(newSet);
            }
            if (k == 0) {
                result += original;
            } else {
                TreeSet resArr = new TreeSet();
                for (String word: original) {
                    double newCount = countHelper(startYear, endYear, word);
                    if (newCount != 0) {
                        wordCount.put(newCount, word);
                        pQueue.add(newCount);
                    }
                }
                if (pQueue.isEmpty()) {
                    return "[]";
                }
                for (int i = 0; i < k; i++) {
                    double count = pQueue.poll();
                    resArr.add(wordCount.get(count));
                }
                result += resArr;
            }
        }
        return result;
    }

    private double countHelper(int start, int end, String word) {
        double count = 0;
        series = ngram.countHistory(word, start, end);
        for (Double value : series.values()) {
            count += value;
        }
        return count;
    }
}
