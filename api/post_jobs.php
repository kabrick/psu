<?php
include 'dbconfig.php';

$title = mysqli_real_escape_string($conn, $_POST['title']);
$text = mysqli_real_escape_string($conn, $_POST['text']);
$source = mysqli_real_escape_string($conn, $_POST['source']);
$author_id = mysqli_real_escape_string($conn, $_POST['author_id']);
$timestamp = mysqli_real_escape_string($conn, $_POST['timestamp']);

$sql = "INSERT INTO psu_jobs (title, text, author_id, timestamp, source) VALUES ('$title','$text','$author_id','$timestamp','$source')";

if ($conn->query($sql) === TRUE) {
    echo $conn->insert_id;
} else {
    echo "0";
}
?>