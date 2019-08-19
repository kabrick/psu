<?php
include 'dbconfig.php';

$news_id = $_GET['news_id'];
$psu_id = $_GET['psu_id'];
$comment = $_GET['comment'];
$timestamp = $_GET['timestamp'];

$sql = "INSERT INTO psu_news_comments (news_id, psu_id, comment, timestamp) VALUES ('$news_id','$psu_id','$comment','$timestamp')";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}
?>