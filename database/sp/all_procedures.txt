DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `clearData`()
begin
SET FOREIGN_KEY_CHECKS = 0; 
truncate booking;
truncate room_image;
truncate room_user;
truncate room_admin;
truncate blocked_timeslot;
truncate room;
truncate org_admin;
truncate org;
truncate user_role;
truncate token;
truncate user;
SET FOREIGN_KEY_CHECKS = 1;
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createBooking`(in theFrom datetime, in theTo datetime,in theRoomId int, in theBookedBy int)
begin
	declare isAvail bool default false;
	declare thePublicId text default UUID();
    select count(*)=0 from booking b 
	where b.room_id = theRoomId 
	and ((b.from_time<=theFrom and theFrom<b.to_time )or( theFrom<b.from_time and b.from_time<=theTo )) for update into isAvail;
    
    if isAvail then
		begin
			insert into booking (date, from_time, to_time, public_id, booked_by, room_id) values (date(theFrom),theFrom,theTo,thePublicId,theBookedBy,theRoomId);
        end;
        select * from booking WHERE public_id = thePublicId;
    end if;
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createBookingTestData`(in dataSize int)
begin
	declare count int default 0;
    declare userSize int default 0;
    declare randUserId int default -1;
    declare userId int default -1;
    declare roomSize int default 0;
    declare roomId int default -1;
    declare randRoomId int default -1;
    declare bookedBy int default -1;
    declare theDate date;
    declare theFrom datetime;
    declare theTo datetime;
    declare countExisting int;
    declare continue handler for sqlexception begin select theDate, theFrom, theTo, userId, roomId;  end;
    select count(*) from room where active = true into roomSize;
    while count < dataSize do
		# set room_id
		set randRoomId := floor(rand()*(roomSize-1));
		select id from room limit randRoomId,1 into roomId;
		select count(*) from user where id in (select user_id from room_user where room_id = roomId) into userSize;
        
		# set booked_by
        if userSize != 0 then 
			set randUserId := floor(rand()*(userSize-1));
			select id from user where id in (select user_id from room_user where room_id = roomId) limit randUserId,1 into userId;
			
			# set date
			select date(now()) + interval round(rand()*10) day into theDate;
			
			# set from
			select timestamp(theDate)+interval round(rand()*22) hour into theFrom;
			
			# set to
			select timestamp(theFrom) + interval round(rand()*1)+1 hour into theTo;
			insert into booking ( date, from_time, public_id, to_time, booked_by, room_id) values ( theDate, theFrom, UUID(), theTo, userId, roomId);
			set count:=count+1;
        end if;
    end while;

