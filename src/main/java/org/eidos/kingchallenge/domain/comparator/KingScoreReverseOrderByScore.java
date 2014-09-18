package org.eidos.kingchallenge.domain.comparator;

import java.util.Comparator;

import org.eidos.kingchallenge.domain.model.KingScore;

public class KingScoreReverseOrderByScore  implements Comparator<KingScore>{
    @Override
    public int compare(KingScore 	o1 , KingScore o2) {
    	
        return  o2.getPoints().compareTo( o1.getPoints() );
    }
}
