package MaQiao.MaQiaoHashArryListMap;

import MaQiao.Constants.Constants;
import MaQiao.MaQiaoHashArryListMap.Consts;
import static MaQiao.MaQiaoHashArryListMap.Consts.booleanType;

//@SuppressWarnings("unused")
public final class MQHashArrayListMap<K, V> {

	/**
	 * 链表头部开始
	 */
	private transient Entry<K, V> entryStart = null;
	/**
	 * 链表尾部
	 */
	private transient Entry<K, V> entryEnd = null;
	/**
	 * 链表长度
	 */
	private transient int entryCount = 0;
	/**
	 * 数组长度
	 */
	private transient int arrayCount = 0;

	/**
	 * 数组总长度
	 */
	private transient int arrayMaxCount = 0;

	@Override
	public String toString() {
		Entry<K, V> p = null;
		System.out.println("====================================");
		for (p = entryStart; p != null; p = p.next) {
			System.out.print("[" + p.markHash);
			System.out.print("](" + p.key);
			System.out.println(")(" + p.value + ")");
		}
		System.out.println("====================================");
		return "";
	}

	public V put(K key, V value) {
		if (key == null) return null;
		//return putForNullKey(value);
		Entry<K, V> f = null;
		try{
			changeWrite();
		}finally{
			
		}
		EntryControl<K, V> entryControl = new EntryControl<K, V>(key, value);
		if ((f = entryControl.getEntryKey(entryStart, entryEnd)) != null) {
			System.out.println("find==>"+f.markHash);
			V valueOld = f.value;
			f.value = value;
			return valueOld;
		}
		/* 未找到则 添加链表*/
		addEntry(entryControl, key, value);
		entryControl = null;
		change();
		return null;
	}

	/**
	 * 添加链表，有顺序
	 * @param key K
	 * @param value V
	 */
	final void addEntry(final EntryControl<K, V> entryControl, final K key, final V value) {
		Entry<K, V> e = new Entry<K, V>(key, value);
		/*添加链表*/
		//System.out.println("e:" + e.key);
		/*空链表时*/
		if (entryStart == null) {
			//System.out.println("first11111111111111111111111111:" + e.markHash);
			entryStart = entryEnd = e;
			return;
		}
		if(entryControl.entryDown==null){
			//System.out.println("entryEnd:" + entryEnd.markHash);
			//System.out.println("11111111111111111111111:"+toString());
			e.forward=entryEnd;
			e.next=null;
			entryEnd.next=e;
			entryEnd=e;
			//System.out.println("22222222222222222222222:"+toString());
			
		}else{
			//System.out.println("e:" + entryControl.entryDown.markHash);
			e.next=entryControl.entryDown;
			e.forward=entryControl.entryDown.forward;
			if(entryControl.entryDown.forward!=null)entryControl.entryDown.forward.next=e;
			entryControl.entryDown.forward=e;			
		}
		//Entry<K, V> f = null;
//		if (entryControl.entryMin != null) {
//			for (f = entryControl.entryMin; f != null; f = f.next) {
//				if(f.markHash>e.markHash){
//					e.next=f;
//					e.forward=f.forward;
//					f.forward.next=e;
//					f.forward=e;
//					//if(entryEnd==f)
//					break;
//				}
//			}
////			if (entryControl.entryMax != null) {
////			} else {
////
////			}
//		} else {
//			if (entryControl.entryMax != null) {
//				for (f = entryControl.entryMax; f != null; f = f.forward) {
//					if(f.markHash<e.markHash){
//						e.forward=f;
//						e.next=f.forward;
//						if(f.next!=null)f.next.forward=e;
//						f.next=e;
//						break;
//					}
//				}
//			} else {
//				System.out.println("err!");
//			}
//		}

		//		/*未发现相同hashcode值*/
		//		if (entryControl.entryMin == null) {
		//			System.out.println("entryMin(;null):" + e.key);
		//
		//		}
		//		if (entryControl.entryMax == null) {
		//			System.out.println("einsertNext(;null):" + e.key);
		//			//插入地点在此单元前面
		//			e.next = entryStart;
		//			entryStart.forward = e;
		//			entryStart = e;
		//			e.forward = null;
		//			return;
		//		}
		//		System.out.println("ee:" + e.key);
		//		if (entryControl.entryMax.markHash > e.markHash) {
		//			// e -> entryControl.entryMax -> X
		//			entryControl.entryMax.forward.next = e;
		//			e.forward = entryControl.entryMax.forward;
		//			e.next = entryControl.entryMax;
		//			entryControl.entryMax.forward = e;
		//		} else {
		//			System.out.println("eee:" + e.key);
		//			//entryControl.entryMax -> e -> X
		//			if (entryControl.entryMax.next != null) entryControl.entryMax.next.forward = e;
		//			e.next = entryControl.entryMax.next;
		//			entryControl.entryMax.next = e;
		//			e.forward = entryControl.entryMax;
		//			if (e.next == null) entryEnd = e;
		//		}

		//if (entryEnd != null) entryEnd.next = e;
		//entryEnd = e;
		entryCount++;
	}

