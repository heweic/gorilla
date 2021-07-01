package org.myframe.gorilla.test.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class Test {

	public static void main(String[] args) {

		PooledObjectFactory<BaseObject> factory = new TestPooledObjectFactory();

		GenericObjectPool<BaseObject> objectPool = new GenericObjectPool<>(factory);

		
		
		try{
			for (int i = 0; i < 5; i++) {
				System.out.println("\n==========="+i+"===========");
				BaseObject baseObject = objectPool.borrowObject();
				System.out.println("bo:" + baseObject);
				System.out.println("池中所有在用实例数量pool.getNumActive()：" + objectPool.getNumActive());
				//if ((i % 2) == 0) {
					// 用完之后归还对象
					objectPool.returnObject(baseObject);
					System.out.println("归还对象！！！！");
				//}
			}
		}catch(Exception e){
			
		}
	}
}
