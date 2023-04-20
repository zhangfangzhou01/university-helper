package com.yhm.universityhelper.controller;

import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yhm.universityhelper.entity.po.UserRole;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.ForumService;
import com.yhm.universityhelper.validation.CustomValidator;
import com.yhm.universityhelper.validation.ForumValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "论坛管理")
@RestController
@RequestMapping("/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;

    @ApiOperation(value = "发帖", notes = "发帖")
    @DynamicParameters(
            name = "InsertPostDto",
            properties = {
                    @DynamicParameter(
                            name = "title",
                            value = "标题",
                            required = true,
                            dataTypeClass = String.class,
                            example = "title"
                    ),
                    @DynamicParameter(
                            name = "content",
                            value = "内容",
                            required = true,
                            dataTypeClass = String.class,
                            example = "content"
                    ),
                    @DynamicParameter(
                            name = "tags",
                            value = "标签",
                            required = true,
                            dataTypeClass = String.class
                    )
            }
    )
    @PostMapping("/insertPost")
    public ResponseResult<Object> insertPost(@RequestBody JSONObject json) {
        ForumValidator.insertPost(json);
        CustomValidator.auth(json.getLong("userId"), UserRole.USER_CAN_CHANGE_SELF);
        return forumService.insertPost(json)
                ? ResponseResult.ok("发帖成功")
                : ResponseResult.fail("发帖失败");
    }

    @ApiOperation(value = "删除帖子", notes = "删除帖子")
    @PostMapping("/deletePost")
    public ResponseResult<Object> deletePost(@RequestParam Long userId, @RequestParam Long postId) {
        ForumValidator.deletePost(userId, postId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.deletePost(postId)
                ? ResponseResult.ok("删除帖子成功")
                : ResponseResult.fail("删除帖子失败");
    }

    @ApiOperation(value = "修改帖子", notes = "修改帖子")
    @DynamicParameters(
            name = "UpdatePostDto",
            properties = {
                    @DynamicParameter(
                            name = "title",
                            value = "标题",
                            required = true,
                            dataTypeClass = String.class,
                            example = "title"
                    ),
                    @DynamicParameter(
                            name = "content",
                            value = "内容",
                            required = true,
                            dataTypeClass = String.class,
                            example = "content"
                    ),
                    @DynamicParameter(
                            name = "tags",
                            value = "标签",
                            required = true,
                            dataTypeClass = String.class
                    )
            }
    )
    @PostMapping("/updatePost")
    public ResponseResult<Object> updatePost(@RequestBody JSONObject json) {
        ForumValidator.updatePost(json);
        CustomValidator.auth(json.getLong("userId"), UserRole.USER_CAN_CHANGE_SELF);
        return forumService.updatePost(json)
                ? ResponseResult.ok("修改帖子成功")
                : ResponseResult.fail("修改帖子失败");
    }

    @ApiOperation(value = "获取帖子列表", notes = "获取帖子列表")
    @DynamicParameters(
            name = "SelectPostDto",
            properties = {
                    @DynamicParameter(
                            name = "search",
                            value = "搜索",
                            required = true,
                            dataTypeClass = List.class,
                            example = "{\n" +
                                    "    \"postId\": 1,\n" +
                                    "    \"userId\": 1,\n" +
                                    "    \"title\": \"title\",\n" +
                                    "    \"tags\": \"tags\",\n" +
                                    "    \"releaseTime\": \"2021-05-01 00:00:00\",\n" +
                                    "    \"lastModifyTime\": \"2021-05-01 00:00:00\",\n" +
                                    "    \"content\": \"content\",\n" +
                                    "    \"likeNum\": 1,\n" +
                                    "    \"starNum\": 1,\n" +
                                    "    \"commentNum\": 1\n" +
                                    "}"
                    ),
                    @DynamicParameter(
                            name = "sortType",
                            value = "排序方式(priority or attribute)",
                            required = true,
                            dataTypeClass = String.class,
                            example = "priority"
                    ),
                    @DynamicParameter(
                            name = "sort",
                            value = "排序",
                            required = true,
                            dataTypeClass = List.class,
                            example = "[\n" +
                                    "    {\n" +
                                    "        \"column\": \"postId\",\n" +
                                    "        \"asc\": true\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "        \"column\": \"userId\",\n" +
                                    "        \"asc\": true\n" +
                                    "    }\n" +
                                    "]"
                    ),
                    @DynamicParameter(
                            name = "page",
                            value = "分页",
                            required = true,
                            dataTypeClass = List.class,
                            example = "{\n" +
                                    "    \"current\": 1,\n" +
                                    "    \"size\": 10\n" +
                                    "}"
                    )
            }
    )
    @PostMapping("/selectPost")
    public ResponseResult<Object> selectPost(@RequestBody JSONObject json) {
        return ResponseResult.ok(forumService.selectPost(json), "获取帖子列表成功");
    }

    @ApiOperation(value = "发表评论", notes = "发表评论")
    @DynamicParameters(
            name = "InsertCommentDto",
            properties = {
                    @DynamicParameter(
                            name = "userId",
                            value = "用户id",
                            required = true,
                            dataTypeClass = Long.class,
                            example = "1"
                    ),
                    @DynamicParameter(
                            name = "postId",
                            value = "帖子id",
                            required = true,
                            dataTypeClass = Long.class,
                            example = "1"
                    ),
                    @DynamicParameter(
                            name = "replyCommentId",
                            value = "回复postId对应的评论的id",
                            required = true,
                            dataTypeClass = Long.class,
                            example = "1"
                    ),
                    @DynamicParameter(
                            name = "content",
                            value = "内容",
                            required = true,
                            dataTypeClass = String.class,
                            example = "content"
                    )
            }
    )
    @PostMapping("/insertComment")
    public ResponseResult<Object> insertComment(@RequestBody JSONObject json) {
        ForumValidator.insertComment(json);
        CustomValidator.auth(json.getLong("userId"), UserRole.USER_CAN_CHANGE_SELF);
        return forumService.insertComment(json)
                ? ResponseResult.ok("发表评论成功")
                : ResponseResult.fail("发表评论失败");
    }

    @ApiOperation(value = "删除评论", notes = "删除评论")
    @PostMapping("/deleteComment")
    public ResponseResult<Object> deleteComment(@RequestParam Long userId, @RequestParam Long commentId) {
        ForumValidator.deleteComment(userId, commentId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.deleteComment(commentId)
                ? ResponseResult.ok("删除评论成功")
                : ResponseResult.fail("删除评论失败");
    }

    @ApiOperation(value = "获取评论", notes = "获取评论")
    @PostMapping("/selectComment")
    public ResponseResult<Object> selectComment(@RequestParam Long userId, @RequestParam Long postId) {
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(forumService.selectComment(postId), "获取评论成功");
    }

    @ApiOperation(value = "获取自己发表的评论", notes = "获取自己发表的评论")
    @PostMapping("/selectCommentByUserId")
    public ResponseResult<Object> selectCommentByUserId(@RequestParam Long userId, int current, int size) {
        ForumValidator.selectCommentByUserId(userId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(forumService.selectCommentByUserId(userId, current, size), "获取自己发表的评论成功");
    }

    @ApiOperation(value = "获取别人对自己发表的帖子的评论", notes = "获取别人对自己发表的帖子的评论")
    @PostMapping("/selectCommentByPostId")
    public ResponseResult<Object> selectCommentByPostId(@RequestParam Long userId, Long postId, int current, int size) {
        ForumValidator.selectCommentByPostId(userId, postId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(forumService.selectCommentByPostId(postId, current, size), "获取别人对自己发表的帖子的评论成功");
    }

    @ApiOperation(value = "获取别人对自己发表的评论的回复", notes = "获取别人对自己发表的评论的回复")
    @PostMapping("/selectReplyByUserId")
    public ResponseResult<Object> selectReplyByUserId(@RequestParam Long userId, int current, int size) {
        ForumValidator.selectReplyByUserId(userId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(forumService.selectReplyByUserId(userId, current, size), "获取别人对自己发表的评论的回复成功");
    }

    @ApiOperation(value = "点赞帖子", notes = "点赞帖子")
    @PostMapping("/likePost")
    public ResponseResult<Object> likePost(@RequestParam Long userId, @RequestParam Long postId) {
        ForumValidator.likePost(userId, postId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.likePost(postId)
                ? ResponseResult.ok("点赞帖子成功")
                : ResponseResult.fail("点赞帖子失败");
    }

    @ApiOperation(value = "点赞评论", notes = "点赞评论")
    @PostMapping("/likeComment")
    public ResponseResult<Object> likeComment(@RequestParam Long userId, @RequestParam Long commentId) {
        ForumValidator.likeComment(userId, commentId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.likeComment(commentId)
                ? ResponseResult.ok("点赞评论成功")
                : ResponseResult.fail("点赞评论失败");
    }

    @ApiOperation(value = "收藏帖子", notes = "收藏帖子")
    @PostMapping("/insertStar")
    public ResponseResult<Object> insertStar(@RequestParam Long userId, @RequestParam Long postId) {
        ForumValidator.insertStar(userId, postId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.insertStar(userId, postId)
                ? ResponseResult.ok("收藏帖子成功")
                : ResponseResult.fail("收藏帖子失败");
    }

    @ApiOperation(value = "取消收藏帖子", notes = "取消收藏帖子")
    @PostMapping("/deleteStar")
    public ResponseResult<Object> deleteStar(@RequestParam Long userId, @RequestParam Long postId) {
        ForumValidator.deleteStar(userId, postId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.deleteStar(postId)
                ? ResponseResult.ok("取消收藏帖子成功")
                : ResponseResult.fail("取消收藏帖子失败");
    }

    @ApiOperation(value = "获取收藏帖子", notes = "获取收藏帖子")
    @PostMapping("/selectStar")
    public ResponseResult<Object> selectStar(@RequestParam Long userId, int current, int size) {
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(forumService.selectStar(userId, current, size), "获取收藏帖子成功");
    }

    @ApiOperation(value = "新增历史记录", notes = "新增历史记录")
    @PostMapping("/insertHistory")
    public ResponseResult<Object> insertHistory(@RequestParam Long userId, @RequestParam Long postId) {
        ForumValidator.insertHistory(userId, postId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.insertHistory(userId, postId)
                ? ResponseResult.ok("新增历史记录成功")
                : ResponseResult.fail("新增历史记录失败");
    }

    @ApiOperation(value = "删除历史记录", notes = "删除历史记录")
    @PostMapping("/deleteHistory")
    public ResponseResult<Object> deleteHistory(@RequestParam Long userId, @RequestParam Long postId) {
        ForumValidator.deleteHistory(userId, postId);
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.deleteHistory(postId)
                ? ResponseResult.ok("删除历史记录成功")
                : ResponseResult.fail("删除历史记录失败");
    }

    @ApiOperation(value = "获取历史记录", notes = "获取历史记录")
    @PostMapping("/selectHistory")
    public ResponseResult<Object> selectHistory(@RequestParam Long userId, int current, int size) {
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return ResponseResult.ok(forumService.selectHistory(userId, current, size), "获取历史记录成功");
    }
}
