<?php
include 'dbconfig.php';

$id = $_GET['id'];
$title = $_GET['title'];
$text = $_GET['text'];

//update values
$sql = "UPDATE psu_jobs SET title='$title',text='$text' WHERE id = '$id'";

if ($conn->query($sql) === TRUE) {
    echo "1";
} else {
    echo "0";
}

?>