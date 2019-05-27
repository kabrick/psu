<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT title, text, author_name, timestamp, model, brand, sdk, version, product FROM psu_feedback WHERE id = '$id'";

$result = $conn->query($sql);

$result_assoc = $result->fetch_assoc();

//get the default info
$responce['title'] = $result_assoc['title'];
$responce['text'] = $result_assoc['text'];
$responce['author_name'] = $result_assoc['author_name'];
$responce['timestamp'] = $result_assoc['timestamp'];
$responce['model'] = $result_assoc['model'];
$responce['brand'] = $result_assoc['brand'];
$responce['sdk'] = $result_assoc['sdk'];
$responce['version'] = $result_assoc['version'];
$responce['product'] = $result_assoc['product'];

echo json_encode($responce);

?>