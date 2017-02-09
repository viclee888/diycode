/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-02-09 23:14:48
 *
 */

package com.gcssloop.diycode_sdk.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gcssloop.diycode_sdk.api.bean.Token;
import com.gcssloop.diycode_sdk.api.utils.Constant;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class Diycode implements DiycodeAPI {

    //--- 初始化和生命周期 -------------------------------------------------------------------------

    private static final Diycode mDiycode = new Diycode();
    private static DiycodeService mDiycodeService;

    private Diycode(){};

    public void init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mDiycodeService = retrofit.create(DiycodeService.class);
    }

    public static Diycode getInstance(){
        return mDiycode;
    }


    //--- 登录相关内容 ----------------------------------------------------------------------------

    /**
     * 登录时调用
     * 返回一个 token，用于获取各类私有信息使用，该 token 用 LoginEvent 接收。
     *
     * @param user_name 用户名
     * @param password  密码
     */
    @Override
    public void login(@NonNull String user_name, @NonNull String password) {
        Call<Token> call = mDiycodeService.getToken(Constant.VALUE_CLIENT_ID, Constant.VALUE_CLIENT_SECRET,
                Constant.VALUE_GRANT_TYPE, user_name, password);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    Log.e(TAG, "token: " + token);
                    EventBus.getDefault().post(new DiycodeEvent.LoginEvent(token));
                } else {
                    EventBus.getDefault().post(new DiycodeEvent.LoginEvent());
                    Log.e(TAG, "getToken STATUS: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                EventBus.getDefault().post(new DiycodeEvent.LoginEvent());
            }
        });

    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {

    }


    /**
     * 更新设备信息
     * 记录用户 Device 信息，用于 Push 通知。
     * 请在每次用户打开 App 的时候调用此 API 以便更新 Token 的 last_actived_at 让服务端知道这个设备还活着。
     * Push 将会忽略那些超过两周的未更新的设备。
     */
    @Override
    public void updateDevices() {

    }

    /**
     * 删除 Device 信息，请注意在用户登出或删除应用的时候调用，以便能确保清理掉。
     */
    @Override
    public void delateDevices() {

    }


    //--- 测试接口 -------------------------------------------------------------------------------

    /**
     * 简单的 API 测试接口，需要登录验证，便于快速测试 OAuth 以及其他 API 的基本格式是否正确。
     * 使用 HelloEvent 接收结果。
     *
     * @param limit 数量极限，值范围[0..100]
     */
    @Override
    public void hello(@Nullable Integer limit) {

    }


    //--- like -------------------------------------------------------------------------------------

    /**
     * 赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     */
    @Override
    public void like(@NonNull String obj_type, @NonNull Integer obj_id) {

    }

    /**
     * 取消之前的赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     */
    @Override
    public void unLike(@NonNull String obj_type, @NonNull Integer obj_id) {

    }


    //--- news -------------------------------------------------------------------------------------

    /**
     * 获取 News 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getNews(@Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit) {

    }

    /**
     * 创建 News
     *
     * @param title   News 标题
     * @param address News 链接
     * @param node_id News 节点编号
     */
    @Override
    public void createNews(@NonNull String title, @NonNull String address, @NonNull Integer node_id) {

    }

    /**
     * 获取 News 评论列表
     *
     * @param obj_id 编号
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getNewsReply(@NonNull Integer obj_id, @Nullable Integer offset, @Nullable Integer limit) {

    }

    /**
     * 获取 News 评论详情
     *
     * @param obj_id 编号
     */
    @Override
    public void getNewsReplyContent(@NonNull Integer obj_id) {

    }

    /**
     * 更新 News 评论
     *
     * @param obj_id 编号
     * @param body   详情
     */
    @Override
    public void updateNewsReply(@NonNull Integer obj_id, @NonNull String body) {

    }

    /**
     * 删除 News 评论
     *
     * @param obj_id 编号
     */
    @Override
    public void deleteNewsReply(@NonNull Integer obj_id) {

    }


    //--- topic ------------------------------------------------------------------------------------

    /**
     * 获取 Topics 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getTopics(@Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit) {

    }

    /**
     * 创建 Topic
     *
     * @param title   Topic 标题
     * @param body    Topic 内容
     * @param node_id Topic 节点编号
     */
    @Override
    public void createTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id) {

    }

    /**
     * 获取 topic 内容
     *
     * @param obj_id
     */
    @Override
    public void getTopicContent(@NonNull Integer obj_id) {

    }

    /**
     * 更新 topic
     *
     * @param title   标题
     * @param body    话题内容 Markdown 格式
     * @param node_id 节点编号
     */
    @Override
    public void updateTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id) {

    }

    /**
     * 删除 topic 仅支持删除自己创建的 topic
     *
     * @param id 编号
     */
    @Override
    public void deleteTopic(@NonNull Integer id) {

    }

    /**
     * 屏蔽话题，移到 NoPoint 节点 (Admin only)
     *
     * @param id 编号
     */
    @Override
    public void banTopic(@NonNull Integer id) {

    }

    /**
     * 关注话题
     *
     * @param id 编号
     */
    @Override
    public void followTopic(@NonNull Integer id) {

    }

    /**
     * 取消关注话题
     *
     * @param id 编号
     */
    @Override
    public void unFollowRopic(@NonNull Integer id) {

    }

    /**
     * 收藏一个话题
     *
     * @param id 编号
     */
    @Override
    public void collectionTopic(@NonNull Integer id) {

    }

    /**
     * 取消收藏一个话题
     *
     * @param id 编号
     */
    @Override
    public void unCollectionTopic(@NonNull Integer id) {

    }

    /**
     * 获取话题评论列表
     *
     * @param id     编号
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getTopicReplies(@NonNull Integer id, @NonNull Integer offset, @NonNull Integer limit) {

    }

    /**
     * 创建话题
     *
     * @param id   编号
     * @param body 内容 Markdown 格式
     */
    @Override
    public void createTopicReplies(@NonNull Integer id, @NonNull String body) {

    }


    //--- reply ------------------------------------------------------------------------------------

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id 编号
     */
    @Override
    public void getReply(@NonNull Integer id) {

    }

    /**
     * 更新回帖
     *
     * @param id   编号
     * @param body 帖子详情
     */
    @Override
    public void postReply(@NonNull Integer id, @NonNull String body) {

    }

    /**
     * 删除回帖
     *
     * @param id 编号
     */
    @Override
    public void deleteReply(@NonNull Integer id) {

    }


    //--- photo ------------------------------------------------------------------------------------

    /**
     * 上传图片,请使用 Multipart 的方式提交图片文件
     *
     * @param img_file 图片文件
     */
    @Override
    public void uploadPhoto(@NonNull File img_file) {

    }


    //--- sites ------------------------------------------------------------------------------------

    /**
     * 获取 sites 列表
     */
    @Override
    public void getSites() {

    }


    //--- user -------------------------------------------------------------------------------------

    /**
     * 获得用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getUsers(@NonNull Integer limit) {

    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     */
    @Override
    public void getUserInfo(@NonNull String username) {

    }

    /**
     * 屏蔽某个用户
     *
     * @param username 被屏蔽的用户名
     */
    @Override
    public void blockUser(@NonNull String username) {

    }

    /**
     * 取消屏蔽某个用户
     *
     * @param username 被屏蔽的用户名
     */
    @Override
    public void unBlockUser(@NonNull String username) {

    }

    /**
     * 获得被屏蔽的用户列表
     *
     * @param username 自己用户名
     */
    @Override
    public void getBlocked(@NonNull String username) {

    }

    /**
     * 关注某个用户
     *
     * @param username 关注的用户名
     */
    @Override
    public void followUser(@NonNull String username) {

    }

    /**
     * 取消关注某个用户
     *
     * @param username 取消关注的用户名
     */
    @Override
    public void unFollowUser(@NonNull String username) {

    }

    /**
     * 获取关注者列表
     *
     * @param username
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getFollowers(@NonNull String username, @NonNull Integer offset, @NonNull Integer limit) {

    }

    /**
     * 获取正在关注的列表
     *
     * @param username
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getFollowing(@NonNull String username, @NonNull Integer offset, @NonNull Integer limit) {

    }

    /**
     * 获取用户创建的回帖列表
     *
     * @param username 用户名
     * @param order    顺序，默认值 recent，取值范围 	["recent"]
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默值 20，值范围 1..150
     */
    @Override
    public void getUserReplies(@NonNull String username, @Nullable String order, @Nullable Integer offset, @Nullable String limit) {

    }

    /**
     * 获取用户相关的话题列表
     *
     * @param username 用户名
     * @param order    顺序，默认值 recent，取值范围 	["recent", "likes", "replies"]
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getUserTopics(@NonNull String username, @Nullable String order, @Nullable Integer offset, @Nullable String limit) {

    }

    /**
     * 获取用户收藏的话题列表
     *
     * @param username 用户名
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getCollection(@NonNull String username, @Nullable Integer offset, @Nullable String limit) {

    }

    /**
     * 获取当前登录者资料
     */
    @Override
    public void getMe() {

    }


    //--- notification -----------------------------------------------------------------------------

    /**
     * 获取通知列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    @Override
    public void getNotifications(@NonNull Integer offset, @NonNull Integer limit) {

    }

    /**
     * 删除用户的某条通知
     *
     * @param id
     */
    @Override
    public void deleteNotionfition(@NonNull Integer id) {

    }

    /**
     * 删除当前用户的所有通知
     */
    @Override
    public void deleteAllNotification() {

    }

    /**
     * 将某些通知标记为已读
     *
     * @param ids id集合
     */
    @Override
    public void markNotificationAsRead(ArrayList<Integer> ids) {

    }

    /**
     * 获得未读通知的数量
     */
    @Override
    public void getUnreadNotificationCount() {

    }

    //--- nodes ------------------------------------------------------------------------------------
    //--- project ----------------------------------------------------------------------------------
}
