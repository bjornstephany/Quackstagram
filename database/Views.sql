-- USER BEHAVIOR VIEW
CREATE VIEW SHOW_MOST_ACTIVE_USER AS
SELECT username, 
(SELECT COUNT(Post.username) FROM Post WHERE User.username = Post.username) + 
(SELECT COUNT(Comment.username) FROM Comment WHERE User.username = Comment.username) + 
(SELECT COUNT(`Like`.username) FROM `Like` WHERE User.username = `Like`.username)
AS activity_score FROM User ORDER BY activity_score DESC LIMIT 1;

-- SYSTEM ANALYTICS VIEW
CREATE VIEW LIST_USERS_WITHOUT_POSTS AS
SELECT username from User
WHERE username NOT IN (SELECT username from Post);

-- CONTENT POPULARITY VIEW ( POSTS WITH OVER 100 LIKES )
CREATE VIEW SHOW_POPULAR_POSTS AS
SELECT username, post_id, image_path, caption, COUNT(Like.username) AS like_count from Post
JOIN Like on `Like`.post_id = Post.post_id
GROUP BY `Like`.post_id, username, post_id, image_path, caption HAVING COUNT(Like.username) > 100;

-- Design at least 2 indexes to optimize the performance of these views and overall query speed. 
-- You need to document the performance of queries before and after the index application
-- to demonstrate the improvement and justify the application

-- PRE INDEX VIEW SPEEDS:
-- SHOW_MOST_ACTIVE_USER = 
-- LIST_USERS_WITHOUT_POSTS = 
-- SHOW_POPULAR_POSTS = 

-- POST INDEX VIEW SPEEDS:
-- SHOW_MOST_ACTIVE_USER = 
-- LIST_USERS_WITHOUT_POSTS = 
-- SHOW_POPULAR_POSTS = 