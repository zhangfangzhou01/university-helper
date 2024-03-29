package com.yhm.universityhelper.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.*;
import com.yhm.universityhelper.dao.wrapper.CustomPostWrapper;
import com.yhm.universityhelper.entity.po.*;
import com.yhm.universityhelper.service.ForumService;
import com.yhm.universityhelper.util.BeanUtils;
import com.yhm.universityhelper.util.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 论坛表，存储论坛信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */
@Transactional
@Service
public class ForumServiceImpl extends ServiceImpl<PostMapper, Post> implements ForumService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostTagMapper postTagMapper;

    @Autowired
    private StarMapper starMapper;

    @Autowired
    private VisitHistoryMapper visitHistoryMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean insertPost(JSONObject json) {
        final Long userId = json.getLong("userId");
        Post post = new Post();
        for (String key : json.keySet()) {
            Object value = json.get(key);
            if ("postId".equals(key) || "releaseTime".equals(key) || "lastModifyTime".equals(key) || "likeNum".equals(key) || "starNum".equals(key) || "commentNum".equals(key)) {
                continue;
            }
            if ("userId".equals(key)) {
                ReflectUtils.set(post, "userId", userId);
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.set(post, key, tags);
                Thread.startVirtualThread(() -> {
                    if (!tags.isEmpty()) {
                        postTagMapper.insertBatch(tags);
                    }
                });
            } else {
                ReflectUtils.set(post, key, value);
            }
        }

        post.setReleaseTime(LocalDateTime.now());
        post.setLastModifyTime(LocalDateTime.now());
        post.setUsername(userMapper.selectUsernameByUserId(userId));

        boolean result = postMapper.insert(post) > 0;
        if (!result) {
            throw new RuntimeException("新增帖子失败，事务回滚");
        }

        return true;
    }

    @Override
    public boolean deletePost(Long postId) {
        boolean result = postMapper.delete(new LambdaUpdateWrapper<Post>().eq(Post::getPostId, postId)) > 0;
        result &= starMapper.delete(new LambdaUpdateWrapper<Star>().eq(Star::getPostId, postId)) > 0;

        if (!result) {
            throw new RuntimeException("删除帖子失败，事务回滚");
        }

        return true;
    }

    @Override
    public boolean updatePost(JSONObject json) {
        Long postId = json.getLong("postId");
        Post post = postMapper.selectById(postId);

        for (String key : json.keySet()) {
            if ("postId".equals(key) || "userId".equals(key) || "releaseTime".equals(key) || "lastModifyTime".equals(key) || "likeNum".equals(key) || "starNum".equals(key) || "commentNum".equals(key)) {
                continue;
            }
            if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.set(post, key, tags);
                Thread.startVirtualThread(() -> {
                    if (!tags.isEmpty()) {
                        postTagMapper.insertBatch(tags);
                    }
                });
            } else {
                ReflectUtils.set(post, key, json.get(key));
            }
        }

        post.setLastModifyTime(LocalDateTime.now());
        boolean result = postMapper.updateById(post) > 0;

        if (!result) {
            throw new RuntimeException("更新帖子失败，事务回滚");
        }

        return true;
    }

    @Override
    public QueryWrapper<Post> searchWrapper(JSONObject json) {
        CustomPostWrapper customPostWrapper = BeanUtils.getBean(CustomPostWrapper.class);

        if (ObjectUtil.isEmpty(json) || json.isEmpty()) {
            return customPostWrapper.getQueryWrapper();
        }

        final Set<String> keys = json.keySet();
        for (String key : keys) {
            Object value = json.get(key);
            if ("postId".equals(key) || "userId".equals(key) || StringUtils.containsIgnoreCase(key, "id") || "likeNum".equals(key) || "commentNum".equals(key) || "starNum".equals(key)) {
                ReflectUtils.call(customPostWrapper, key, Long.valueOf(value.toString()));
            } else if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = value.toString().replace(' ', 'T');
                ReflectUtils.call(customPostWrapper, key, LocalDateTime.parse(time));
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.call(customPostWrapper, key, tags);
            } else {
                ReflectUtils.call(customPostWrapper, key, value);
            }
        }

        return customPostWrapper.getQueryWrapper();
    }

    @Override
    public List<OrderItem> sortWrapper(JSONArray sortJson) {
        List<OrderItem> orderItems = new ArrayList<>();
        if (ObjectUtil.isEmpty(sortJson) || sortJson.isEmpty()) {
            return orderItems;
        }

        for (Object obj : sortJson) {
            JSONObject jsonObject = (JSONObject)obj;
            if (ObjectUtil.isEmpty(jsonObject) || jsonObject.isEmpty()) {
                continue;
            }

            String column = jsonObject.get("column", String.class);
            Boolean asc = jsonObject.get("asc", Boolean.class);
            if (ObjectUtil.isEmpty(column) || ObjectUtil.isEmpty(asc)) {
                throw new RuntimeException("排序参数缺失，需要提供column和asc参数");
            }

            OrderItem orderItem = new OrderItem(column, asc);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    @Override
    public Page<Post> pageWrapper(JSONObject json) {
        Long current = 1L;
        Long size = -1L;

        if (ObjectUtil.isNotEmpty(json) && (!json.isEmpty())) {
            Validator.validateNotNull(json.get("current"), "分页参数缺失，需要提供current参数");
            Validator.validateNotNull(json.get("size"), "分页参数缺失，需要提供size参数");

            current = json.get("current", Long.class);
            size = json.get("size", Long.class);
        }

        return new Page<>(current, size);
    }

    @Override
    public Page<Post> selectPost(JSONObject json) {
        final JSONObject searchJson = json.get("search", JSONObject.class);
        final JSONObject pageJson = json.get("page", JSONObject.class);
        final JSONArray sortJson = json.get("sort", JSONArray.class);
        final String sortType = json.get("sortType", String.class);

        QueryWrapper<Post> wrapper = searchWrapper(searchJson);
        Page<Post> page = pageWrapper(pageJson);

        if (ObjectUtil.isNotNull(sortJson) && (!sortJson.isEmpty())) {
            Validator.validateNotNull(sortType, "排序类型不能为空");
            Validator.validateMatchRegex("^(attribute|priority)$", sortType, "排序类型不正确，只能是attribute或者priority");
        }

        if ("attribute".equals(sortType)) {
            page.addOrder(sortWrapper(sortJson));
        } else if ("priority".equals(sortType)) {
            page.addOrder(CustomPostWrapper.prioritySort());
        }

        return postMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean insertComment(JSONObject json) {
        Long replyCommentId = json.getLong("replyCommentId");

        if (ObjectUtil.isNull(replyCommentId)) {
            replyCommentId = 0L;
        }

        Comment comment = new Comment();
        for (String key : json.keySet()) {
            switch (key) {
                case "commentId":
                case "releaseTime":
                case "likeNum":
                    break;
                case "userId":
                case "postId":
                    ReflectUtils.set(comment, key, Long.valueOf(json.get(key).toString()));
                    break;
                case "replyCommentId":
                    ReflectUtils.set(comment, key, replyCommentId);
                    break;
                case null:
                default:
                    ReflectUtils.set(comment, key, json.get(key));
                    break;
            }
        }

        comment.setReleaseTime(LocalDateTime.now());
        comment.setUsername(userMapper.selectById(comment.getUserId()).getUsername());

        boolean result = commentMapper.insert(comment) > 0;
        if (!result) {
            throw new RuntimeException("插入评论失败，事务回滚");
        }

        return false;
    }

    @Override
    public Long selectPostCount(Long userId) {
        return postMapper.selectCount(new LambdaQueryWrapper<Post>().eq(Post::getUserId, userId));
    }

    @Override
    public boolean deleteComment(Long commentId) {
        return commentMapper.delete(new LambdaQueryWrapper<Comment>().eq(Comment::getCommentId, commentId)) > 0;
    }

    @Override
    public Comment selectComment(Long commentId) {
        return commentMapper.selectOne(new LambdaQueryWrapper<Comment>().eq(Comment::getCommentId, commentId));
    }

    @Override
    public Page<Comment> selectFirstClassCommentByPostId(Long postId, int current, int size) {
        return commentMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<Comment>().eq(Comment::getPostId, postId).eq(Comment::getReplyCommentId, 0));
    }

    @Override
    public Page<JSONObject> selectCommentWithThreeReplyByPostId(Long postId, int current, int size) {
        Page<Comment> commentPage = commentMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<Comment>().eq(Comment::getPostId, postId).eq(Comment::getReplyCommentId, 0));
        List<Comment> comments = commentPage.getRecords();
        List<JSONObject> commentList = new ArrayList<>();
        for (Comment comment : comments) {
            JSONObject commentJson = new JSONObject();
            commentJson.set("rootComment", comment);
            commentJson.set("replyComments", commentMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getReplyCommentId, comment.getCommentId()).last("limit 3")));
            commentList.add(commentJson);
        }
        Page<JSONObject> page = new Page<>();
        page.setRecords(commentList);
        page.setCurrent(commentPage.getCurrent());
        page.setSize(commentPage.getSize());
        page.setTotal(commentPage.getTotal());
        page.setPages(commentPage.getPages());
        return page;
    }

    @Override
    public Page<Comment> selectCommentByUserId(Long userId, int current, int size) {
        return commentMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<Comment>().eq(Comment::getUserId, userId));
    }

    @Override
    public Page<Comment> selectCommentByPostId(Long postId, int current, int size) {
        return commentMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<Comment>().eq(Comment::getPostId, postId));
    }

    @Override
    public Page<Comment> selectReplyByUserId(Long userId, int current, int size) {
        return commentMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<Comment>().in(Comment::getCommentId, commentMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getUserId, userId)).stream().map(Comment::getCommentId).collect(Collectors.toList())));
    }

    @Override
    public Page<Comment> selectReplyByCommentId(Long commentId, int current, int size) {
        return commentMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<Comment>().eq(Comment::getReplyCommentId, commentId));
    }

    @Override
    public boolean likeComment(Long commentId) {
        Comment comment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().eq(Comment::getCommentId, commentId));
        comment.setLikeNum(comment.getLikeNum() + 1);
        return commentMapper.updateById(comment) > 0;
    }

    @Override
    public boolean likePost(Long postId) {
        Post post = postMapper.selectOne(new LambdaQueryWrapper<Post>().eq(Post::getPostId, postId));
        post.setLikeNum(post.getLikeNum() + 1);
        return postMapper.updateById(post) > 0;
    }

    @Override
    public boolean viewPost(Long postId) {
        Post post = postMapper.selectOne(new LambdaQueryWrapper<Post>().eq(Post::getPostId, postId));
        post.setViewNum(post.getViewNum() + 1);
        return postMapper.updateById(post) > 0;
    }


    @Override
    public boolean insertStar(Long userId, Long postId) {
        return starMapper.insert(new Star(userId, postId)) > 0;
    }

    @Override
    public boolean deleteStar(Long postId) {
        return starMapper.delete(new LambdaQueryWrapper<Star>().eq(Star::getPostId, postId)) > 0;
    }

    @Override
    public Page<Post> selectStar(Long userId, int current, int size) {
        return postMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<Post>().in(Post::getPostId, starMapper.selectList(new LambdaQueryWrapper<Star>().eq(Star::getUserId, userId)).stream().map(Star::getPostId).collect(Collectors.toList())));
    }

    @Override
    public boolean insertHistory(Long userId, Long postId) {
        return visitHistoryMapper.insert(new VisitHistory(userId, postId)) > 0;
    }

    @Override
    public boolean deleteHistory(Long postId) {
        return visitHistoryMapper.delete(new LambdaQueryWrapper<VisitHistory>().eq(VisitHistory::getPostId, postId)) > 0;
    }

    @Override
    public Page<Post> selectHistory(Long userId, int current, int size) {
        return postMapper.selectPage(new Page<>(current, size), new LambdaQueryWrapper<Post>().in(Post::getPostId, visitHistoryMapper.selectList(new LambdaQueryWrapper<VisitHistory>().eq(VisitHistory::getUserId, userId)).stream().map(VisitHistory::getPostId).collect(Collectors.toList())));
    }

    @Override
    public List<String> selectAllPostTags() {
        return postTagMapper.selectList(null).stream().map(PostTag::getTag).collect(Collectors.toList());
    }
}
