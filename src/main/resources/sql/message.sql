drop table if exists message;
CREATE TABLE message (
  id                int           NOT NULL AUTO_INCREMENT primary key ,
  content           varchar(255)  not null comment '内容',
  fromId            int           not null comment '发送方id',
  toId              int           not null comment '接收方id',
  readState         tinyint(1)    not null default '0' comment '是否已读',
  title             varchar(255)  not null comment '标题',
  updateTime        datetime      comment '更新时间',
  createTime        datetime      comment '创建时间'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;