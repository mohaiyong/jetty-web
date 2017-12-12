package com.momo.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.momo.redis.util.SerializationUtils;
import com.momo.redis.util.SpringConfigurerUtil;
import com.momo.redis.util.SpringContextUtil;

/**
 * ClassName: SharedJedisUtils 
 * @Description: ShardedJedis 工具类
 * @author JornTang
 * @date 2017年7月29日
 */
public class RedisSlave {

	private static Logger logger = LoggerFactory.getLogger(RedisSlave.class);

	private static ShardedJedisPool shardedJedisPool = SpringContextUtil.getBean(ShardedJedisPool.class);

	public static final String KEY_PREFIX = (String) SpringConfigurerUtil.getProperty("redis.keyPrefix");

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key) {
		String value = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(key)) {
				value = ShardedJedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return value;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Object getObject(String key) {
		Object value = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(getBytesKey(key))) {
				value = toObject(ShardedJedis.get(getBytesKey(key)));
				logger.debug("getObject {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObject {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return value;
	}

	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.set(key, value);
			if (cacheSeconds != 0) {
				ShardedJedis.expire(key, cacheSeconds);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("set {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObject(String key, Object value, int cacheSeconds) {
		String result = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.set(getBytesKey(key), toBytes(value));
			if (cacheSeconds != 0) {
				ShardedJedis.expire(key, cacheSeconds);
			}
			logger.debug("setObject {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObject {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<String> getList(String key) {
		List<String> value = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(key)) {
				value = ShardedJedis.lrange(key, 0, -1);
				logger.debug("getList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getList {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return value;
	}

	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key) {
		List<Object> value = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(getBytesKey(key))) {
				List<byte[]> list = ShardedJedis.lrange(getBytesKey(key), 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectList {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return value;
	}

	/**
	 * 设置List缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setList(String key, List<String> value, int cacheSeconds) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(key)) {
				ShardedJedis.del(key);
			}
			result = ShardedJedis.rpush(key, (String[]) value.toArray());
			if (cacheSeconds != 0) {
				ShardedJedis.expire(key, cacheSeconds);
			}
			logger.debug("setList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setList {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 设置List缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value, int cacheSeconds) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(getBytesKey(key))) {
				ShardedJedis.del(key);
			}
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			result = ShardedJedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
			if (cacheSeconds != 0) {
				ShardedJedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectList {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listAdd(String key, String... value) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.rpush(key, value);
			logger.debug("listAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listAdd {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listObjectAdd(String key, Object... value) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			result = ShardedJedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
			logger.debug("listObjectAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listObjectAdd {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<String> getSet(String key) {
		Set<String> value = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(key)) {
				value = ShardedJedis.smembers(key);
				logger.debug("getSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getSet {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return value;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<Object> getObjectSet(String key) {
		Set<Object> value = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(getBytesKey(key))) {
				value = Sets.newHashSet();
				Set<byte[]> set = ShardedJedis.smembers(getBytesKey(key));
				for (byte[] bs : set) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectSet {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return value;
	}

	/**
	 * 设置Set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int cacheSeconds) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(key)) {
				ShardedJedis.del(key);
			}
			result = ShardedJedis.sadd(key, (String[]) value.toArray());
			if (cacheSeconds != 0) {
				ShardedJedis.expire(key, cacheSeconds);
			}
			logger.debug("setSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSet {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 设置Set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(getBytesKey(key))) {
				ShardedJedis.del(key);
			}
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value) {
				set.add(toBytes(o));
			}
			result = ShardedJedis.sadd(getBytesKey(key), (byte[][]) set.toArray());
			if (cacheSeconds != 0) {
				ShardedJedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectSet {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long setSetAdd(String key, String... value) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.sadd(key, value);
			logger.debug("setSetAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSetAdd {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long setSetObjectAdd(String key, Object... value) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value) {
				set.add(toBytes(o));
			}
			result = ShardedJedis.rpush(getBytesKey(key), (byte[][]) set.toArray());
			logger.debug("setSetObjectAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSetObjectAdd {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, String> getMap(String key) {
		Map<String, String> value = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(key)) {
				value = ShardedJedis.hgetAll(key);
				logger.debug("getMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getMap {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return value;
	}

	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, Object> getObjectMap(String key) {
		Map<String, Object> value = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(getBytesKey(key))) {
				value = Maps.newHashMap();
				Map<byte[], byte[]> map = ShardedJedis.hgetAll(getBytesKey(key));
				for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
					value.put(new String(e.getKey()), toObject(e.getValue()));
				}
				logger.debug("getObjectMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectMap {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return value;
	}

	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setMap(String key, Map<String, String> value, int cacheSeconds) {
		String result = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(key)) {
				ShardedJedis.del(key);
			}
			result = ShardedJedis.hmset(key, value);
			if (cacheSeconds != 0) {
				ShardedJedis.expire(key, cacheSeconds);
			}
			logger.debug("setMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setMap {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
		String result = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(getBytesKey(key))) {
				ShardedJedis.del(key);
			}
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()) {
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = ShardedJedis.hmset(getBytesKey(key), (Map<byte[], byte[]>) map);
			if (cacheSeconds != 0) {
				ShardedJedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectMap {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String mapPut(String key, Map<String, String> value) {
		String result = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.hmset(key, value);
			logger.debug("mapPut {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("mapPut {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String mapObjectPut(String key, Map<String, Object> value) {
		String result = null;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()) {
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = ShardedJedis.hmset(getBytesKey(key), (Map<byte[], byte[]>) map);
			logger.debug("mapObjectPut {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("mapObjectPut {} = {}", key, value, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 移除Map缓存中的值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long mapRemove(String key, String mapKey) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.hdel(key, mapKey);
			logger.debug("mapRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapRemove {}  {}", key, mapKey, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 移除Map缓存中的值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long mapObjectRemove(String key, String mapKey) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.hdel(getBytesKey(key), getBytesKey(mapKey));
			logger.debug("mapObjectRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectRemove {}  {}", key, mapKey, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static boolean mapExists(String key, String mapKey) {
		boolean result = false;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.hexists(key, mapKey);
			logger.debug("mapExists {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapExists {}  {}", key, mapKey, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static boolean mapObjectExists(String key, String mapKey) {
		boolean result = false;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.hexists(getBytesKey(key), getBytesKey(mapKey));
			logger.debug("mapObjectExists {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectExists {}  {}", key, mapKey, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long del(String key) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(key)) {
				result = ShardedJedis.del(key);
				logger.debug("del {}", key);
			} else {
				logger.debug("del {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("del {}", key, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long delObject(String key) {
		long result = 0;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			if (ShardedJedis.exists(getBytesKey(key))) {
				result = ShardedJedis.del(getBytesKey(key));
				logger.debug("delObject {}", key);
			} else {
				logger.debug("delObject {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("delObject {}", key, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.exists(key);
			logger.debug("exists {}", key);
		} catch (Exception e) {
			logger.warn("exists {}", key, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean existsObject(String key) {
		boolean result = false;
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = getResource();
			result = ShardedJedis.exists(getBytesKey(key));
			logger.debug("existsObject {}", key);
		} catch (Exception e) {
			logger.warn("existsObject {}", key, e);
		} finally {
			returnResource(ShardedJedis);
		}
		return result;
	}

	/**
	 * 获取资源
	 * @return
	 * @throws JedisException
	 */
	public static ShardedJedis getResource() throws JedisException {
		ShardedJedis ShardedJedis = null;
		try {
			ShardedJedis = shardedJedisPool.getResource();
			//            logger.debug("getResource.", ShardedJedis);
		} catch (JedisException e) {
			logger.warn("getResource.", e);
			returnBrokenResource(ShardedJedis);
			throw e;
		}
		return ShardedJedis;
	}

	/**
	 * 归还资源
	 * @param ShardedJedis
	 * @param isBroken
	 */
	public static void returnBrokenResource(ShardedJedis ShardedJedis) {
		if (ShardedJedis != null) {
			shardedJedisPool.returnBrokenResource(ShardedJedis);
		}
	}

	/**
	 * 释放资源
	 * @param ShardedJedis
	 * @param isBroken
	 */
	public static void returnResource(ShardedJedis ShardedJedis) {
		if (ShardedJedis != null) {
			shardedJedisPool.returnResource(ShardedJedis);
		}
	}

	/**
	 * 获取byte[]类型Key
	 * @param key
	 * @return
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			String key = (String) object;
			return key.getBytes();
		} else {
			return SerializationUtils.serialize(object);
		}
	}

	/**
	 * Object转换byte[]类型
	 * @param key
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return SerializationUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * @param key
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return SerializationUtils.deserialize(bytes);
	}

}