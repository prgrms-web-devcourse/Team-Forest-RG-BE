create table rg.address_code
(
    code     int          not null
        primary key,
    area     varchar(255) null,
    district varchar(255) null
);

create table rg.attached_image
(
    id                 bigint       not null
        primary key,
    created_at         datetime(6)  null,
    updated_at         datetime(6)  null,
    original_file_name varchar(255) null,
    url                varchar(255) null
);

create table rg.bicycle
(
    id   bigint       not null
        primary key,
    name varchar(255) not null,
    constraint UK_iqn9fsw7bqr4che388je7bx4k
        unique (name)
);

create table rg.jwt_refresh_token
(
    id      bigint auto_increment
        primary key,
    exp     datetime(6) null,
    iat     datetime(6) null,
    user_id bigint      null
);

create table rg.temporary_image
(
    id                 bigint auto_increment
        primary key,
    original_file_name varchar(255) null,
    url                varchar(255) null
);

create table rg.user
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6)  null,
    updated_at        datetime(6)  null,
    email             varchar(255) null,
    introduction      varchar(255) null,
    is_registered     bit          not null,
    banned_until      date         null,
    no_show           smallint     not null,
    point             smallint     not null,
    nickname          varchar(24)  not null,
    phone_number      varchar(255) null,
    level             varchar(255) null,
    riding_start_year tinyblob     null,
    provider          varchar(255) null,
    provider_id       varchar(255) null,
    address_code      int          null,
    constraint UK_n4swgcf30j6bmtb4l4cjryuym
        unique (nickname),
    constraint FKe4ffp2q1quev7oqj6idfsfc5g
        foreign key (address_code) references rg.address_code (code)
);

create table rg.profile_image
(
    id      bigint not null
        primary key,
    user_id bigint null,
    constraint FK7c5ge678vgxydo2sepdmrj6ge
        foreign key (user_id) references rg.user (id),
    constraint FKg0k5fkmdnnhqytmq8d7biidwa
        foreign key (id) references rg.attached_image (id)
);

create table rg.riding_post
(
    id                    bigint auto_increment
        primary key,
    created_at            datetime(6)  null,
    updated_at            datetime(6)  null,
    level                 varchar(255) not null,
    lat                   double       null,
    lng                   double       null,
    estimated_time        varchar(255) not null,
    evaluation_due_date   datetime(6)  not null,
    fee                   int          null,
    riding_date           datetime(6)  not null,
    title                 varchar(255) not null,
    max_participant_count int          not null,
    min_participant_count int          not null,
    participant_count     int          null,
    riding_status         varchar(255) null,
    host_id               bigint       not null,
    address_code          int          not null,
    constraint FK8fh74nq0gsjm6xjucab1c4sl9
        foreign key (host_id) references rg.user (id),
    constraint FKmkggl019yq9sftwf15dhlgsnw
        foreign key (address_code) references rg.address_code (code)
);

create table rg.notification
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    updated_at datetime(6)  null,
    contents   varchar(255) null,
    is_read    bit          not null,
    type       int          null,
    post_id    bigint       null,
    user_id    bigint       null,
    constraint FK6eucx6qpsd6ysynv1akhpsafq
        foreign key (post_id) references rg.riding_post (id),
    constraint FKb0yvoep4h4k92ipon31wmdf7e
        foreign key (user_id) references rg.user (id)
);

create table rg.riding_condition_bicycle
(
    id         bigint auto_increment
        primary key,
    bicycle_id bigint not null,
    post_id    bigint not null,
    constraint FKca5cerf5xajlg0efacx03oxwe
        foreign key (bicycle_id) references rg.bicycle (id),
    constraint FKol9po1hpikp963xx2m59n2by5
        foreign key (post_id) references rg.riding_post (id)
);

create table rg.riding_participant
(
    id           bigint auto_increment
        primary key,
    is_evaluated bit    not null,
    post_id      bigint null,
    user_id      bigint null,
    constraint FKm9lqmcxrx2kjsy6tjwwh325m
        foreign key (post_id) references rg.riding_post (id),
    constraint FKstt5d0bqofp6xn4kh2od3d29n
        foreign key (user_id) references rg.user (id)
);

create table rg.riding_post_comment
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6)  null,
    updated_at        datetime(6)  null,
    contents          varchar(500) not null,
    author_id         bigint       not null,
    parent_comment_id bigint       null,
    riding_post_id    bigint       null,
    constraint FK1xvi9fouqk40dcdn8kj59woq
        foreign key (author_id) references rg.user (id),
    constraint FKfq92ycnsrolkeetyk4lnaxcep
        foreign key (parent_comment_id) references rg.riding_post_comment (id),
    constraint FKlbeoq5stwvp03adw76pk2g666
        foreign key (riding_post_id) references rg.riding_post (id)
);

create table rg.riding_routes
(
    post_id  bigint       not null,
    route    varchar(255) null,
    list_idx int          not null,
    primary key (post_id, list_idx),
    constraint FK66rbij957wxp1u9so7gromw5q
        foreign key (post_id) references rg.riding_post (id)
);

create table rg.riding_sub_section
(
    id      bigint auto_increment
        primary key,
    content varchar(500) null,
    title   varchar(30)  null,
    post_id bigint       null,
    constraint FK8694xesdvo8lprpcwmjxj68rl
        foreign key (post_id) references rg.riding_post (id)
);

create table rg.riding_thumbnail_image
(
    id      bigint not null
        primary key,
    post_id bigint null,
    constraint FKcbwmavw3il23tljpjgepvsmri
        foreign key (post_id) references rg.riding_post (id),
    constraint FKnulif7sysssm4uovnrdfum83u
        foreign key (id) references rg.attached_image (id)
);

create table rg.sub_image
(
    id                 bigint not null
        primary key,
    sub_information_id bigint null,
    constraint FKlgk27xgfb11ggxqufq059bstl
        foreign key (sub_information_id) references rg.riding_sub_section (id),
    constraint FKpidtf6nql5nx1lff05v45w4nt
        foreign key (id) references rg.attached_image (id)
);

create table rg.user_bicycle
(
    id         bigint auto_increment
        primary key,
    bicycle_id bigint null,
    user_id    bigint null,
    constraint FKhisxqhxkju7dr83afc8g3wdih
        foreign key (user_id) references rg.user (id),
    constraint FKkg8po6duny662rfsss056wb19
        foreign key (bicycle_id) references rg.bicycle (id)
);

