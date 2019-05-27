<?php
include 'dbconfig.php';

$title = mysqli_real_escape_string($conn, $_POST['title']);
$text = mysqli_real_escape_string($conn, $_POST['text']);
$id = mysqli_real_escape_string($conn, $_POST['id']);
$source = mysqli_real_escape_string($conn, $_POST['source']);
$timestamp = mysqli_real_escape_string($conn, $_POST['timestamp']);

//update values
$sql = "UPDATE psu_news SET title='$title',text='$text',timestamp='$timestamp', source='$source' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>