package com.lucloud.utils.jedis;

import com.alibaba.excel.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisFactory {  
	
	private static final Log logger = LogFactory.getLog(JedisFactory.class);

	//单例的jedis池
	private static JedisPool pool = null;

	static{
		//初始化JedisPool池数据。
		init();
	}

	//功能：初始化JedisPool池数据
	private static void init() {
		try{

			//如果还没有初始化，则进行初始化数据。
			if (pool == null) {

				JedisPoolConfig config = new JedisPoolConfig();
				//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
				//如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
				config.setMaxTotal(Integer.parseInt(JedisPropertyManager.getProperty("maxActive")));
				//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
				config.setMaxIdle(Integer.parseInt(JedisPropertyManager.getProperty("maxIdle")));
				//表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
				config.setMaxIdle(Integer.parseInt(JedisPropertyManager.getProperty("maxWait")));
				//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
				config.setTestOnBorrow(Boolean.parseBoolean(JedisPropertyManager.getProperty("testOnBorrow")));
				
				String password=JedisPropertyManager.getProperty("password");
				if(!StringUtils.isEmpty(password)){
					pool = new JedisPool(config, JedisPropertyManager.getProperty("ip"),Integer.parseInt(JedisPropertyManager.getProperty("port")),5000,password);
				}else{
					pool = new JedisPool(config, JedisPropertyManager.getProperty("ip"),Integer.parseInt(JedisPropertyManager.getProperty("port")));
				}
				
			}//end if

		}catch(Exception e){
			pool = null;
			logger.error("JedisFactory的init函数出现异常：",e);
		}
	}

	//得到一个可用的Jedis
	public static Jedis getJedis(){
		return pool.getResource();
	}

	//判断是否得到一个可用的Jedis
	public static boolean  getJedisConnect(){
		boolean msg = true;
		try {
			pool.getResource();
		} catch (Exception e) {
			msg = false;
		}
		return msg;
	}

	//返回用完的jedis
	@SuppressWarnings("deprecation")
	public static void returnJedis(Jedis jedis){
		if(jedis!=null){
			pool.returnResource(jedis);
		}
	}
  
}  