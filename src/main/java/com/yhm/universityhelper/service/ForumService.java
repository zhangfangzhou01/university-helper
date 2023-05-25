package com.yhm.universityhelper.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhm.universityhelper.entity.po.Comment;
import com.yhm.universityhelper.entity.po.Post;

import java.util.List;

/**
 * <p>
 * 论坛表，存储论坛信息 服务类
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */
public interface ForumService extends IService<Post> {

    /*
     * 帖子
     * */
    boolean insertPost(JSONObject json);

    boolean deletePost(Long postId);

    boolean updatePost(JSONObject json);

    QueryWrapper<Post> searchWrapper(JSONObject json);

    List<OrderItem> sortWrapper(JSONArray json);

    Page<Post> pageWrapper(JSONObject json);

    Page<Post> selectPost(JSONObject json);
    
    Long selectPostCount(Long userId);

    /*
     * 评论
     * */
    boolean insertComment(JSONObject json/*, Long postId, int type*/);

    boolean deleteComment(Long commentId);

    Comment selectComment(Long commentId);
    
    Page<Comment> selectFirstClassCommentByPostId(Long postId, int current, int size);
    
    Page<JSONObject> selectCommentWithThreeReplyByPostId(Long postId, int current, int size);

    // 看自己发表的评论
    Page<Comment> selectCommentByUserId(Long userId, int current, int size);

    // 看别人对自己的帖子的评论
    Page<Comment> selectCommentByPostId(Long postId, int current, int size);

    // 看自己收到的对自己的评论的回复
    Page<Comment> selectReplyByUserId(Long userId, int current, int size);
    
    Page<Comment> selectReplyByCommentId(Long commentId, int current, int size);
    
    /*
    * 观看
    * */
    boolean viewPost(Long postId);
    
    /*
    * 点赞
    * */

    boolean likeComment(Long commentId);

    boolean likePost(Long postId);


    /*
     * 收藏
     * */

    boolean insertStar(Long userId, Long postId);

    boolean deleteStar(Long postId);

    Page<Post> selectStar(Long userId, int current, int size);

    // collect不能手动update，只能通过insert和delete

    /*
     * 历史记录
     * */

    boolean insertHistory(Long userId, Long postId);

    boolean deleteHistory(Long postId);

    Page<Post> selectHistory(Long userId, int current, int size);
    
    List<String> selectAllPostTags();
}
