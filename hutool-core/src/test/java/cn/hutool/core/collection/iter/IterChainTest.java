package cn.hutool.core.collection.iter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * test for {@link IterChain}
 */
public class IterChainTest {

	@Test
	public void testAddChain() {
		final Iterator<Integer> iter1 = Arrays.asList(1, 2).iterator();
		final Iterator<Integer> iter2 = Arrays.asList(3, 4).iterator();
		IterChain<Integer> iterChain = new IterChain<>();
		Assert.assertSame(iterChain, iterChain.addChain(iter1));
		Assert.assertSame(iterChain, iterChain.addChain(iter2));
		Assert.assertEquals(2, iterChain.allIterators.size());

		iterChain = new IterChain<>(iter1, iter2);
		Assert.assertEquals(2, iterChain.allIterators.size());
	}

	@Test
	public void testHasNext() {
		final IterChain<Integer> iterChain = new IterChain<>();
		Assert.assertFalse(iterChain.hasNext());
		Assert.assertFalse(iterChain.addChain(Collections.emptyIterator()).hasNext());
		Assert.assertTrue(iterChain.addChain(Arrays.asList(3, 4).iterator()).hasNext());
	}

	@Test
	public void testNext() {
		final Iterator<Integer> iter1 = Arrays.asList(1, 2).iterator();
		final Iterator<Integer> iter2 = Arrays.asList(3, 4).iterator();
		final IterChain<Integer> iterChain = new IterChain<>();
		Assert.assertSame(iterChain, iterChain.addChain(iter1));
		Assert.assertSame(iterChain, iterChain.addChain(iter2));
		Assert.assertEquals((Integer)1, iterChain.next());
		Assert.assertEquals((Integer)2, iterChain.next());
		Assert.assertEquals((Integer)3, iterChain.next());
		Assert.assertEquals((Integer)4, iterChain.next());
	}

	@Test
	public void testRemove() {
		final IterChain<Integer> iterChain = new IterChain<>();
		iterChain.addChain(Arrays.asList(1, 2).iterator());
		Assert.assertThrows(IllegalStateException.class, iterChain::remove);
	}

	@Test
	public void testIterator() {
		final Iterator<Integer> iter1 = Arrays.asList(1, 2).iterator();
		final Iterator<Integer> iter2 = Arrays.asList(3, 4).iterator();
		final IterChain<Integer> iterChain = new IterChain<>();
		Assert.assertSame(iterChain, iterChain.addChain(iter1));
		Assert.assertSame(iterChain, iterChain.addChain(iter2));

		final Iterator<Iterator<Integer>> iterators = iterChain.iterator();
		Assert.assertSame(iter1, iterators.next());
		Assert.assertSame(iter2, iterators.next());
	}

}
