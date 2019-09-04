<?php
include 'dbconfig.php';
include 'functions.php';

$id = $_GET['id'];

$sql = "SELECT title FROM psu_forums WHERE id = '$id'";
$result = $conn->query($sql);
$result_assoc = $result->fetch_assoc();

$sql = "UPDATE psu_forums SET approved = '1' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
	send_push_notification("PSU Forum", $result_assoc['title']);
    echo "1";
} else {
    echo "0";
}

?>