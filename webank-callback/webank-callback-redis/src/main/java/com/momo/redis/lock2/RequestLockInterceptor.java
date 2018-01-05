package com.momo.redis.lock2;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.base.Joiner;
import com.momo.redis.BisRedisSlave;

/**
 * 锁切面抽象类.
 */
@Aspect
public class RequestLockInterceptor {

	private Logger logger = Logger.getLogger(RequestLockInterceptor.class);

	/**
	 * 切面.
	 */
	@Pointcut("@annotation(Lockable)")
	public void pointcut() {
	}

	/**
	 * 环绕切面.
	 * @param point 切点.
	 * @return Object
	 * @throws Throwable 异常
	 */
	@Around("pointcut()")
	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		String targetName = point.getTarget().getClass().getName();
		String methodName = point.getSignature().getName();
		Object[] arguments = point.getArgs();

		if (method != null && method.isAnnotationPresent(Lockable.class)) {
			Lockable requestLockable = method.getAnnotation(Lockable.class);
			String requestLockKey = getLockKey(method, targetName, methodName, requestLockable.key(), arguments);

			//获取分布式锁：setNX是SET if Not eXists的简写（返回true,则说明没用设置这个key， 认为你拿到了这个ID使用权，其他想设置这个ID的都返回false排队等待拿到true才执行后续逻辑）
			boolean hasRedisLock = BisRedisSlave.getInstance().setNX(requestLockKey, requestLockKey, requestLockable.expirationTime());
			logger.info("获取分布式锁返回结果：key=" + requestLockKey + "|是否已获取这个key的分布式锁(其他同key执行逻辑需等到这个key释放才能拿到锁权限执行后续代码)=" + hasRedisLock);

			if (hasRedisLock) {//假如获取到分布式锁
				try {
					return point.proceed();
				} finally {
					BisRedisSlave.getInstance().del(requestLockKey);//方法执行完释放锁
					logger.info("释放分布式锁：key=" + requestLockKey);
				}
			} else {
				if (requestLockable.maximumWaiteTime() == 0) {
					throw new RuntimeException("获取锁资源失败：" + requestLockKey);
				} else {
					long waiteTime = requestLockable.maximumWaiteTime() * 1000;
					while (waiteTime > 0) {
						long begin = System.currentTimeMillis();
						hasRedisLock = BisRedisSlave.getInstance().setNX(requestLockKey, requestLockKey, requestLockable.expirationTime());
						logger.info("重新获取分布式锁返回结果：key=" + requestLockKey + "|是否已获取这个key的分布式锁(其他同key执行逻辑需等到这个key释放才能拿到锁权限执行后续代码)="
								+ hasRedisLock);
						if (hasRedisLock) {//假如获取到分布式锁
							try {
								return point.proceed();
							} finally {
								BisRedisSlave.getInstance().del(requestLockKey);//方法执行完释放锁
								logger.info("释放分布式锁：key=" + requestLockKey);
							}
						}
						Thread.sleep(100L);
						waiteTime -= (System.currentTimeMillis() - begin);
					}
				}
			}
		}

		return point.proceed();
	}

	/**
	 * 获取锁的key: 方法名称#参数ID
	 * @desc 返回：lock.com.tsh.aps.service.webank.callback.impl.CallbackPayService.dealCallbackMsg#12580418058588160
	 */
	private String getLockKey(Method method, String targetName, String methodName, String[] keys, Object[] arguments) {
		StringBuilder sb = new StringBuilder();
		sb.append("lock.").append(targetName).append(".").append(methodName);

		if (keys != null) {
			String keyStr = Joiner.on(".").skipNulls().join(keys);
			if (!StringUtils.isBlank(keyStr)) {
				LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
				String[] parameters = discoverer.getParameterNames(method);
				ExpressionParser parser = new SpelExpressionParser();
				Expression expression = parser.parseExpression(keyStr);
				EvaluationContext context = new StandardEvaluationContext();
				int length = parameters.length;
				if (length > 0) {
					for (int i = 0; i < length; i++) {
						context.setVariable(parameters[i], arguments[i]);
					}
				}

				//获取参数ID值
				String keysValue = expression.getValue(context, String.class);
				sb.append("#").append(keysValue);
			}
		}
		return sb.toString();
	}
}
