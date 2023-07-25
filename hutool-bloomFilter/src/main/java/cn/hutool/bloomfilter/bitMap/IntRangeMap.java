package cn.hutool.bloomfilter.bitmap;

/**
 * 允许设置值范围的变种IntMap
 *
 * @author wangliang181230
 */
class IntRangeMap extends IntMap {
	private static final long serialVersionUID = 1L;

	// the range
	protected final long min;


	/**
	 * 构造
	 *
	 * @param size 容量
	 * @param min  最小值
	 */
	IntRangeMap(int size, int min) {
		super(size, min + (long) size * MACHINE32 - 1);
		this.min = min;
	}


	/**
	 * 构造
	 *
	 * @param size 容量
	 * @param min  最小值
	 * @param max  最大值
	 */
	IntRangeMap(int size, int min, int max) {
		super(size, max);
		this.min = min;
	}


	@Override
	public void doAdd(long i) {
		i -= min;
		super.doAdd(i);
	}

	@Override
	public boolean doContains(long i) {
		i -= min;
		return super.doContains(i);
	}

	@Override
	public void doRemove(long i) {
		i -= min;
		super.doRemove(i);
	}

	@Override
	public long getMin() {
		return min;
	}
}
