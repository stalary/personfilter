drop table if exists recruit;
CREATE TABLE recruit (
  id                int           NOT NULL AUTO_INCREMENT primary key ,
  companyId         int           not null comment '公司id',
  content           varchar(255)  not null comment '内容',
  hrId              int           not null,
  skillStr          text          not null comment '要求技能',
  title             varchar(255)  not null comment '标题',
  updateTime        datetime      comment '更新时间',
  createTime        datetime      comment '创建时间'
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
