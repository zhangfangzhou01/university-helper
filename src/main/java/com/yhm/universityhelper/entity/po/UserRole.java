package com.yhm.universityhelper.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uh_user_role")
@ApiModel(value = "UserRole", description = "")
public class UserRole implements Serializable {

    public static final int USER_CAN_CHANGE_SELF = 2;
    public static final int USER_CAN_CHANGE_NOBODY = 3;

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("userId")
    private Long userId;

    @TableField("roleId")
    private Long roleId;

    @Version
    @TableField("version")
    private Integer version;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
