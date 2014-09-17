package org.eidos.kingchallenge.domain.comparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eidos.kingchallenge.domain.model.KingScore;

public class KingScoreChainedComparator  implements Comparator<KingScore> {

    private List<Comparator<KingScore>> listComparators;
	@SafeVarargs
    public KingScoreChainedComparator(Comparator<KingScore>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }
@Override
public int compare(KingScore o1, KingScore o2) {
	 for (Comparator<KingScore> comparator : listComparators) {
            int result = comparator.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
}
 

}