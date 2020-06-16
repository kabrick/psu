<?php
include 'dbconfig.php';
include 'functions.php';

$id = $_GET['id'];
$reviewed_by = $_GET['psu_id'];

// fetch the news information
$sql = "SELECT title, author, text FROM psu_news WHERE id = '$id'";
$result = $conn->query($sql);
$result_assoc = $result->fetch_assoc();

// update values
$sql = "UPDATE psu_news SET approved = '1', reviewed_by = '$reviewed_by' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
	send_single_push_notification("PSU Notification", "A news post you made has been approved", $result_assoc['author']);
	send_push_notification("PSU Notification", $result_assoc['title'], 0);
	send_email_global("PSU News Alert", "A news post has been added on the PSU app with title " . $result_assoc['title'] . ". Please visit the application to view it.");
    echo "1";
} else {
    echo "0";
}

?>