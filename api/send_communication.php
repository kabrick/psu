<?php
include 'functions.php';
include 'dbconfig.php';

$title = mysqli_real_escape_string($conn, $_POST['title']);
$message = mysqli_real_escape_string($conn, $_POST['message']);
$category = mysqli_real_escape_string($conn, $_POST['category']);
$psu_id = mysqli_real_escape_string($conn, $_POST['psu_id']);

$sql = "INSERT INTO psu_admin_communications (psu_id, title, message, category) VALUES ('$psu_id','$title','$message','$category')";

if ($conn->query($sql) === TRUE) {
	send_push_notification($title, $message, $category);
    echo "1";
} else {
    echo "0";
}

?>