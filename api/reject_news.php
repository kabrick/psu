<?php
include 'dbconfig.php';
include 'functions.php';

$id = $_GET['id'];
$reviewed_by = $_GET['psu_id'];

// fetch the news information
$sql = "SELECT author FROM psu_news WHERE id = '$id'";
$result = $conn->query($sql);
$result_assoc = $result->fetch_assoc();

// update values
$sql = "UPDATE psu_news SET approved = '2', reviewed_by = '$reviewed_by' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
	send_single_push_notification("PSU Notification", "A news post you made has been rejected", $result_assoc['author']);
    echo "1";
} else {
    echo "0";
}

?>