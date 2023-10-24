drop table user;
CREATE TABLE user
(
    id              BIGINT      NOT NULL comment 'id' auto_increment,
    login_name      VARCHAR(64) NOT NULL comment '登录名',
    password        VARCHAR(64) NOT NULL comment '密码',
    mobile          VARCHAR(11) NOT NULL comment '手机',
    email           VARCHAR(64) comment '邮箱',
    avata           VARCHAR(128) comment '头像文件地址',
    admin           INT(1) comment '管理员标识 0:非管理 1:管理',
    status          INT(2) DEFAULT 1 comment '用户状态 0:禁用，1，正常，2:锁定',
    last_login_time DATETIME comment '最后登录时间',
    PRIMARY KEY (id),
    UNIQUE KEY (login_name, mobile)
);
