<?php
include 'functions.php';
include 'dbconfig.php';

$title = mysqli_real_escape_string($conn, $_POST['title']);
$message = mysqli_real_escape_string($conn, $_POST['message']);
$category = mysqli_real_escape_string($conn, $_POST['category']);

send_push_notification($title, $message, $category);

echo "1";

?>