drop table if exists userInfo;
CREATE TABLE userInfo (
  id                int           NOT NULL AUTO_INCREMENT primary key ,
  userId            int           not null comment '用户id',
  username          varchar(50)   not null comment '用户名',
  address           varchar(50)   NOT NULL comment '地址',
  education         varchar(50)   not null comment '学历',
  endTime           int           not null comment '毕业时间',
  intentionCompany  varchar(50)   not null comment '意向公司',
  intentionJob      varchar(50)   not null comment '意向岗位',
  introduce         varchar(255)  not null comment '自我介绍',
  nickname          varchar(50)   not null comment '昵称',
  school            varchar(50)   not null comment '学校',
  sex               varchar(10)   not null comment '性别',
  avatar            varchar(255)  comment '头像',
  updateTime        datetime      comment '更新时间',
  createTime        datetime      comment '创建时间'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;