<?php
include 'dbconfig.php';
include 'functions.php';

$id = $_GET['id'];
$reviewed_by = $_GET['psu_id'];

$sql = "SELECT title, author_id FROM psu_jobs WHERE id = '$id'";
$result = $conn->query($sql);
$result_assoc = $result->fetch_assoc();

// update values
$sql = "UPDATE psu_jobs SET approved = '1', reviewed_by = '$reviewed_by' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
	send_single_push_notification("PSU Notification", "A job post you made has been approved", $result_assoc['author_id']);
	send_push_notification("PSU Notification", $result_assoc['title'], 0);
    echo "1";
} else {
    echo "0";
}

?>