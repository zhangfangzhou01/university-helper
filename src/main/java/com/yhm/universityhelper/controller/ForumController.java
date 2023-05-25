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
                    ),
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
                    ),
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

    @PostMapping("/selectPostCount")
    public ResponseResult<Object> selectPostCountByUserId(@RequestParam Long userId) {
        return ResponseResult.ok(forumService.selectPostCount(userId), "获取帖子数量成功");
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
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.deleteComment(commentId)
                ? ResponseResult.ok("删除评论成功")
                : ResponseResult.fail("删除评论失败");
    }

    @ApiOperation(value = "根据评论id获取评论", notes = "根据评论id获取评论")
    @PostMapping("/selectComment")
    public ResponseResult<Object> selectComment(@RequestParam Long commentId) {
        return ResponseResult.ok(forumService.selectComment(commentId), "获取评论成功");
    }

    @ApiOperation(value = "获取用户发表的所有评论", notes = "获取用户发表的所有评论")
    @PostMapping("/selectCommentByUserId")
    public ResponseResult<Object> selectCommentByUserId(@RequestParam Long userId, @RequestParam int current, @RequestParam int size) {
        return ResponseResult.ok(forumService.selectCommentByUserId(userId, current, size), "获取自己发表的评论成功");
    }

    @ApiOperation(value = "获取帖子下的所有评论（NGO的样式）", notes = "获取帖子下所有层级的评论（NGO的样式，不需要手动获取每个评论下的回复，缺点是返回的数据会平铺，需要前端自行处理）")
    @PostMapping("/selectCommentByPostId")
    public ResponseResult<Object> selectCommentByPostId(@RequestParam Long postId, @RequestParam int current, @RequestParam int size) {
        return ResponseResult.ok(forumService.selectCommentByPostId(postId, current, size), "获取别人对自己发表的帖子的评论成功");
    }
    
    @ApiOperation(value = "获取帖子下的所有一级评论（知乎的样式）", notes = "只获取帖子下的所有一级评论（知乎评论区的样式，不显示回复，点击\"查看全部回复\"按钮会跳转到对应评论的回复页面，缺点是需要手动获取每个评论下的回复，用户体验较差）")
    @PostMapping("/selectFirstClassCommentByPostId")
    public ResponseResult<Object> selectFirstClassCommentByPostId(@RequestParam Long postId, @RequestParam int current, @RequestParam int size) {
        return ResponseResult.ok(forumService.selectFirstClassCommentByPostId(postId, current, size), "获取帖子下的所有一级评论成功");
    }

    @ApiOperation(value = "获取帖子下所有一级评论及附带的前三条回复（B站的样式）", notes = "获取帖子下所有一级评论及附带的前三条回复（B站评论区的样式，评论会显示前三条回复，点击\"查看全部回复\"按钮会跳转到对应评论的回复页面）")
    @PostMapping("/selectCommentWithThreeReplyByPostId")
    public ResponseResult<Object> selectCommentWithThreeReplyByPostId(@RequestParam Long postId, @RequestParam int current, @RequestParam int size) {
        return ResponseResult.ok(forumService.selectCommentWithThreeReplyByPostId(postId, current, size), "获取帖子下所有一级评论及附带的前三条回复成功");
    }

    @ApiOperation(value = "获取对某个评论的所有回复（\"查看全部回复\"的样式）", notes = "获取对某个评论的所有回复（\"查看全部回复\"的样式）")
    @PostMapping("/selectReplyByCommentId")
    public ResponseResult<Object> selectReplyByCommentId(@RequestParam Long commentId, @RequestParam int current, @RequestParam int size) {
        return ResponseResult.ok(forumService.selectReplyByCommentId(commentId, current, size), "获取对某个评论的所有回复成功");
    }
    // 获取对某个评论的所有回复
    
    @ApiOperation(value = "获取用户所有评论下的所有回复", notes = "获取用户所有评论下的所有回复")
    @PostMapping("/selectReplyByUserId")
    public ResponseResult<Object> selectReplyByUserId(@RequestParam Long userId, @RequestParam int current, @RequestParam int size) {
        return ResponseResult.ok(forumService.selectReplyByUserId(userId, current, size), "获取别人对自己发表的评论的回复成功");
    }

    @ApiOperation(value = "浏览帖子", notes = "访问这个接口会让帖子的浏览量+1")
    @PostMapping("/viewPost")
    public ResponseResult<Object> viewPost(@RequestParam Long postId) {
        return forumService.viewPost(postId)
                ? ResponseResult.ok("观看帖子成功")
                : ResponseResult.fail("观看帖子失败");
    }

    @ApiOperation(value = "点赞帖子", notes = "访问这个接口会让帖子的点赞量+1")
    @PostMapping("/likePost")
    public ResponseResult<Object> likePost(@RequestParam Long postId) {
        return forumService.likePost(postId)
                ? ResponseResult.ok("点赞帖子成功")
                : ResponseResult.fail("点赞帖子失败");
    }

    @ApiOperation(value = "点赞评论", notes = "访问这个接口会让评论的点赞量+1")
    @PostMapping("/likeComment")
    public ResponseResult<Object> likeComment(@RequestParam Long commentId) {
        return forumService.likeComment(commentId)
                ? ResponseResult.ok("点赞评论成功")
                : ResponseResult.fail("点赞评论失败");
    }

    @ApiOperation(value = "收藏帖子", notes = "收藏帖子")
    @PostMapping("/insertStar")
    public ResponseResult<Object> insertStar(@RequestParam Long userId, @RequestParam Long postId) {
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.insertStar(userId, postId)
                ? ResponseResult.ok("收藏帖子成功")
                : ResponseResult.fail("收藏帖子失败");
    }

    @ApiOperation(value = "取消收藏帖子", notes = "取消收藏帖子")
    @PostMapping("/deleteStar")
    public ResponseResult<Object> deleteStar(@RequestParam Long userId, @RequestParam Long postId) {
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
        CustomValidator.auth(userId, UserRole.USER_CAN_CHANGE_SELF);
        return forumService.insertHistory(userId, postId)
                ? ResponseResult.ok("新增历史记录成功")
                : ResponseResult.fail("新增历史记录失败");
    }

    @ApiOperation(value = "删除历史记录", notes = "删除历史记录")
    @PostMapping("/deleteHistory")
    public ResponseResult<Object> deleteHistory(@RequestParam Long userId, @RequestParam Long postId) {
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

    @ApiOperation(value = "获取所有帖子标签", notes = "获取所有帖子标签")
    @PostMapping("/selectAllPostTags")
    public ResponseResult<Object> selectAllPostTags() {
        return ResponseResult.ok(forumService.selectAllPostTags(), "获取所有帖子标签成功");
    }

}
