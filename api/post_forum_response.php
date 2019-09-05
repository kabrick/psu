<?php
include 'dbconfig.php';

$id = $_GET['id'];
$psu_id = $_GET['psu_id'];
$comment = $_GET['comment'];
$timestamp = $_GET['timestamp'];

$sql = "INSERT INTO psu_forum_responses (topic_id, psu_id, comment, timestamp) VALUES ('$id','$psu_id','$comment','$timestamp')";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}
?>