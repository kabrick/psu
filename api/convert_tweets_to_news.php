<?php
include 'dbconfig.php';
include 'functions.php';

$title = mysqli_real_escape_string($conn, $_POST['title']);
$text = mysqli_real_escape_string($conn, $_POST['text']);
$source = mysqli_real_escape_string($conn, $_POST['source']);
$author_id = mysqli_real_escape_string($conn, $_POST['author']);
$timestamp = mysqli_real_escape_string($conn, $_POST['timestamp']);
$picturesUrl = mysqli_real_escape_string($conn, $_POST['photo']);


$sql = "INSERT INTO psu_news (title, text, source, author, timestamp, photo, reviewed_by, approved) VALUES ('$title','$text','$source','$author_id','$timestamp','$picturesUrl', '1060', 1)";

if ($conn->query($sql) === TRUE) {
	send_push_notification("PSU Notification", $title, 0);
	//send_email_global("PSU News Alert", "A news post has been added on the PSU app with title " . $title . ". Please visit the application to view it.");
	return "1";
} else {
    return "0";
}
?>