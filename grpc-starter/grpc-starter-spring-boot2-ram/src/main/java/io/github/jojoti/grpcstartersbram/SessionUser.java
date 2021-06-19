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
     * @return
     * @throws SessionIsNotCreatedException
     */
    int getScopeId();

    /**
     * 获取用户的 uid
     *
     * @return
     * @throws SessionIsNotCreatedException
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
     *
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
     *
     * @return
     * @throws SessionIsNotCreatedException
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
     * @param key
     * @return
     * @throws SessionIsNotCreatedException
     */
    String getAttach(String key);

    /**
     * @param key
     * @return
     * @throws SessionIsNotCreatedException
     */
    default int getAttachAsInt(String key) {
        return Integer.parseInt(getAttach(key));
    }

    /**
     * @param key
     * @return
     * @throws SessionIsNotCreatedException
     */
    default long getAttachAsLong(String key) {
        return Long.parseLong(getAttach(key));
    }

    /**
     * 不会读库
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     * @throws SessionIsNotCreatedException
     */
    <T> T getAttachJson(String key, Class<T> t);

    /**
     * 获取 缓存对象
     *
     * @param key
     * @return
     * @throws SessionIsNotCreatedException
     */
    Object getAttachObject(String key);

    /**
     * @return
     * @throws SessionIsNotCreatedException
     */
    Map<String, Object> getAllAttach();

    /**
     * 会写库 谨慎操作
     *
     * @param key
     * @param val
     * @return
     * @throws SessionIsNotCreatedException
     */
    default SessionUser setAttachString(String key, String val) {
        return setAttachString(ImmutableMap.of(key, val));
    }

    /**
     * 会写库 谨慎操作
     *
     * @param jsonValues
     * @return
     * @throws SessionIsNotCreatedException
     */
    SessionUser setAttachString(ImmutableMap<String, String> jsonValues);

    /**
     * 会写库 谨慎操作
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     * @throws SessionIsNotCreatedException
     */
    default <T> SessionUser setAttachJson(String key, T t) {
        return setAttachJson(ImmutableMap.of(key, t));
    }

    /**
     * 会写库 谨慎操作
     *
     * @param jsonValues
     * @param <T>
     * @return
     * @throws SessionIsNotCreatedException
     */
    <T> SessionUser setAttachJson(ImmutableMap<String, T> jsonValues);

}