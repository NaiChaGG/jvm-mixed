package io.github.jojoti.grpcstartersbram;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface SessionUser {

    /**
     * 获取当前会话的 scopeId
     *
     * @throws SessionIsNotCreatedException
     * @return
     */
    int getScopeId();

    /**
     * 获取用户的 uid
     *
     * @throws SessionIsNotCreatedException
     * @return
     */
    long getUid();

    /**
     * 判断匿名会话
     *
     * @return
     */
    boolean isAnonymous();

    /**
     * 用户登出
     * @throws SessionIsNotCreatedException
     */
    void logout();

    /**
     * 获取当前请求的 token
     *
     * @return
     */
    String getCurrentToken();

    /**
     * 刷新当前会话 token
     * @throws SessionIsNotCreatedException
     * @return
     */
    String refreshToken();

    /**
     * 登录
     *
     * @param uid
     * @return 返回 token
     */
    String login(long uid);

    /**
     * 不会读库
     *
     * @throws SessionIsNotCreatedException
     * @param key
     * @return
     */
    String getAttach(String key);

    /**
     * @throws SessionIsNotCreatedException
     * @param key
     * @return
     */
    default int getAttachAsInt(String key) {
        return Integer.parseInt(getAttach(key));
    }

    /**
     * @throws SessionIsNotCreatedException
     * @param key
     * @return
     */
    default long getAttachAsLong(String key) {
        return Long.parseLong(getAttach(key));
    }

    /**
     * 不会读库
     * @throws SessionIsNotCreatedException
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    <T> T getAttachJson(String key, Class<T> t);

    /**
     * 获取 缓存对象
     * @throws SessionIsNotCreatedException
     * @param key
     * @return
     */
    Object getAttachObject(String key);

    /**
     * @throws SessionIsNotCreatedException
     * @return
     */
    Map<String, Object> getAllAttach();

    /**
     * 会写库 谨慎操作
     *
     * @throws SessionIsNotCreatedException
     * @param key
     * @param val
     * @return
     */
    default SessionUser setAttachString(String key, String val) {
        return setAttachString(ImmutableMap.of(key, val));
    }

    /**
     * 会写库 谨慎操作
     *
     * @throws SessionIsNotCreatedException
     * @param jsonValues
     * @return
     */
    SessionUser setAttachString(ImmutableMap<String, String> jsonValues);

    /**
     * 会写库 谨慎操作
     *
     * @throws SessionIsNotCreatedException
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    default <T> SessionUser setAttachJson(String key, T t) {
        return setAttachJson(ImmutableMap.of(key, t));
    }

    /**
     * 会写库 谨慎操作
     *
     * @throws SessionIsNotCreatedException
     * @param jsonValues
     * @param <T>
     * @return
     */
    <T> SessionUser setAttachJson(ImmutableMap<String, T> jsonValues);

}