	/**
	 * 对象操作类
	 * @author Sunjian
	 * @param <K>
	 * @param <V>
	 */
	static final class EntryControl<K, V> {
		/**
		 * 大于key的最小标记值
		 */
		Entry<K, V> entryDown = null;
		Entry<K, V> entryMin = null, entryMax = null;
		final K key;
		V value;
		/**
		 * 64位标记值
		 */
		long MarkHash64 = 0;
		long MarkHashMin64 = 0;//得到hash值左移32位，变成long型，即"XX..XX1|00..000" --相同hash值的最小值
		long MarkHashMax64 = 0;//得到hash值最大型，为"XX..XX2|00..000" --相同hash值的最大值

		EntryControl(K k, V v) {
			key = k;
			value = v;
			init();
		}

		void init() {
			if (key == null) {
				new StringIndexOutOfBoundsException("key is not null!!!");
				return;
			}
			final int hash32 = Consts.hash(key);
			MarkHashMin64 = ((long) hash32) << 32;//得到hash值左移32位，变成long型，即"XX..XX1|00..000" --相同hash值的最小值
			MarkHashMax64 = ((long) (hash32 + 1)) << 32;
			MarkHash64 = (MarkHashMin64 | (long) System.identityHashCode(key));
		}

		Entry<K, V> getEntryKey(final Entry<K, V> entryStart, final Entry<K, V> entryEnd) {
			boolean isFirst = true;
			Entry<K, V> p = null;
			/* 寻找区间 */
			/* 查到所有相同hashCode的记录，以确定链表的中间位置*/
			for (p = entryStart; p != null; p = p.next) {
				//if(p.markHash < MarkHashMin64)entryMin = p;
				if (p.markHash > MarkHashMin64 && p.markHash < MarkHashMax64) {
					//System.out.println("find!!!!" + p.markHash);
					if (isFirst && (isFirst = (!isFirst))) entryMin = p.forward;/* 记录第一个开始链表的前端 */
				}
				if (entryDown == null && p.markHash > MarkHash64) entryDown = p;
				if (p.markHash > MarkHashMax64) {
					entryMax = p;
					break;
				}
				if (p == entryEnd) break;
			}
			/* 
			 * ????????????????????????????????????????????????????????????????????????????????????????
			 * 如果出现相同hashcode 不同identityHashCode 是否进行第二次检索，不以identityHashCode为标准
			 * 以key为标准  f.key.equals(key)
			 */
			{
				System.out.println("===================================================================");
				if (entryMin == null) {
					System.out.println("["+key+"]entryMinNull!!!![" + MarkHash64+"]");
					entryMin = entryStart;
				}
				if (entryMax == null) {
					System.out.println("["+key+"]entryMaxNull!!!![" + MarkHash64+"]");
					entryMax = entryEnd;
				}
				for (p = entryMin; p!=null && p.markHash <= entryMax.markHash; p = p.next) {
					if (p.markHash > MarkHashMin64 && p.markHash < MarkHashMax64) if (p.key != null && (p.key == key || p.key.equals(key))) 
						return p;

				}
			}
			/* 
			 * ????????????????????????????????????????????????????????????????????????????????????????
			 */
			return null;
		}
	}

	/**
	 * 对象单元
	 * @author Sunjian
	 * @param <K>
	 * @param <V>
	 */
	private static final class Entry<K, V> implements java.util.Map.Entry<K, V> {
		Entry<K, V> forward;
		final K key;
		V value;
		Entry<K, V> next;
		long markHash = 0;

		/**
		 * Creates new entry.
		 */
		Entry(K k, V v) {
			forward = null;
			key = k;
			value = v;
			next = null;
			init();
		}

		void init() {
			if (key == null) return;
			markHash = Consts.mark64(key);
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			this.value = value;
			return null;
		}
	}

	/**
	 * 更改是否已经修改的状态值，保持线程安全
	 */
	private final void change() {
		while (!(Constants.UNSAFE.compareAndSwapInt(this, Consts.OffsetLockedIsChange, booleanType.True.index, booleanType.True.index)
				|| Constants.UNSAFE.compareAndSwapInt(this, Consts.OffsetLockedIsChange, booleanType.False.index, booleanType.True.index)) ){
		}
	}

	/**
	 * 更改是否已经修改的状态值，保持线程安全
	 */
	private final void unchange() {
		while (!(Constants.UNSAFE.compareAndSwapInt(this, Consts.OffsetLockedIsChange, booleanType.False.index, booleanType.False.index)
				|| Constants.UNSAFE.compareAndSwapInt(this, Consts.OffsetLockedIsChange, booleanType.True.index, booleanType.False.index))) {
		}
	}

	/**
	 * 锁定对象 读
	 */
	private final void changeRead() {
		lockedIsReadOffsetCAS(booleanType.False, booleanType.True);
	}

	/**
	 * 释放对象锁 读
	 */
	private final void changeWrite() {
		lockedIsReadOffsetCAS(booleanType.True, booleanType.False);
	}

	/**
	 * volatile int lockedIsRead 主锁的CAS，循环设置锁<br/>
	 * @param from booleanType
	 * @param to booleanType
	 */
	private final void lockedIsReadOffsetCAS(final booleanType from, final booleanType to) {
		while (!Constants.UNSAFE.compareAndSwapInt(this, Consts.OffsetLocked, from.index, to.index)) {
		}
	}

	/** 判断是否有数据发生了更改，一般只针对链表，不针对数组(插入时不判断，插入后改成真) */
	@SuppressWarnings("unused")
	private transient volatile int lockedIsChange = booleanType.False.index;
	/** 判断是否进入读状态 */
	@SuppressWarnings("unused")
	private transient volatile int locked = booleanType.False.index;

}
