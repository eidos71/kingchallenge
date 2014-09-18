package org.eidos.kingchallenge.domain.comparator;

import java.util.Comparator;

import org.eidos.kingchallenge.domain.model.KingScore;

public class KingScoreReverseUserIdComparator  implements Comparator<KingScore> { 
	 
    @Override
    public int compare(KingScore 	o1 , KingScore o2) {
   
        return  o2.getKingUserId().compareTo( o1.getKingUserId() );
    }
}
