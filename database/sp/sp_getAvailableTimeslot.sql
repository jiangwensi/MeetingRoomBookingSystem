DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getAvailableTimeslot`(in theRoomPublicId text, in theDate date)
begin 
	declare roomId int default -1;
    declare theFrom time default time(date(now()));
    declare theTo time default theFrom + interval 1 hour;
    
	drop table if exists slots;
    create temporary table slots(`from` datetime, `to` datetime);
    while theFrom < time(date(now())) + interval 24 hour do
		insert into slots (`from`,`to`) 
        values(timestamp(theDate, theFrom),
        timestamp(theDate, theTo));
        set theFrom := theFrom + interval 1 hour;
        set theTo := theFrom + interval 1 hour;
    end while;
    
    select id from room where public_id = theRoomPublicId into roomId;
    
    select s.from, s.to, r.date,r.from_time,r.to_time from slots s 
    left join (select * from booking  where room_id = roomId and `date` = theDate) as r on 
    (r.from_time <= s.from and r.to_time > s.from) 
    or (r.from_time >= s.from and r.from_time < s.to)
    where r.id is null
    ;
    
    drop temporary table slots;
end$$
DELIMITER ;
