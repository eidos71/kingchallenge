package test.eidos.kingchanllenge;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.domain.KingSizeLimitedScore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(EasyMockRunner.class)
public class TestKingKingSizeLimitedScore extends AbstractKingTest {
		private static final int maxElems = 300;
	private  KingSizeLimitedScore<String> tKgScore;
	@Before
	public void setup() {
		int lvl=1;
		tKgScore = new KingSizeLimitedScore<String>(lvl, maxElems);
		 for (int i=0;  i<300;i++) {
			 tKgScore.add("e"+i);
		 }
	}
	@Test(expected=IllegalStateException.class)
	public void testStringsLevel1() {
		 assertThat("", 	tKgScore.getCurrentElements().get(), equalTo(maxElems));
		 assertThat("", 	tKgScore.getMaxSize(), equalTo(maxElems));
		 assertThat("", 	tKgScore.getLevelScope(), equalTo(1));
		//we remove one
		tKgScore.remove("e1");
		//we add one
		tKgScore.add("e1");
		//we die...
		tKgScore.add("BOOOUM");
		}
	@Test
	public void cleanAllElements() {
		tKgScore.clear();
		 assertThat("", 	tKgScore.getCurrentElements().get(), equalTo(0));
		 assertThat("", 	tKgScore.getMaxSize(), equalTo(maxElems));
		 assertThat("", 	tKgScore.getLevelScope(), equalTo(1));

	}
	@Test
	public void cleanRemoveCollections() {
		Set<String> c= new HashSet<String>();
		int numElemRemove=100;
		for (int i=0;  i<numElemRemove; i++) {
			c.add("e"+i);
		}
		
		tKgScore.removeAll(c);
		 assertThat("", 	tKgScore.getCurrentElements().get(), equalTo(maxElems-numElemRemove));
		 assertThat("", 	tKgScore.getMaxSize(), equalTo(maxElems));
		 assertThat("", 	tKgScore.getLevelScope(), equalTo(1));
	}
}
