drop procedure if exists getRoleByEmail;
delimiter $$
create procedure getRoleByEmail(in theEmail text)
begin
	select u.email, r.name as role_name
    from user u 
    join user_role ur on u.id = ur.user_id 
    join role r on ur.role_id = r.id 
    and u.email like concat("%", theEmail,"%");
end$$
delimiter ;
