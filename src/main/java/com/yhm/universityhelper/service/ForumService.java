package com.yhm.universityhelper.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    boolean deletePost(int postId);

    boolean updatePost(JSONObject json);

    LambdaQueryWrapper<Post> searchWrapper(JSONObject json);

    List<OrderItem> sortWrapper(JSONArray json);

    Page<Post> pageWrapper(JSONObject json);

    Page<Post> selectPost(JSONObject json);

    /*
     * 评论
     * */
    boolean insertComment(JSONObject json/*, Long postId, int type*/);

    boolean deleteComment(Long commentId);

    boolean updateComment(JSONObject json);

    Comment selectComment(Long commentId);

    /*
     * 回复评论
     * */

    boolean insertReply(Comment comment, Long commentId);

    boolean deleteReply(Long replyId);

    boolean updateReply(Comment comment);

    Comment selectReply(Long replyId);

    /*
     * 点赞
     * */
    // 1:帖子 2:评论
    boolean insertLike(Long id, int type);

    boolean deleteLike(Long id, int type);

    Long selectLike(Long id, int type);

    // like不能手动update，只能通过insert和delete

    /*
     * 点踩
     * */
    // 1:帖子 2:评论
    boolean insertDislike(Long id, int type);

    boolean deleteDislike(Long id, int type);

    Long selectDislike(Long id, int type);

    // dislike不能手动update，只能通过insert和delete

    /*
     * 收藏
     * */

    boolean insertStar(Long userId, Long postId);

    boolean deleteStar(Long userId, Long postId);

    List<Post> selectStar(Long userId);
    
    // collect不能手动update，只能通过insert和delete

    /*
     * 历史记录
     * */

    boolean insertHistory(Long userId, Long postId);

    boolean deleteHistory(Long userId, Long postId);

    List<Post> selectHistory(Long userId);

    List<Comment> selectYourComment(Long userId);
}
