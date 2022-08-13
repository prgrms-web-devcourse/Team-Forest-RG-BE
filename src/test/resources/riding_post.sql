insert into riding_post(id, created_at, updated_at, host_id, level, address_code, lat, lng, estimated_time, fee, riding_date, title, max_participant_count, min_participant_count, participant_count, riding_status)
values (1, date_sub(now(), interval 3 day) , null, 1, 'MASTER', 11010, 127.65025, 35.2326, '120분', 10000, date_sub(now(), interval 2 day) , '자전거가 타고싶어요', 5, 2, 1, 'IN_PROGRESS');

insert into riding_participant (is_evaluated, post_id, user_id) values(false, 1, 1);