drop table if exists company;
CREATE TABLE company (
  id                int           NOT NULL AUTO_INCREMENT primary key ,
  address           varchar(255)  not null comment '公司地址',
  avatar            text  comment 'logo',
  introduce         text          not null comment '介绍',
  name              varchar(255)  not null comment '名称',
  scale             varchar(50)   not null comment '规模',
  type              varchar(50)   not null comment '类型',
  updateTime        datetime      comment '更新时间',
  createTime        datetime      comment '创建时间'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;