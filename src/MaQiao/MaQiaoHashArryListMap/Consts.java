package MaQiao.MaQiaoHashArryListMap;

import MaQiao.Constants.Constants;

/**
 * 常量池 <br/>
 * @author Sun.jian(孙.健) <br/>
 */
public final class Consts {
	/**
	 * 初始化
	 */
	public Consts() {
	}
	/**
	 * 本对象的锁对象 lockedIsChange 的地址偏移量
	 */
	static long OffsetLockedIsChange = 0L;
	/**
	 * 本对象的锁对象 lockedIsRead 的地址偏移量
	 */
	static long OffsetLocked = 0L;
	static {
		try {
			OffsetLockedIsChange = Constants.UNSAFE.objectFieldOffset(MQHashArrayListMap.class.getDeclaredField("lockedIsChange"));/*得到锁对象的偏移量*/
			OffsetLocked = Constants.UNSAFE.objectFieldOffset(MQHashArrayListMap.class.getDeclaredField("locked"));/*得到锁对象的偏移量*/
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	/**
	 * boolean的状态:<br/>
	 * False(0):假<br/>
	 * True(1):真<br/>
	 * @author Sunjian
	 */
	static enum booleanType {
		/**
		 * 假(0)
		 */
		False(0),
		/**
		 * 真(1)
		 */
		True(1);
		/**
		 * False:假(0)<br/>
		 * True:真(1)<br/>
		 */
		int index;

		/**
		 * 构造初始化
		 * @param index int
		 */
		private booleanType(final int index) {
			this.index = index;
		}
	}	
	/**
	 * 得到对象的Mark值 64位long
	 * @param obj Object
	 * @return long
	 */
	final static long mark64(final Object obj) {
		return (markHashMin64(obj) | markIdentity64(obj));
	}
	
	

	final static long markHashMin64(final Object k) {
		return (((long) hash(k)) << 32);
	}
	@Deprecated
	final static long markHashMax64(final Object k) {
		return ((long) (hash(k) + 1)) << 32;
	}
	

	final static long markIdentity64(final Object k) {
		return ((long) System.identityHashCode(k));
	}	
	@Deprecated
	final static long Hash64(final Object k) {
		return ((long) hash(k));
	}
	/**
	 * 得到对象(key)的哈希值，参照java.util.HashMap的hash(Object k)方法<br/>
	 * @param k Object
	 * @return int
	 */
	final static int hash(final Object k) {
		int h = 0;
		//        if (useAltHashing) {
		//            if (k instanceof String) {
		//                return sun.misc.Hashing.stringHash32((String) k);
		//            }
		//            h = hashSeed;
		//        }
		h ^= k.hashCode();
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}	

}
