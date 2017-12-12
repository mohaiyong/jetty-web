package com.momo.redis;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 封装redis 缓存服务器服务接口 （集群）
 */
@SuppressWarnings("all")
public class BisRedisSlave {

	private static RedisTemplate redistemplate;

	public static RedisTemplate getRedistemplate() {
		return redistemplate;
	}

	public static void setRedistemplate(RedisTemplate redisTemplate) {
		BisRedisSlave.redistemplate = redisTemplate;
	}

	private static String redisCode = "utf-8";

	private BisRedisSlave() {
	}

	private static BisRedisSlave goodsRedisSlave = null;

	// 静态工厂方法
	public static BisRedisSlave getInstance() {
		if (goodsRedisSlave == null) {
			synchronized (BisRedisSlave.class) {
				if (goodsRedisSlave == null) {
					goodsRedisSlave = new BisRedisSlave();
				}
			}
		}
		return goodsRedisSlave;
	}

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public long del(final String... keys) {

		return (Long) redistemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				for (int i = 0; i < keys.length; i++) {
					result = connection.del(keys[i].getBytes());
				}
				return result;
			}
		});
	}

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public void delete(final String... keys) {
		redistemplate.delete(keys);
	}

	/**
	 * @param key
	 * @return
	 */
	public DataType type(final String key) {
		return redistemplate.type(key);
	}

	/**
	 * 添加key value 并且设置存活时间(byte)
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void set(final byte[] key, final byte[] value, final long liveTime) {
		redistemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	public Boolean setNX(final byte[] key, final byte[] value, final long liveTime) {
		return (Boolean) redistemplate.execute(new RedisCallback() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				Boolean flag = connection.setNX(key, value);
				if (flag && liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return flag;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public Boolean setNX(String key, String value, long liveTime) {
		return this.setNX(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 *            单位秒
	 */
	public void set(String key, String value, long liveTime) {
		this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * set 新增
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 *            有效时间 单位（秒）
	 */
	public void setObject(final String key, final byte[] value, final long liveTime) {
		this.set(key.getBytes(), value, liveTime);
	}

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	public byte[] get(final String key) {
		return (byte[]) redistemplate.execute(new RedisCallback() {
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
	}

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	public String getString(final String key) {
		byte[] bytes = this.get(key);
		if (bytes != null) {
			return new String(this.get(key));
		} else {
			return null;
		}
	}

	/**
	 * 根据Key
	 * 
	 * @param key
	 * @param liveTime
	 *            有效时间 单位（秒）
	 */
	public void expire(final String key, final long liveTime) {

		redistemplate.execute(new RedisCallback() {

			public Long doInRedis(RedisConnection connection) throws DataAccessException {

				if (liveTime > 0) {
					connection.expire(key.getBytes(), liveTime);
				}
				return 1L;

			}

		});

	}

	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @return
	 */
	public Set keys(String pattern) {
		return redistemplate.keys(pattern);

	}

	/**
	 * 压栈
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long push(String key, String value) {
		return redistemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 出栈
	 * 
	 * @param key
	 * @return
	 */
	public String pop(String key) {
		return (String) redistemplate.opsForList().leftPop(key);
	}

	/**
	 * 入队
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long in(String key, String value) {
		return redistemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 出队
	 * 
	 * @param key
	 * @return
	 */
	public String out(String key) {
		return (String) redistemplate.opsForList().rightPop(key);
	}

	/**
	 * 栈/队列长
	 * 
	 * @param key
	 * @return
	 */
	public Long length(String key) {
		return redistemplate.opsForList().size(key);
	}

	/**
	 * 范围检索
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> range(String key, int start, int end) {
		return redistemplate.opsForList().range(key, start, end);
	}

	/**
	 * 移除
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	public void remove(String key, long i, String value) {
		redistemplate.opsForList().remove(key, i, value);
	}

	/**
	 * 检索
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public String index(String key, long index) {
		return (String) redistemplate.opsForList().index(key, index);
	}

	/**
	 * 置值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public void set(String key, long index, String value) {
		redistemplate.opsForList().set(key, index, value);
	}

	/**
	 * 裁剪
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void trim(String key, long start, int end) {
		redistemplate.opsForList().trim(key, start, end);
	}

	/**
	 * 使用set时，检查key是否已经存在
	 * 
	 * @param key
	 * @return true 存在，false 不存在
	 */
	public boolean exists(final String key) {
		return (Boolean) redistemplate.execute(new RedisCallback() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] bkey = redistemplate.getStringSerializer().serialize(key);
				return connection.exists(bkey);
			}
		});
	}

	/**
	 * 使用push,in时，检查key是否已经存在
	 * @param key
	 * @return
	 */
	public boolean hasKey(final String key) {
		try {
			return redistemplate.hasKey(key);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 清空redis 所有数据
	 * 
	 * @return ok 清空成功
	 */
	public String flushDB() {
		return (String) redistemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * 查看redis里有多少数据 return size
	 */
	public long dbSize() {
		return (Long) redistemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	/**
	 * 检查是否连接成功
	 * 
	 * @return PONG
	 */
	public String ping() {
		return (String) redistemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.ping();
			}
		});
	}

	public void close() {
		redistemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.close();
				return null;
			}
		});
	}

}