end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createOrgTestData`(in size int)
begin
	begin
    
		drop temporary table if exists orgName;
		create temporary table orgName (n text);
		insert into orgName (n) values ("Ang Mo Kio Public Library"),("Bedok Public Library"),("Bishan Public Library"),("Bukit Batok Public Library"),("Bukit Panjang Public Library"),("Central Public Library"),("Cheng San Public Library"),("Choa Chu Kang Public Library"),("Clementi Public Library"),("Geylang East Public Library"),("Jurong West Public Library"),("Marine Parade Public Library"),("Pasir Ris Public Library"),("Queenstown Public Library"),("Sembawang Public Library"),("Serangoon Public Library"),("Sengkang Public Library"),("Toa Payoh Public Library"),("Yishun Public Library"),("ACE The Place Community Club"),("Aljunied Community Centre"),("Anchorvale Community Club"),("Ang Mo Kio Community Centre"),("Ayer Rajah Community Club"),("Bedok Community Centre"),("Bishan Community Club"),("Boon Lay Community Club"),("Braddell Heights Community Club"),("Bukit Batok Community Club"),("Bukit Batok East Community Club"),("Bukit Merah Community Centre"),("Bukit Panjang Community Club"),("Bukit Timah Community Club"),("Buona Vista Community Club"),("Cairnhill Community Club"),("Canberra Community Club"),("Changi Simei Community Club"),("Cheng San Community Club"),("Chong Pang Community Club");
	end;

	begin
		declare userIndex int;
        declare orgNameIndex int;
        declare theDesc text;
        declare theName text;
        declare orgId int;
        declare userId int;
        declare count int default 0;
        declare continue handler for sqlexception begin end;
		select ifnull(max(id),0) from org into orgId;
        l:loop
			if count > size then
				leave l;
            end if;
            
			set orgId:=orgId + 1;
			select round(count(*)*rand()) from user into userIndex;
			select round(count(*)*rand()) from orgName into orgNameIndex;
			select n from orgName limit orgNameIndex,1 into theName;
            select id from user limit userIndex,1 into userId;
			select concat(n," Description") from orgName limit orgNameIndex,1 into theDesc;
			insert into org (id,active,description,name,public_id) values(orgId, 1, theDesc,theName,UUID());
			insert into org_admin(org_id,user_id) values(orgId,userId);
            set count := count + 1;
        end loop l;
        
	end;
    
	begin
		drop temporary table orgName;
	end;
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createRoomTestData`(in size int)
begin
	begin
		drop temporary table if exists roomName;
		create temporary table roomName (n text);
		insert into roomName (n) values ("Meeting Room"),("Activity Room"),("Function Room"),("Discussion Cornor"),("Interview Room"),("Auditorium"),("Tutorial Room");
		
		drop temporary table if exists locationName;
        create temporary table locationName (n text);
		insert into locationName (n) values ("Level 1"),("Level 2"),("Level 3"),("Level 4"),("Level 5");
	end;

	begin
		declare userIndex1 int;
		declare userIndex2 int;
		declare userIndex3 int;
		declare userIndex4 int;
		declare adminIndex int;
        
        declare user1 int;
        declare user2 int;
        declare user3 int;
        declare user4 int;
        declare theAdmin int;
        
        declare roomNameIndex int;
        declare locNameIndex int;
        declare orgIndex int;
        
        declare roomId int;
        declare orgId int;
        declare theDesc text;
        declare theName text;
        declare theLocName text;
        declare theRoomName text;
        declare count int default -1;
		select ifnull(max(id),0) from room into roomId;
        l:loop
            set count:=count+1;
			if count>size then leave l; end if;
			begin
			declare exit handler for SQLEXCEPTION begin rollback; end;
			set roomId :=roomId + 1;
			select floor((count(*)-1)*rand()) from roomName into roomNameIndex;
			select floor((count(*)-1)*rand()) from locationName into locNameIndex;
			select floor((count(*)-1)*rand()) from org into orgIndex;
			select n from roomName limit roomNameIndex,1 into theRoomName;
			select n from locationName limit locNameIndex,1 into theLocName;
            select concat(theLocName," ",theRoomName) into theName;
            
			select id from org limit orgIndex,1 into orgId;
            
			select concat(n," Description") from roomName limit roomNameIndex,1 into theDesc;
			insert into room (id,`active`,capacity,`description`, facilities,`name`,public_id,org_id) 
            values(roomId, 1, round(rand()*10)+2, theDesc,"chairs, tables, whiteboards, projectors",theName,UUID(),orgId);
            
			select floor((count(*)-1)*rand()) from user into adminIndex;
			select id from user limit adminIndex,1 into theAdmin;
            insert into room_admin(room_id,user_id)values(roomId,theAdmin);
            
			select floor((count(*)-1)*rand()) from user into userIndex1;
			select id from user limit userIndex1,1 into user1;
            select floor((count(*)-1)*rand()) from user into userIndex2;
			select id from user limit userIndex2,1 into user2;
			select floor((count(*)-1)*rand()) from user into userIndex3;
			select id from user limit userIndex3,1 into user3;
			select floor((count(*)-1)*rand()) from user into userIndex4;
			select id from user limit userIndex4,1 into user4;
            insert into room_user(room_id,user_id)values(roomId,user1),(roomId,user2),(roomId,user3),(roomId,user4);
            
            end;
        end loop l;
	end;
    
	begin
		drop temporary table roomName;
		drop temporary table locationName;
	end;
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createTestData`(in userSize int, in orgSize int, in roomSize int, in bookingSize int)
begin 
	call createUserTestData(userSize);
    select concat("finished createUserTestData:",userSize,"record");
    call createOrgTestData(orgSize);
    select concat("finished createOrgTestData:",orgSize,"record");
    call createRoomTestData(roomSize);
    select concat("finished createRoomTestData:",roomSize,"record");
    call createBookingTestData(bookingSize);
    select concat("finished createBookingTestData:",bookingSize,"record");
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `createUserTestData`(in size int)
begin
#password 123
	declare password text default "$2a$10$ZgW/I03WqkrnsgkbCEsfZO2y5.lkUZuUyYT5NXWm5Xd.Ia7vibeW.";
    declare base text default "abcdefghijklmnopqrstuvwxyz1234567890";
    declare email text default '';
    declare theName text default '';
    declare count int default 1;
    declare userId int default  0;
			declare continue handler for sqlexception begin select userId,email,theName; end;
	select ifnull(max(id),0) from `user` into userId;
    l:loop
		if count > size then 
			leave l;
        end if;
        set userId:=userId+1;
        set theName:= concat(
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1),
			substring(base,round(rand()*length(base)),1)
		);
        set email = concat(theName,'@abctestabc.com');
         if userId is null then 
 			select userId,email,theName,password;
         end if;
         begin
			declare continue handler for sqlexception
            begin 
            end;
			
			insert into user (id,active,email,email_verified,name,password,public_id) values (userId,1,email,1,theName,password,UUID());
			insert into user_role (user_id,role_id) values (userId,1);
		end;
        set count := count +1;
    end loop l;
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteUser`(in theEmail text)
begin
	declare userId int default -1;
	select id from user where email = theEmail into userId;
    if userId is null then
		select concat( "email ",email," doesn't exists");
	else 
		select concat("selected user id is ",userId);
    end if;
    delete from user_role where user_id = userId;
    select concat("deleted ",ROW_COUNT()," rows");
    delete from token where user_id = userId;
    select concat("deleted ",ROW_COUNT()," rows");
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getAvailableTimeslot`(in theRoomPublicId text, in theDate date)
begin 
	declare roomId int default -1;
    declare theFrom time default time(date(now()));
    declare theTo time default theFrom + interval 1 hour;
    declare nowPlus24 time default time(date(now())) + interval 24 hour;
    
	drop table if exists slots;
    create temporary table slots(`from` datetime, `to` datetime, index 	slots_from_index (`from`), index slots_to_index (`to`));
    while theFrom < nowPlus24 do
		insert into slots (`from`,`to`) 
        values(timestamp(theDate, theFrom),
        timestamp(theDate, theTo));
        set theFrom := theFrom + interval 1 hour;
        set theTo := theFrom + interval 1 hour;
    end while;
    
    select id from room where public_id = theRoomPublicId into roomId;
    
    select s.from, s.to from slots s 
    left join (select * from booking where room_id = roomId and `date` = theDate) as r on 
    (r.from_time <= s.from and r.to_time > s.from) 
    or (r.from_time >= s.from and r.from_time < s.to)
    where r.id is null
    ;
    
    drop temporary table slots;
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getRoleByEmail`(in theEmail text)
begin
	select u.email, r.name as role_name
    from user u 
    join user_role ur on u.id = ur.user_id 
    join role r on ur.role_id = r.id 
    and u.email like concat("%", theEmail,"%");
end$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUserByRole`(in theRoleName text)
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
DELIMITER ;
