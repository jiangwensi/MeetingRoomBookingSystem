drop procedure if exists getUserByRole;
delimiter $$
create procedure getUserByRole(in theRoleName text)
begin
	create temporary table user_role_tmp (role_name text,email text);
    
    insert into user_role_tmp(role_name,email) 
    select r.name,u.email 
	from user u 
	join user_role ur on u.id=ur.user_id 
	join role r on r.id=ur.role_id 
	and r.name = theRoleName; 

    select * from user_role_tmp;
    
    drop temporary table user_role_tmp;
end$$
delimiter ;