package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_user_role")
@ApiModel(value = "UserRole", description = "")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Long ROLE_ADMIN = 1L;
    public static final Long ROLE_USER = 2L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("userId")
    private Long userId;
    @TableField("roleId")
    private Long roleId;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
