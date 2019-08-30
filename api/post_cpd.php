<?php
include 'dbconfig.php';
include 'functions.php';

$title = mysqli_real_escape_string($conn, $_POST['title']);
$description = mysqli_real_escape_string($conn, $_POST['description']);
$resource_type = mysqli_real_escape_string($conn, $_POST['resource_type']);
$resource_text = mysqli_real_escape_string($conn, $_POST['resource_text']);
$author_id = mysqli_real_escape_string($conn, $_POST['author_id']);
$timestamp = mysqli_real_escape_string($conn, $_POST['timestamp']);

$sql = "INSERT INTO psu_ecpd (title, description, resource_type, resource_text, author_id, timestamp) VALUES ('$title','$description','$resource_type','$resource_text','$author_id','$timestamp')";

if ($conn->query($sql) === TRUE) {
	send_push_notification("PSU Notification - New eCPD Resource", $title);
    echo $conn->insert_id;
} else {
    echo "0";
}
?>