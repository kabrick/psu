<?php
include 'dbconfig.php';
include 'functions.php';

$id = $_GET['id'];

// fetch the news title
$sql = "SELECT title FROM psu_news WHERE id = '$id'";
$result = $conn->query($sql);
$result_assoc = $result->fetch_assoc();

// update values
$sql = "UPDATE psu_news SET approved = '1' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
	send_push_notification("PSU Notification", $result_assoc['title'], 0);
    echo "1";
} else {
    echo "0";
}

?>