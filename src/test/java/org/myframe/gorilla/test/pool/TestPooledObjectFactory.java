package org.myframe.gorilla.test.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class TestPooledObjectFactory implements PooledObjectFactory<BaseObject> {

	/**
	 * 创建object
	 *  */
	@Override
	public PooledObject<BaseObject> makeObject() throws Exception {
		
		BaseObject baseObject = new BaseObject();
		
		
		return new DefaultPooledObject<BaseObject>(baseObject);
	}
	/**
	 * 销毁对象
	 *  */
	@Override
	public void destroyObject(PooledObject<BaseObject> p) throws Exception {
		
	}
	
	/**
	 * 验证object
	 *  */
	@Override
	public boolean validateObject(PooledObject<BaseObject> p) {
		return p.getObject().isActive();
	}
	
	/**
	 * 重新初始化实例返回池
	 *  */
	@Override
	public void activateObject(PooledObject<BaseObject> p) throws Exception {
		p.getObject().setActive(true);
	}
	
	/**
	 * 取消初始化实例返回到空闲对象池
	 *  */
	@Override
	public void passivateObject(PooledObject<BaseObject> p) throws Exception {
		p.getObject().setActive(false);
	}

}
