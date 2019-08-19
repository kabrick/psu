<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT title, text, author_id, timestamp, source FROM psu_jobs WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

$id = $result_assoc['author_id'];

//get the author full name
$sql1 = "SELECT name FROM psu_admin WHERE psu_id = '$id'";

$result1 = $conn->query($sql1);
$result_assoc1 = $result1->fetch_assoc();

//get the default info
$responce['author_id'] = $result_assoc['author_id'];
$responce['title'] = $result_assoc['title'];
$responce['text'] = $result_assoc['text'];
$responce['author'] = $result_assoc1['name'];
$responce['timestamp'] = $result_assoc['timestamp'];
$responce['source'] = $result_assoc['source'];

echo json_encode($responce);

?>