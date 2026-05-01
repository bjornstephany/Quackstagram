-- 1 procedure, 1 function and 2 triggers. At least one of the triggers must call your procedure and your function.
-- Develop at least one stored procedure and function for frequent or complex database operations.

-- Function: Count how many likes a post has
DELIMITER //
CREATE Function countLikes(p_post_id INT)
RETURNS INT 
BEGIN 
    DECLARE like_count INT;
    SELECT COUNT(*) INTO like_count
    FROM `Like`
    WHERE post_id = p_post_id;
    RETURN like_count;
END //
DELIMITER ; 

--Procedure: Adds a notification 
DELIMITER //
CREATE Procedure insertNotification(p_username VARCHAR(255), p_post_id INT)
BEGIN
    INSERT INTO Notification (username, post_id)
    VALUES (p_username, p_post_id);
END //
DELIMITER ;

-- Trigger 1: triggered after a like is added (calls procedure and function)
DELIMITER //
CREATE Trigger afterLikeInsert
AFTER INSERT ON `Like`
FOR EACH ROW
BEGIN
    DECLARE post_owner VARCHAR(255);
    DECLARE total_likes INT;
    SELECT username INTO post_owner
    FROM Post
    WHERE post_id = NEW.post_id;
    SET total_likes = countLikes(NEW.post_id); -- calls function
    CALL insertNotification(post_owner, NEW.post_id); -- calls procedure
END //
DELIMITER ;

-- Trigger 2: triggered after a follow is added
DELIMITER //
CREATE Trigger afterFollowInsert
AFTER INSERT ON Follow
FOR EACH ROW
BEGIN
    CALL insertNotification(New.followed_username, NULL); -- calls procedure
END //
DELIMITER ;

