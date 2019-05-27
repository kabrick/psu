<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT title, text, source FROM psu_news WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

//get the default info
$responce['title'] = $result_assoc['title'];
$responce['text'] = $result_assoc['text'];
$responce['source'] = $result_assoc['source'];

echo json_encode($responce);

?>