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
    select count(*) from room where active = true into roomSize;
    while count < dataSize do
		# set room_id
		set randRoomId := round(rand()*roomSize);
		select id from room limit 1 offset randRoomId into roomId;
        
		# set booked_by
        select count(*) from user where id in (select user_id from room_user where room_id = roomId) into userSize;
		set randUserId := round(rand()*userSize);
		select id from user limit 1 offset randUserId into userId;
        
		# set date
        select date(now()) + interval round(rand()*10) day into theDate;
        
        # set from
        select timestamp(theDate)+interval round(rand()*22) hour into theFrom;
        
        # set to
        select timestamp(theFrom) + interval round(rand()*1)+1 hour into theTo;
        
        insert into booking ( date, from_time, public_id, to_time, booked_by, room_id) values ( theDate, theFrom, UUID(), theTo, userId, roomId);
        
    set count:=count+1;
    end while;

end$$
DELIMITER ;
