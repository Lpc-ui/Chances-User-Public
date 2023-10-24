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


# 测试数据
INSERT INTO user (login_name, password, mobile, email, avata, admin, status, last_login_time)
VALUES
('test_user01', 'password01', '13000000001', 'testuser01@email.com', '/path/to/avatar01.jpg', 0, 1, '2023-01-01 10:10:10'),
('test_user02', 'password02', '13000000002', 'testuser02@email.com', '/path/to/avatar02.jpg', 1, 1, '2023-01-02 11:11:11'),
('test_user03', 'password03', '13000000003', 'testuser03@email.com', '/path/to/avatar03.jpg', 0, 0, '2023-01-03 12:12:12'),
('test_user04', 'password04', '13000000004', 'testuser04@email.com', '/path/to/avatar04.jpg', 1, 2, '2023-01-04 13:13:13'),
('test_user05', 'password05', '13000000005', 'testuser05@email.com', '/path/to/avatar05.jpg', 0, 1, '2023-01-05 14:14:14'),
('test_user06', 'password06', '13000000006', 'testuser06@email.com', '/path/to/avatar06.jpg', 1, 1, '2023-01-06 15:15:15'),
('test_user07', 'password07', '13000000007', 'testuser07@email.com', '/path/to/avatar07.jpg', 0, 0, '2023-01-07 16:16:16'),
('test_user08', 'password08', '13000000008', 'testuser08@email.com', '/path/to/avatar08.jpg', 1, 2, '2023-01-08 17:17:17'),
('test_user09', 'password09', '13000000009', 'testuser09@email.com', '/path/to/avatar09.jpg', 0, 1, '2023-01-09 18:18:18'),
('test_user10', 'password10', '13000000010', 'testuser10@email.com', '/path/to/avatar10.jpg', 1, 1, '2023-01-10 19:19:19'),
('test_user11', 'password11', '13000000011', 'testuser11@email.com', '/path/to/avatar11.jpg', 0, 0, '2023-01-11 20:20:20'),
('test_user12', 'password12', '13000000012', 'testuser12@email.com', '/path/to/avatar12.jpg', 1, 2, '2023-01-12 21:21:21'),
('test_user13', 'password13', '13000000013', 'testuser13@email.com', '/path/to/avatar13.jpg', 0, 1, '2023-01-13 22:22:22'),
('test_user14', 'password14', '13000000014', 'testuser14@email.com', '/path/to/avatar14.jpg', 1, 1, '2023-01-14 23:23:23'),
('test_user15', 'password15', '13000000015', 'testuser15@email.com', '/path/to/avatar15.jpg', 0, 0, '2023-01-15 00:00:00'),
('test_user16', 'password16', '13000000016', 'testuser16@email.com', '/path/to/avatar16.jpg', 1, 2, '2023-01-16 01:01:01'),
('test_user17', 'password17', '13000000017', 'testuser17@email.com', '/path/to/avatar17.jpg', 0, 1, '2023-01-17 02:02:02'),
('test_user18', 'password18', '13000000018', 'testuser18@email.com', '/path/to/avatar18.jpg', 1, 1, '2023-01-18 03:03:03'),
('test_user19', 'password19', '13000000019', 'testuser19@email.com', '/path/to/avatar19.jpg', 0, 0, '2023-01-19 04:04:04'),
('test_user20', 'password20', '13000000020', 'testuser20@email.com', '/path/to/avatar20.jpg', 1, 2, '2023-01-20 05:05:05');
