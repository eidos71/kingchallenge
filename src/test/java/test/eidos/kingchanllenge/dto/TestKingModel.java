package test.eidos.kingchanllenge.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class TestKingModel extends EasyMock {
	@Test
	public void testScore() {
		KingScore a1 = new KingScore.Builder(300, 351234L).build();
		KingScore a2 = new KingScore.Builder(300, 2221L).build();
		assertThat(a1.getKingUserId(), greaterThan(a2.getKingUserId()));
		assertThat(a1.getKingUserId(), is(notNullValue()) );
		a1.hashCode();
	}
	@Test
	public void testUser() {
		KingUser a1 = new KingUser.Builder(314L).build();
		KingUser a2 = new KingUser.Builder(11).build();
		assertThat(a1.getKingUserId().get(), greaterThan(a2.getKingUserId().get()));
		assertThat(a1.getKingUserId(), is(notNullValue()) );
		a1.hashCode();
	
	}
}
