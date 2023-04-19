package com.yhm.universityhelper.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.PostMapper;
import com.yhm.universityhelper.dao.PostTagsMapper;
import com.yhm.universityhelper.dao.StarMapper;
import com.yhm.universityhelper.dao.VisitHistoryMapper;
import com.yhm.universityhelper.dao.wrapper.CustomPostWrapper;
import com.yhm.universityhelper.entity.po.Comment;
import com.yhm.universityhelper.entity.po.Post;
import com.yhm.universityhelper.entity.po.PostTag;
import com.yhm.universityhelper.entity.po.Star;
import com.yhm.universityhelper.service.ForumService;
import com.yhm.universityhelper.util.BeanUtils;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 论坛表，存储论坛信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-04-19
 */
@Service
public class ForumServiceImpl extends ServiceImpl<PostMapper, Post> implements ForumService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostTagsMapper postTagsMapper;

    @Autowired
    private StarMapper starMapper;

    @Autowired
    private VisitHistoryMapper visitHistoryMapper;


    @Override
    public boolean insertPost(JSONObject json) {
        final Long userId = json.getLong("userId");
        Post post = new Post();
        for (String key : json.keySet()) {
            Object value = json.get(key);
            if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = value.toString().replace(" ", "T");
                ReflectUtils.set(post, key, LocalDateTime.parse(time));
            } else if ("userId".equals(key)) {
                ReflectUtils.set(post, "userId", userId);
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.set(post, key, JsonUtils.jsonArrayToJson(tags));
                for (Object tag : tags) {
                    final PostTag postTag = new PostTag((String)tag);
                    if (!postTagsMapper.exists(new LambdaUpdateWrapper<PostTag>().eq(PostTag::getTag, postTag.getTag()))) {
                        postTagsMapper.insert(postTag);
                    }
                }
            }
        }

        post.setReleaseTime(LocalDateTime.now());
        post.setLastModifyTime(LocalDateTime.now());

        boolean result = postMapper.insert(post) > 0;
        if (!result) {
            throw new RuntimeException("新增帖子失败，事务回滚");
        }

        return true;
    }

    @Override
    public boolean deletePost(int postId) {
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
            if ("postId".equals(key) || "userId".equals(key)) {
                continue;
            }
            if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = json.get(key).toString().replace(" ", "T");
                ReflectUtils.set(post, key, LocalDateTime.parse(time));
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.set(post, key, JsonUtils.jsonArrayToJson(tags));
                for (Object tag : tags) {
                    final PostTag postTag = new PostTag((String)tag);
                    if (!postTagsMapper.exists(new LambdaUpdateWrapper<PostTag>().eq(PostTag::getTag, postTag.getTag()))) {
                        postTagsMapper.insert(postTag);
                    }
                }
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
    public LambdaQueryWrapper<Post> searchWrapper(JSONObject json) {
        CustomPostWrapper customPostWrapper = BeanUtils.getBean(CustomPostWrapper.class);

        if (ObjectUtil.isEmpty(json) || json.isEmpty()) {
            return customPostWrapper.getLambdaQueryWrapper();
        }

        final Set<String> keys = json.keySet();
        for (String key : keys) {
            Object value = json.get(key);
            if ("userRelease".equals(key) || "userTake".equals(key) || StringUtils.containsIgnoreCase(key, "id")) {
                ReflectUtils.call(customPostWrapper, key, Long.valueOf(value.toString()));
            } else if (StringUtils.containsIgnoreCase(key, "time")) {
                String time = value.toString().replace(" ", "T");
                ReflectUtils.call(customPostWrapper, key, LocalDateTime.parse(time));
            } else if ("tags".equals(key)) {
                JSONArray tags = json.getJSONArray(key);
                ReflectUtils.call(customPostWrapper, key, tags);
            } else {
                ReflectUtils.call(customPostWrapper, key, value);
            }
        }

        return customPostWrapper.getLambdaQueryWrapper();
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

        LambdaQueryWrapper<Post> wrapper = searchWrapper(searchJson);
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
        final Long userId = json.getLong("userId");
        final Long postId = json.getLong("postId");
        final Long replyCommentId = json.getLong("replyCommentId");
        
        return false;
    }

    @Override
    public boolean deleteComment(Long commentId) {
        return false;
    }

    @Override
    public boolean updateComment(JSONObject json) {
        return false;
    }

    @Override
    public Comment selectComment(Long commentId) {
        return null;
    }

    @Override
    public boolean insertReply(Comment comment, Long commentId) {
        return false;
    }

    @Override
    public boolean deleteReply(Long replyId) {
        return false;
    }

    @Override
    public boolean updateReply(Comment comment) {
        return false;
    }

    @Override
    public Comment selectReply(Long replyId) {
        return null;
    }

    @Override
    public boolean insertLike(Long id, int type) {
        return false;
    }

    @Override
    public boolean deleteLike(Long id, int type) {
        return false;
    }

    @Override
    public Long selectLike(Long id, int type) {
        return null;
    }

    @Override
    public boolean insertDislike(Long id, int type) {
        return false;
    }

    @Override
    public boolean deleteDislike(Long id, int type) {
        return false;
    }

    @Override
    public Long selectDislike(Long id, int type) {
        return null;
    }

    @Override
    public boolean insertStar(Long userId, Long postId) {
        return false;
    }

    @Override
    public boolean deleteStar(Long userId, Long postId) {
        return false;
    }

    @Override
    public List<Post> selectStar(Long userId) {
        return null;
    }

    @Override
    public boolean insertHistory(Long userId, Long postId) {
        return false;
    }

    @Override
    public boolean deleteHistory(Long userId, Long postId) {
        return false;
    }

    @Override
    public List<Post> selectHistory(Long userId) {
        return null;
    }
}
