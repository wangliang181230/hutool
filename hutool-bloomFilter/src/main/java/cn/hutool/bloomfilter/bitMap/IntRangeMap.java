package cn.hutool.bloomfilter.bitMap;

/**
 * 过滤器BitMap在32位机器上.这个类能发生更好的效果.一般情况下建议使用此类
 *
 * @author wangliang181230
 */
class IntRangeMap extends IntMap {
	private static final long serialVersionUID = 1L;

	private final int min;
	private final int max;


	/**
	 * 构造
	 *
	 * @param min  最小值
	 * @param size 容量
	 */
	IntRangeMap(int min, int size) {
		super(size);
		this.min = min;
		this.max = min + size - 1;
	}


	@Override
	public void add(long i) {
		if (i < min || i > max) {
			return;
		}

		i -= min;
		super.add(i);
	}

	@Override
	public boolean contains(long i) {
		if (i < min || i > max) {
			return false;
		}

		i -= min;
		return super.contains(i);
	}

	@Override
	public void remove(long i) {
		if (i < min || i > max) {
			return;
		}

		i -= min;
		super.remove(i);
	}


	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
