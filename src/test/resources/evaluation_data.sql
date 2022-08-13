--user
insert into user(id, created_at, updated_at, address_code, email, introduction, is_registered, banned_until, no_show, point, nickname, phone_number, level, riding_start_year, profile_images, provider, provider_id)
values (10, date_sub(now(), interval 3 day), NULL, NULL, NULL, NULL, false, NULL, 0, 0, 'leader', NULL, NULL, x'ACED00057372000D6A6176612E74696D652E536572955D84BA1B2248B20C0000787077050B000007E378', NULL, NULL, NULL);
insert into user(id, created_at, updated_at, address_code, email, introduction, is_registered, banned_until, no_show, point, nickname, phone_number, level, riding_start_year, profile_images, provider, provider_id)
values (2, date_sub(now(), interval 3 day), NULL, NULL, NULL, NULL, false, NULL, 0, 0, 'memberOne', NULL, NULL, NULL, NULL, NULL, NULL);
insert into user(id, created_at, updated_at, address_code, email, introduction, is_registered, banned_until, no_show, point, nickname, phone_number, level, riding_start_year, profile_images, provider, provider_id)
values (3, date_sub(now(), interval 3 day), NULL, NULL, NULL, NULL, false, NULL, 0, 0, 'memberTwo', NULL, NULL, NULL, NULL, NULL, NULL);


--ridingpost
insert into riding_post(id, created_at, updated_at, host_id, level, address_code, lat, lng, estimated_time, fee, riding_date,
evaluation_due_date, title, max_participant_count, min_participant_count, participant_count, riding_status)
values (1, date_sub(now(), interval 3 day) , null, 10, 'MASTER', 11010, 127.65025, 35.2326, '120분', 10000, date_sub(now(), interval 2 day),
date_add(now(), interval 5 day), '자전거가 타고싶어요', 5, 2, 3, 'CLOSED');
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 1, 10);
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 1, 2);
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 1, 3);

insert into riding_post(id, created_at, updated_at, host_id, level, address_code, lat, lng, estimated_time, fee, riding_date,
evaluation_due_date, title, max_participant_count, min_participant_count, participant_count, riding_status)
values (2, date_sub(now(), interval 3 day) , null, 2, 'MASTER', 11010, 127.65025, 35.2326, '120분', 10000, date_sub(now(), interval 2 day),
date_add(now(), interval 5 day), '자전거 타실 분.', 5, 2, 2, 'CLOSED');
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 2, 2);
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 2, 10);

insert into riding_post(id, created_at, updated_at, host_id, level, address_code, lat, lng, estimated_time, fee, riding_date,
evaluation_due_date, title, max_participant_count, min_participant_count, participant_count, riding_status)
values (3, date_sub(now(), interval 3 day) , null, 2, 'MASTER', 11010, 127.65025, 35.2326, '120분', 10000, date_sub(now(), interval 2 day),
date_add(now(), interval 5 day), '자전거 타요!!!!', 5, 2, 2, 'CLOSED');
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 3, 2);
insert into riding_participant (is_evaluated, post_id, user_id) values(true, 3, 10);

insert into riding_post(id, created_at, updated_at, host_id, level, address_code, lat, lng, estimated_time, fee, riding_date,
evaluation_due_date, title, max_participant_count, min_participant_count, participant_count, riding_status)
values (4, date_sub(now(), interval 3 day) , null, 2, 'MASTER', 11010, 127.65025, 35.2326, '120분', 10000, date_add(now(), interval 2 day),
date_add(now(), interval 9 day), '자전거 타요!!!!', 5, 2, 2, 'CLOSED');
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 4, 2);
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 4, 10);

insert into riding_post(id, created_at, updated_at, host_id, level, address_code, lat, lng, estimated_time, fee, riding_date,
evaluation_due_date, title, max_participant_count, min_participant_count, participant_count, riding_status)
values (5, date_sub(now(), interval 3 day) , null, 2, 'MASTER', 11010, 127.65025, 35.2326, '120분', 10000, date_sub(now(), interval 2 day),
date_add(now(), interval 5 day), '자전거 gogo', 5, 2, 1, 'CLOSED');
insert into riding_participant (is_evaluated, post_id, user_id) values(false, 5, 2);
